package com.octopus_tech.share.util.chrono;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class TimeVector implements UserType, Serializable, Comparable<TimeVector>
{
	private static final long serialVersionUID = -735523136152575752L;

	public static final TimeVector ZERO = new TimeVector();
	
	long second;
	long minute;
	long hour;
	long day;
	
	public TimeVector() {this(0);}

	public TimeVector(long second) {this(0, second);}

	public TimeVector(long minute, long second) {this(0, minute, second);}
	
	public TimeVector(long hour, long minute, long second) {this(0, hour, minute, second);}
	
	public TimeVector(long day, long hour, long minute, long second)
	{
		if(second < 0)
		{
			throw new IllegalArgumentException("second < 0");
		}
		if(minute < 0)
		{
			throw new IllegalArgumentException("minute < 0");
		}
		if(hour < 0)
		{
			throw new IllegalArgumentException("hour < 0");
		}
		if(day < 0)
		{
			throw new IllegalArgumentException("day < 0");
		}

		this.second = second;
		this.minute = minute;
		this.hour = hour;
		this.day = day;
		
		if(second >= 60)
		{
			double s = second / 60.0;
			int u = (int)Math.ceil(s);
			this.minute += u;
			this.second -= 60 * u;
		}
		
		if(minute >= 60)
		{
			double s = minute / 60.0;
			int u = (int)Math.ceil(s);
			this.hour += u;
			this.minute -= 60 * u;
		}
		
		if(hour >= 24)
		{
			double s = minute / 24.0;
			int u = (int)Math.ceil(s);
			this.day += u;
			this.hour -= 24 * u;
		}
	}
	
	public TimeVector(TimeVector tv)
	{
		this.second = tv.second;
		this.minute = tv.minute;
		this.hour = tv.hour;
		this.day = tv.day;
	}
	
	public long getTotalSecond()
	{
		return second + (minute * Epoch.ONE_MINUTE) + (hour * Epoch.ONE_HOUR) + (day * Epoch.ONE_DAY);
	}
	
	public TimeSpan timeSpanWith(TimeVector tv)
	{
		return new LongTimeSpan(getTotalSecond(), tv.getTotalSecond());
	}
	
	public long getSecond() {
		return second;
	}

	public long getMinute() {
		return minute;
	}

	public long getHour() {
		return hour;
	}

	public long getDay() {
		return day;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (day ^ (day >>> 32));
		result = prime * result + (int) (hour ^ (hour >>> 32));
		result = prime * result + (int) (minute ^ (minute >>> 32));
		result = prime * result + (int) (second ^ (second >>> 32));
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
		TimeVector other = (TimeVector) obj;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (minute != other.minute)
			return false;
		if (second != other.second)
			return false;
		return true;
	}
	
	@Override
	public String toString() 
	{
		return String.format("%d Day %02d Hour %02d Minute %02d Second", day, hour, minute, second);
	}

	@Override
	public Object assemble(Serializable serializable, Object owner) throws HibernateException 
	{
		return serializable;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException 
	{
		if(!(value instanceof TimeVector))
		{
			return null;
		}
		return new TimeVector((TimeVector)value);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		if(value instanceof Serializable)
		{
			return (Serializable)value;
		}
		return null;
	}

	@Override
	public boolean equals(Object a, Object b) throws HibernateException
	{
		return Objects.equals(a, b);
	}

	@Override
	public int hashCode(Object value) throws HibernateException
	{
		return Objects.hashCode(value);
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
		long sec = rs.getLong(names[0]);
		if(rs.wasNull())
		{
			return null;
		}
		if(sec < 0)
		{
			throw new SQLException("time vector should not less then zero");
		}
		
		TimeVector ts = new TimeVector();
		ts.day = (sec) / Epoch.ONE_DAY;
		ts.hour = ((sec) % Epoch.ONE_DAY) / Epoch.ONE_HOUR;
		ts.minute = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) / Epoch.ONE_MINUTE;
		ts.second = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) % Epoch.ONE_MINUTE;
		return ts;
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
			TimeVector tv = (TimeVector)value;
			if(tv.getTotalSecond() < 0)
			{
				throw new SQLException("time vector should not less then zero");
			}
			ps.setLong(index, tv.getTotalSecond());
		}
	}

	@Override
	public Object replace(Object replace, Object target, Object owner) throws HibernateException 
	{
		return replace;
	}

	@Override
	public Class<?> returnedClass() 
	{
		return TimeVector.class;
	}

	@Override
	public int[] sqlTypes()
	{
		return new int[] {java.sql.Types.INTEGER};
	}

	@Override
	public int compareTo(TimeVector o) 
	{
		return (int)(this.getTotalSecond() - o.getTotalSecond());
	}
}