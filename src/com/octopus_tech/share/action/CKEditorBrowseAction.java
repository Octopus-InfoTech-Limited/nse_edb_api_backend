package com.octopus_tech.share.action;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.reflections8.Reflections;
import org.reflections8.scanners.MethodAnnotationsScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;

import com.octopus_tech.share.annotation.CKEditorValidatorMethod;
import com.opensymphony.xwork2.ActionSupport;

/*
@ParentPackage("struts-default")
@Namespace("ckeditor")
@Action(value = "browse")
@Results({
	@Result(name=ActionSupport.SUCCESS, location = "/WEB-INF/share/ckeditor_browse.jsp"),
	@Result(name=ActionSupport.ERROR, location = "/WEB-INF/share/error.jsp"),
	@Result(name="exception", location = "/WEB-INF/share/exception.jsp")
})
@ExceptionMappings({
	@ExceptionMapping(exception = "java.lang.Exception", result = "exception")
})
*/
public class CKEditorBrowseAction extends ActionSupport implements ServletContextAware
{
	private static final long serialVersionUID = 4943668863837889793L;

	public String CKEditor;
	public String CKEditorFuncNum;
	
	public ArrayList<F> files = new ArrayList<>();
	
	private ServletContext servletContext;
	
	@Override
	public String execute() throws Exception
	{
		if(!CKEditorBrowseAction.validate2())
		{
			return ERROR;
		}

		if(CKEditor == null || CKEditorFuncNum == null)
		{
			return ERROR;
		}
		
		File dir = new File(servletContext.getRealPath("/uploads"));
		dir.mkdirs();
		
		for(File file: dir.listFiles())
		{
			F f = new F();
			f.url = "/uploads/" + file.getName();
			files.add(f);
		}
		
		return SUCCESS;
	}

	@Override
	public void setServletContext(ServletContext context)
	{
		this.servletContext = context;
	}
	
	static boolean validate2() throws Exception
	{
		ClassLoader[] classLoaders = new ClassLoader[] {
				ClasspathHelper.contextClassLoader(),
				ClasspathHelper.staticClassLoader()
		};
		
		ConfigurationBuilder cb = new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forClassLoader(classLoaders))
				.addScanners(new MethodAnnotationsScanner());
		Reflections ref = new Reflections(cb);
		Set<Method> methods = ref.getMethodsAnnotatedWith(CKEditorValidatorMethod.class);
		Map<Method, Class<?>> map = methods.stream()
				.collect(Collectors.toMap(Function.identity(), Method::getDeclaringClass));
		
		boolean allPassed = false;
		for(Map.Entry<Method, Class<?>> entry:map.entrySet())
		{
			Object instance = entry.getValue().newInstance();
			Object ret = entry.getKey().invoke(instance);
			if(ret == null)
			{
				throw new RuntimeException(entry.getKey().getName() + " returns null value");
			}
			if(!(ret instanceof Boolean))
			{
				throw new RuntimeException(entry.getKey().getName() + " returns non boolean typed value");
			}
			Boolean b = (Boolean)ret;
			if(!b.booleanValue())
			{
				allPassed = false;
				break;
			}
			else
			{
				allPassed = true;
			}
		}
		
		return allPassed;
	}
	
	public static class F
	{
		public String url;
	}
}
