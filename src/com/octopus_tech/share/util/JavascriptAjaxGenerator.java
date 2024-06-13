package com.octopus_tech.share.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.annotation.AjaxParam;
import com.octopus_tech.share.annotation.AjaxSupport;

public class JavascriptAjaxGenerator 
{
	private JavascriptAjaxGenerator() {}
	
	public static String generateJavascriptFunction(Class<?> clazz, String applicationName)
	{
		AjaxSupport ajaxSupport = clazz.getAnnotation(AjaxSupport.class);
		if(ajaxSupport == null)
		{
			throw new IllegalArgumentException("class " + clazz.getCanonicalName() + " has no annotation @AjaxSupport");
		}
		
		List<Pair> params = new ArrayList<>();
		for(Field f:clazz.getDeclaredFields())
		{
			AjaxParam ajaxParam = f.getAnnotation(AjaxParam.class);
			if(ajaxParam != null)
			{
				params.add(new Pair(ajaxParam.order(), f.getName()));
			}
		}
		params = params.stream()
				.sorted(Comparator.comparing(Pair::getOrder))
				.collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		sb.append("function ");
		sb.append(lowerFirstChar(clazz.getSimpleName()));
		sb.append(" (");
		for(int i=0;i<params.size();i++)
		{
			Pair pair = params.get(i);
			if(i > 0)
			{
				sb.append(",");
			}
			sb.append(String.format("$%s", pair.name));
		}
		sb.append(") {");
		sb.append("\r\n");
		sb.append("const opts = {\r\n");
		sb.append(String.format("method: '%s',\r\n", ajaxSupport.method()));
		sb.append(String.format("url: '/%s%s',\r\n", applicationName, ajaxSupport.url()));
		sb.append("dataType: 'JSON',\r\n");
		sb.append("data: {\r\n");
		for(Pair pair: params)
		{
			sb.append(String.format("'%s': $%s,\r\n", pair.name, pair.name));
		}
		sb.append("\r\n},}");
		sb.append("\r\nreturn $.ajax(opts);");
		sb.append("\r\n}");
		
		return sb.toString();
	}
	
	private static String lowerFirstChar(String str)
	{
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	private static class Pair
	{
		final int order;
		final String name;
		public Pair(int order, String name) {
			super();
			this.order = order;
			this.name = name;
		}
		public int getOrder() {
			return order;
		}
		public String getName() {
			return name;
		}
	}
}
