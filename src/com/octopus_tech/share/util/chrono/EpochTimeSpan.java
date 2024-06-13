package com.octopus_tech.share.util.chrono;

import java.util.Objects;

public class EpochTimeSpan implements TimeSpan
{
	private static final long serialVersionUID = -3173638321767769468L;
	
	private Epoch epoch1;
	private Epoch epoch2;
	
	public EpochTimeSpan(Epoch epoch1, Epoch epoch2)
	{
		Objects.requireNonNull(epoch1);
		Objects.requireNonNull(epoch2);
		
		if(epoch1.after(epoch2))
		{
			this.epoch1 = epoch2;
			this.epoch2 = epoch1;
		}
		else if(epoch2.after(epoch1))
		{
			this.epoch1 = epoch1;
			this.epoch2 = epoch2;
		}
		else //equal!
		{
			this.epoch1 = epoch1;
			this.epoch2 = epoch2;
		}
	}
	
	public long getSecond()
	{
		long sec = epoch2.epoch - epoch1.epoch;
		return sec;
	}
	
	public TimeVector getTimeVector()
	{
		long sec = epoch2.epoch - epoch1.epoch;
		
		TimeVector ts = new TimeVector();
		ts.day = (sec) / Epoch.ONE_DAY;
		ts.hour = ((sec) % Epoch.ONE_DAY) / Epoch.ONE_HOUR;
		ts.minute = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) / Epoch.ONE_MINUTE;
		ts.second = (((sec) % Epoch.ONE_DAY) % Epoch.ONE_HOUR) % Epoch.ONE_MINUTE;
		return ts;
	}

	public TimeVectorSortOf getTimeVectorSortOf()
	{
		long sec = epoch2.epoch - epoch1.epoch;
		
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
		result = prime * result + ((epoch1 == null) ? 0 : epoch1.hashCode());
		result = prime * result + ((epoch2 == null) ? 0 : epoch2.hashCode());
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
		EpochTimeSpan other = (EpochTimeSpan) obj;
		if (epoch1 == null) {
			if (other.epoch1 != null)
				return false;
		} else if (!epoch1.equals(other.epoch1))
			return false;
		if (epoch2 == null) {
			if (other.epoch2 != null)
				return false;
		} else if (!epoch2.equals(other.epoch2))
			return false;
		return true;
	}

	@Override
	public int compareTo(TimeSpan o)
	{
		return (int)(this.getSecond() - o.getSecond());
	}
}