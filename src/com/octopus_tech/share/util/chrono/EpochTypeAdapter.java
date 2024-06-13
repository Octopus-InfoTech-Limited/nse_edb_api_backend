package com.octopus_tech.share.util.chrono;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class EpochTypeAdapter extends TypeAdapter<Epoch>
{
	@Override
	public Epoch read(JsonReader j) throws IOException
	{
		boolean read = false;
		long value = 0;
		try
		{
			value = j.nextLong();
			read = true;
		}
		catch(Exception e)
		{
			j.skipValue();
		}
		
		if(read)
		{
			return new Epoch(value);
		}
		return null;
	}

	@Override
	public void write(JsonWriter j, Epoch e) throws IOException 
	{
		if(e == null)
		{
			j.nullValue();
		}
		else
		{
			j.value(e.epoch);
		}
	}
}
