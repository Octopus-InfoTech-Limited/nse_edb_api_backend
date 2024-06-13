package com.octopus_tech.share.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class Argon2Util
{
	private Argon2Util() {}
	
	public static String generateHash(String str)
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties();
		
		Argon2 argon2 = Argon2Factory.create(
				Argon2Types.ARGON2id, 
				Integer.parseInt(ep.getProperty("argon2.length.salt")),
				Integer.parseInt(ep.getProperty("argon2.length.hash"))
		);
		
		String hash = argon2.hash(
				Integer.parseInt(ep.getProperty("argon2.iterations")), 
				Integer.parseInt(ep.getProperty("argon2.memory")), 
				Integer.parseInt(ep.getProperty("argon2.parallelism")), 
				str.toCharArray()
		);
		
		return hash;
	}
	
	public static boolean verify(String hash, String str)
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties();
		
		Argon2 argon2 = Argon2Factory.create(
				Argon2Types.ARGON2id, 
				Integer.parseInt(ep.getProperty("argon2.length.salt")),
				Integer.parseInt(ep.getProperty("argon2.length.hash"))
		);
		return argon2.verify(hash, str.toCharArray());
	}
}
