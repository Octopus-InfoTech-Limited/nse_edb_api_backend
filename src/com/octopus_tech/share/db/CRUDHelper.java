package com.octopus_tech.share.db;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.criteria.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections8.Reflections;

import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class CRUDHelper 
{
	private CRUDHelper() {}
	
	public static void autowireCRUD(DBHelper dbHelper, DBHelperBasicAction action) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		autowireCRUD(dbHelper, (Object)action);
	}
	
	public static void autowireCRUD(DBHelper dbHelper, Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Objects.requireNonNull(object);
		
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field: fields)
		{
			boolean accessible = field.isAccessible();
			try
			{
				field.setAccessible(true);
				
				Class<?>[] interfaces = field.getType().getInterfaces();
				boolean isCRUD = Arrays.asList(interfaces)
					.stream()
					.filter(t->t == ICRUDRepo.class)
					.findAny()
					.isPresent();
				if(isCRUD)
				{
					Class<?> fieldClass = field.getType();
					Object instance = createCRUD(dbHelper, fieldClass);
					Class<?> type = instance.getClass();
					Object instance2 = fieldClass.cast(instance);
					field.set(object, instance2);
				}
			}
			finally
			{
				field.setAccessible(accessible);
			}
		}
	}
	
	public static <T> T createCRUD(DBHelper dbHelper, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		CRUDRepoModel responder = clazz.getAnnotation(CRUDRepoModel.class);
		if(responder == null)
		{
			LogManager.getLogger(CRUDHelper.class).fatal("instance {} does have annotation @CRUDRepoModel", clazz.getName());
			return null;
		}
		CURDHandler handler = new CURDHandler(dbHelper, responder.value(), (Class<? extends ICRUDRepo<?>>)clazz);
		
		//T object = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
		ProxyFactory pf = new ProxyFactory();
		if(clazz.isAnnotation())
		{
			throw new IllegalArgumentException(clazz.getCanonicalName() + " is annotation");
		}
		else if(clazz.isInterface())
		{
			pf.setInterfaces(new Class<?>[] {clazz});
		}
		else
		{
			pf.setSuperclass(clazz);
		}
		
		try
		{
			T object = (T)pf.create(new Class<?>[0], new Object[0], handler);
			return object;
		}
		catch(Exception e)
		{
			LogManager.getLogger(CRUDHelper.class).fatal("Cannot init proxy class {}", clazz.getName());
			return null;
		}
	}
	
	private static class CURDHandler implements InvocationHandler, MethodHandler
	{
		private Logger logger;
		private DBHelper dbHelper;
		private Class<?> modelClass;
		private Class<? extends ICRUDRepo<?>> crudClass;
		
		public CURDHandler(DBHelper dbHelper, Class<?> modelClass, Class<? extends ICRUDRepo<?>> crudClass)
		{
			logger = LogManager.getLogger(CURDHandler.class);
			this.dbHelper = dbHelper;
			this.modelClass = modelClass;
			this.crudClass = crudClass;
		}

		@Override
		public Object invoke(Object instance, Method method, Object[] params) throws Throwable 
		{
			if(!(instance instanceof ICRUDRepo))
			{
				logger.fatal("instance {} does not implement ICRUD", instance.toString());
				return null;
			}
			
			String methodName = method.getName();
			if("add".equals(methodName))
			{
				dbHelper.save(params[0]);
			}
			else if("update".equals(methodName))
			{
				dbHelper.update(params[0]);
			}
			else if("trash".equals(methodName))
			{
				dbHelper.delete(params[0]);
			}
			else if(methodName.equals("getById"))
			{
				return dbHelper.queryOne(modelClass, "id", params[0]);
			}
			else if(methodName.equals("list"))
			{
				return dbHelper.query(modelClass);
			}
			else
			{
				if(methodName.startsWith("get"))
				{
					DBHelper.WhereClause2<?> wc = CRUDDBHelperWhereBuilder.build(modelClass, crudClass, false, method, params);
					Method m = DBHelper.class.getMethod("query", Class.class, DBHelper.WhereClause2.class);
					return ((List<?>)m.invoke(dbHelper, modelClass, wc)).stream().findFirst();
				}
				else if(methodName.startsWith("list"))
				{
					DBHelper.WhereClause2<?> wc = CRUDDBHelperWhereBuilder.build(modelClass, crudClass, true, method, params);
					Method m = DBHelper.class.getMethod("query", Class.class, DBHelper.WhereClause2.class);
					return m.invoke(dbHelper, modelClass, wc);
				}
				else if("toString".equals(methodName))
				{
					return "CRUDHelper of " + modelClass.toString();
				}
				else if("equals".equals(methodName))
				{
					return this == params[0];
				}
				else if("hashCode".equals(methodName))
				{
					return Objects.hashCode(this);
				}
				else if("finalize".equals(methodName))
				{
					return null;
				}
				else
				{
					/*
					ICRUD crud = (ICRUD) Proxy.newProxyInstance(
			            Thread.currentThread().getContextClassLoader(),
			            new Class[] { crudClass },
			            (proxy, method2, args) -> 
			            {
			            	return MethodHandles
			                    .lookup()
			                    .in(crudClass)
			                    .unreflectSpecial(method, crudClass)
			                    .bindTo(proxy)
			                    .invokeWithArguments(args);
			            }
			        );
					method.setAccessible(true);
					method.invoke(crud, params);
					*/
					
					logger.fatal("CRUDHelper does not support method {}", methodName);
					throw new IllegalAccessError("CRUDHelper does not support method " + methodName);
				}
			}
			
			return null;
		}

		@Override
		public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable
		{
			return invoke(self, thisMethod, args);
		}
	}

	private static String upperFirstChar(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	private static String lowerFirstChar(String str)
	{
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
}
