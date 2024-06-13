package com.octopus_tech.share.util.chrono;

public interface TimeSpan extends Comparable<TimeSpan>
{
	long getSecond();
	TimeVector getTimeVector();
	TimeVectorSortOf getTimeVectorSortOf();
}
