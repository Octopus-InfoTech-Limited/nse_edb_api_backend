package com.octopus_tech.share.db;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.octopus_tech.share.annotation.db.CRUDCC;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDGT;
import com.octopus_tech.share.annotation.db.CRUDIn;
import com.octopus_tech.share.annotation.db.CRUDLT;
import com.octopus_tech.share.annotation.db.CRUDLimit;
import com.octopus_tech.share.annotation.db.CRUDNEQ;
import com.octopus_tech.share.annotation.db.CRUDOtherTable;
import com.octopus_tech.share.annotation.db.CRUDPage;
import com.octopus_tech.share.annotation.db.CRUDSort;
import com.octopus_tech.share.annotation.db.CRUDSort.Order;
import com.octopus_tech.share.db.DBHelper.WhereClause2Condition;

public class CRUDDBHelperWhereBuilder 
{
	private CRUDDBHelperWhereBuilder() {}
	
	public static <T> DBHelper.WhereClause2<T> build(Class<T> dbModelClass, Class<? extends ICRUDRepo<?>> crudClass, boolean list, Method method, Object[] invokeParams) throws IllegalArgumentException
	{
		final Parameter[] methodParams = method.getParameters();
		
		CRUDLimit limit = null;
		CRUDSort[] sorts = null;
		CRUDPage page = null;
		Number pageObject = 0;
		
		if(1 + 1 > 1)
		{
			limit = method.getAnnotation(CRUDLimit.class);
			if(limit != null && limit.value() <= 0)
			{
				throw new IllegalArgumentException("CRUDLimit <= 0");
			}
			
			sorts = method.getAnnotationsByType(CRUDSort.class);
		}

		for(int i=0;i<methodParams.length;i++)
		{
			final Parameter methodParam = methodParams[i];
			if(page != null)
			{
				throw new IllegalArgumentException("Many CRUDPage, only 1 CRUDPage is allowed");
			}
			
			if(methodParam.isAnnotationPresent(CRUDPage.class))
			{
				page = methodParam.getAnnotation(CRUDPage.class);
				pageObject = box(invokeParams[i]);
			}
		}
		
		if(limit == null && page != null)
		{
			throw new IllegalArgumentException("Logic error, CRUDPage exists, but CRUDLimit is not exists");
		}

		if(limit == null && page == null)
		{
			pageObject = null;
		}

		final CRUDSort[] sorts2 = sorts;
		final CRUDLimit limit2 = limit;
		final Number pageObject2 = pageObject;
		final CRUDCC[] ccs = method.getAnnotationsByType(CRUDCC.class);
		
		return new DBHelper.WhereClause2<T>()
		{
			@Override
			public void where(CriteriaBuilder cb, Root<T> root, WhereClause2Condition<T> wc2c) 
			{
				List<Predicate> ps = new ArrayList<Predicate>();
				
				for(int i=0;i<methodParams.length;i++)
				{
					final Parameter methodParam = methodParams[i];

					CRUDOtherTable ot = methodParam.getAnnotation(CRUDOtherTable.class);
					CRUDField f = methodParam.getAnnotation(CRUDField.class);
					if(f == null)
					{
						continue;
					}
					
					Path<?> path = root.get(f.value());
					Predicate p = null;
					if(ot != null)
					{
						path = path.get(ot.field());
					}
					
					if(methodParam.isAnnotationPresent(CRUDEQ.class))
					{
						//CRUDEQ eq = methodParam.getAnnotation(CRUDEQ.class);
						if(invokeParams[i] == null)
						{
							p = cb.isNull(path);
						}
						else
						{
							p = cb.equal(path, invokeParams[i]);
						}
					}
					else if(methodParam.isAnnotationPresent(CRUDNEQ.class))
					{
						if(invokeParams[i] == null)
						{
							p = cb.isNotNull(path);
						}
						else
						{
							p = cb.notEqual(path, invokeParams[i]);
						}
					}
					else if(methodParam.isAnnotationPresent(CRUDLT.class))
					{
						if(invokeParams[i] == null)
						{
							throw new IllegalArgumentException("Logic error, CRUDLT cannot be null value");
						}
						else
						{
							CRUDLT lt = methodParam.getAnnotation(CRUDLT.class);
							if(canBox(invokeParams[i]))
							{
								Number number = box(invokeParams[i]);
								if(lt.includeEqual())
								{
									p = cb.le((Expression<? extends Number>) path, number);
								}
								else
								{
									p = cb.lt((Expression<? extends Number>) path, number);
								}
							}
							else
							{
								try
								{
									if(lt.includeEqual())
									{
										Method m = CriteriaBuilder.class.getMethod("lessThanOrEqualTo", Expression.class, Comparable.class);
										p = (Predicate)m.invoke(cb, path, invokeParams[i]);
									}
									else
									{
										Method m = CriteriaBuilder.class.getMethod("lessThan", Expression.class, Comparable.class);
										p = (Predicate)m.invoke(cb, path, invokeParams[i]);
									}
								}
								catch (Exception e) 
								{
									throw new IllegalArgumentException(e);
								}
							}
						}
					}
					else if(methodParam.isAnnotationPresent(CRUDGT.class))
					{
						if(invokeParams[i] == null)
						{
							throw new IllegalArgumentException("Logic error, CRUDGT cannot be null value");
						}
						else
						{
							CRUDGT gt = methodParam.getAnnotation(CRUDGT.class);
							if(canBox(invokeParams[i]))
							{
								Number number = box(invokeParams[i]);
								if(gt.includeEqual())
								{
									p = cb.ge((Expression<? extends Number>) path, number);
								}
								else
								{
									p = cb.gt((Expression<? extends Number>) path, number);
								}
							}
							else
							{

								try
								{
									if(gt.includeEqual())
									{
										Method m = CriteriaBuilder.class.getMethod("greaterThanOrEqualTo", Expression.class, Comparable.class);
										p = (Predicate)m.invoke(cb, path, invokeParams[i]);
									}
									else
									{
										Method m = CriteriaBuilder.class.getMethod("greaterThan", Expression.class, Comparable.class);
										p = (Predicate)m.invoke(cb, path, invokeParams[i]);
									}
								}
								catch (Exception e) 
								{
									throw new IllegalArgumentException(e);
								}
							}
						}
					}
					else if(methodParam.isAnnotationPresent(CRUDIn.class))
					{
						Collection<?> collections = (Collection<?>)invokeParams[i];
						p = path.in(collections);
					}
					
					if(p != null)
					{
						ps.add(p);
					}
				}

				if(ccs != null)
				{
					for(CRUDCC cc: ccs)
					{
						Predicate p = null;
						Object val = null;
						switch(cc.type())
						{
						case TString:
							val = cc.stringValue();
							break;
						case TLong:
							val = cc.longValue();
							break;
						case TDouble:
							val = cc.doubleValue();
							break;
						default:
							throw new UnsupportedOperationException(cc.type() + " is not support");
						}
						
						switch(cc.operator())
						{
						case Equal:
							p = cb.equal(root.get(cc.field()), val);
							break;
						case NotEqual:
							p = cb.notEqual(root.get(cc.field()), val);
							break;
						case LessThen:
							p = cb.lt(root.get(cc.field()), (Number)val);
							break;
						case GreatThen:
							p = cb.gt(root.get(cc.field()), (Number)val);
							break;
						case LessAndEqual:
							p = cb.le(root.get(cc.field()), (Number)val);
							break;
						case GreatAndEqual:
							p = cb.ge(root.get(cc.field()), (Number)val);
							break;
						case IsNull:
							p = cb.isNull(root.get(cc.field()));
							break;
						case IsNotNull:
							p = cb.isNotNull(root.get(cc.field()));
							break;
						}
						
						if(p != null)
						{
							ps.add(p);
						}
					}
				}
				
				Predicate[] pps = new Predicate[ps.size()];
				ps.toArray(pps);
				if(pps.length > 0)
				{
					wc2c.where(cb.and(pps));
				}
				if(sorts2 != null)
				{
					for(CRUDSort sort2: sorts2)
					{
						if(sort2.order() == Order.ASC)
						{
							wc2c.sortAsc(root.get(sort2.field()));
						}
						else
						{
							wc2c.sortDesc(root.get(sort2.field()));
						}
					}
				}
				if(!list)
				{
					wc2c.limit(1);
				}
				else
				{
					if(pageObject2 != null)
					{
						wc2c.offset(limit2.value() * pageObject2.intValue());
						wc2c.limit(limit2.value());
					}
				}
			}
		};
	}
	
	private static boolean canBox(Object number)
	{
		if(number == null)
		{
			return false;
		}
		else if(number.getClass().isPrimitive())
		{
			return true;
		}
		else if(number instanceof Number)
		{
			return true;
		}
		return false;
	}

	private static Number box(Object number)
	{
		if(number == null)
		{
			return null;
		}
		else if(number.getClass().isPrimitive())
		{
			if(number.getClass() == int.class)
			{
				return box((int)number);
			}
			else if(number.getClass() == long.class)
			{
				return box((long)number);
			}
			else if(number.getClass() == short.class)
			{
				return box((short)number);
			}
			else if(number.getClass() == float.class)
			{
				return box((float)number);
			}
			else if(number.getClass() == double.class)
			{
				return box((double)number);
			}
			else if(number.getClass() == byte.class)
			{
				return box((byte)number);
			}
		}
		/*
		else if(number instanceof Epoch)
		{
			return (int)((Epoch)number).getEpoch();
		}
		else if(number instanceof TimeVector)
		{
			return (int)((TimeVector)number).getTotalSecond();
		}
		else if(number instanceof TimeSpan)
		{
			return (int)((TimeSpan)number).getSecond();
		}
		*/
		else if(number instanceof Number)
		{
			return (Number)number;
		}
		/*
		else if(number instanceof Epoch)
		{
			return (Number)number;
		}
		*/
		throw new IllegalArgumentException(number.toString() + "[" + number.getClass().getName() + "] is not number");
	}

	private static Integer box(int number)
	{
		return Integer.valueOf(number);
	}

	private static Long box(long number)
	{
		return Long.valueOf(number);
	}

	private static Short box(short number)
	{
		return Short.valueOf(number);
	}

	private static Float box(float number)
	{
		return Float.valueOf(number);
	}

	private static Double box(Double number)
	{
		return Double.valueOf(number);
	}
	
	private static Byte box(byte number)
	{
		return Byte.valueOf(number);
	}
}
