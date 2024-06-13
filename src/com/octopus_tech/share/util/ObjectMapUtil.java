package com.octopus_tech.share.util;

import java.util.Map;

import com.octopus_tech.share.action.DBHelperBasicAction;

public class ObjectMapUtil
{
	private ObjectMapUtil() {}

	public static final Map<String, Object> objectToMap(Object object)
	{
		String json = DBHelperBasicAction.gson.toJson(object);
		return DBHelperBasicAction.gson.fromJson(json, Map.class);
	}
	
	public static final <T> T mapToObject(Class<T> clazz, Map<String, ? extends Object> map)
	{
		String json = DBHelperBasicAction.gson.toJson(map);
		return DBHelperBasicAction.gson.fromJson(json, clazz);
	}
}
