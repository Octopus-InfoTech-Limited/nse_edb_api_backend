package com.octopus_tech.share.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil
{
	private HashUtil() {}
	
	public static String hash(String str)
	{
		return DigestUtils.md5Hex(str).toLowerCase();
	}
}
