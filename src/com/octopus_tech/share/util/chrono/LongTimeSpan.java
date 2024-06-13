package com.octopus_tech.share.util.chrono;

public class LongTimeSpan implements TimeSpan
{
	private static final long serialVersionUID = -3891424136464416015L;
	
	private long a;
	private long b;
	
	public LongTimeSpan(long a, long b)
	{
		if(a > b)
		{
			this.a = b;
			this.b = a;
		}
		else if(a <= b)
		{
			this.a = a;
			this.b = b;
		}
	}

	@Override
	public long getSecond() 
	{
		return b - a;
	}

	@Override
	public TimeVector getTimeVector() 
	{
		long sec = b - a;
		
		TimeVector ts = new TimeVector();
		ts.day = (sec) / Epoch.ONE_DAY;
		ts.hour = ((sec) % Epoch.ONE_DAY) / Epoch.ONE_HOUR;
		ts.minute = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) / Epoch.ONE_MINUTE;
		ts.second = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) % Epoch.ONE_MINUTE;
		return ts;
	}

	@Override
	public TimeVectorSortOf getTimeVectorSortOf() 
	{
		long sec = b - a;
		
		TimeVectorSortOf ts = new TimeVectorSortOf();
		ts.sortOfYear = (sec / Epoch.ONE_YEAR);
		ts.sortOfMonth = (sec % Epoch.ONE_YEAR) / Epoch.ONE_MONTH;
		ts.day = ((sec % Epoch.ONE_YEAR) % Epoch.ONE_MONTH) / Epoch.ONE_DAY;
		ts.hour = (((sec % Epoch.ONE_YEAR) % Epoch.ONE_MONTH) % Epoch.ONE_DAY) / Epoch.ONE_HOUR;
		ts.minute = ((((sec % Epoch.ONE_YEAR) % Epoch.ONE_MONTH) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) / Epoch.ONE_MINUTE;
		ts.second = ((((sec % Epoch.ONE_YEAR) % Epoch.ONE_MONTH) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) % Epoch.ONE_MINUTE;
		return ts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (a ^ (a >>> 32));
		result = prime * result + (int) (b ^ (b >>> 32));
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
		LongTimeSpan other = (LongTimeSpan) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}

	@Override
	public int compareTo(TimeSpan o) 
	{
		return (int)(this.getSecond() - o.getSecond());
	}
}