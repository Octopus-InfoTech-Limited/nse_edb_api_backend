package com.octopus_tech.share.util.chrono;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Epoch implements UserType, Serializable, JsonSerializer<Epoch>, JsonDeserializer<Epoch>, Comparable<Epoch>
{
	private static final long serialVersionUID = 666811627227140264L;
	
	private static final int[] SQL_TYPES = new int[] 
	{
		java.sql.Types.INTEGER,
	};
	
	public static final long ONE_SECOND = 1L;
	public static final long ONE_MINUTE = 60L;
	public static final long ONE_HOUR = 3600L;
	public static final long ONE_DAY = 86400L;
	public static final long ONE_WEEK = ONE_DAY * 7L;
	public static final long ONE_MONTH = ONE_DAY * 30L;
	public static final long ONE_SEASON = ONE_DAY * 90L;
	public static final long ONE_YEAR = ONE_DAY * 365L;
	
	public static final Epoch ZERO = new Epoch();
	
	final long epoch;
	
	public static Epoch now()
	{
		return new Epoch(System.currentTimeMillis() / 1000L);
	}

	public static Epoch fromPaypalDateFormat(String date) throws IllegalArgumentException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try
		{
			Date _date = sdf.parse(date);
			return new Epoch(_date);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	public Epoch()
	{
		this(0);
	}
	
	public Epoch(long epoch)
	{
		this.epoch = epoch;
	}
	
	public Epoch(Date date)
	{
		Objects.requireNonNull(date);
		epoch = date.getTime() / 1000L;
	}
	
	@Override
	public String toString()
	{
		return toDateString();
	}
	
	public String toEpochString()
	{
		return "" + epoch;
	}
	
	public String toDateString()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epoch * 1000L);
		Date date = cal.getTime();
		return date.toString();
	}
	
	public long getEpoch()
	{
		return epoch;
	}
	
	public Date getDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epoch * 1000L);
		return cal.getTime();
	}
	
	public boolean before(Epoch e)
	{
		Objects.requireNonNull(e);
		return this.epoch < e.epoch;
	}

	public boolean after(Epoch e)
	{
		Objects.requireNonNull(e);
		return this.epoch > e.epoch;
	}
	
	public boolean equals(Epoch e)
	{
		Objects.requireNonNull(e);
		return this.epoch == e.epoch;
	}

	public boolean equalAndAfter(Epoch eu)
	{
		Objects.requireNonNull(eu);
		return equals(eu) && after(eu);
	}
	
	public boolean equalAndBefore(Epoch eu)
	{
		Objects.requireNonNull(eu);
		return equals(eu) && before(eu);
	}
	
	public Epoch add(TimeVector ts)
	{
		Objects.requireNonNull(ts);
		Epoch eu = new Epoch(epoch + ts.getTotalSecond());
		return eu;
	}

	public Epoch add(long second)
	{
		Epoch eu = new Epoch(epoch + second);
		return eu;
	}
	
	public Epoch subtract(TimeVector ts)
	{
		Objects.requireNonNull(ts);
		Epoch eu = new Epoch(epoch - ts.getTotalSecond());
		return eu;
	}
	
	public Epoch subtract(long second)
	{
		Epoch eu = new Epoch(epoch - second);
		return eu;
	}
	
	public TimeSpan timeSpanWith(Epoch eu)
	{
		Objects.requireNonNull(eu);
		return new EpochTimeSpan(this, eu);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (epoch ^ (epoch >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Epoch other = (Epoch) obj;
		if (epoch != other.epoch)
			return false;
		return true;
	}
	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public Object assemble(Serializable serializable, Object owner) throws HibernateException
	{
		return serializable;
	}

	@Override
	public Object deepCopy(Object object) throws HibernateException 
	{
		return object;
	}

	@Override
	public Serializable disassemble(Object owner) throws HibernateException 
	{
		if(owner instanceof Serializable)
		{
			return (Serializable) owner;
		}
		return null;
	}

	@Override
	public boolean equals(Object a, Object b) throws HibernateException 
	{
		return Objects.equals(a, b);
	}

	@Override
	public int hashCode(Object object) throws HibernateException 
	{
		return Objects.hashCode(object);
	}

	@Override
	public boolean isMutable() 
	{
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor implementor, Object owner)
			throws HibernateException, SQLException
	{
		long epoch = rs.getLong(names[0]);
		if(rs.wasNull())
		{
			return null;
		}
		return new Epoch(epoch);
	}

	@Override
	public void nullSafeSet(PreparedStatement ps, Object value, int index, SharedSessionContractImplementor implementor)
			throws HibernateException, SQLException
	{
		if(value == null)
		{
			ps.setNull(index, java.sql.Types.INTEGER);
		}
		else
		{
			Epoch epoch = (Epoch)value;
			ps.setLong(index, epoch.epoch);
		}
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException 
	{
		return original;
	}

	@Override
	public Class<?> returnedClass() 
	{
		return Epoch.class;
	}

	@Override
	public int[] sqlTypes() 
	{
		return SQL_TYPES;
	}

	@Override
	public Epoch deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		return new Epoch(je.getAsLong());
	}

	@Override
	public JsonElement serialize(Epoch epoch, Type type, JsonSerializationContext context)
	{
		return new JsonPrimitive(epoch.epoch);
	}

	@Override
	public int compareTo(Epoch o)
	{
		return (int)(this.getEpoch() - o.getEpoch());
	}
}
