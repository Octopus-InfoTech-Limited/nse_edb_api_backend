package com.octopus_tech.share.util.chrono;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class TimeVectorTypeAdapter extends TypeAdapter<TimeVector> {

	@Override
	public TimeVector read(JsonReader json) throws IOException
	{
		boolean read = false;
		long value = 0;
		try
		{
			value = json.nextLong();
			read = true;
		}
		catch(Exception e)
		{
			json.skipValue();
		}
		
		if(read)
		{
			return new TimeVector(value);
		}
		return null;
	}

	@Override
	public void write(JsonWriter json, TimeVector tv) throws IOException
	{
		if(tv == null)
		{
			json.nullValue();
		}
		else
		{
			json.value(tv.getTotalSecond());
		}
	}

}
