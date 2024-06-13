package com.octopus_tech.share.cron;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(CronClassGroup.class)
public @interface CronClass 
{
	int day() default -1;
	int hour() default -1;
	int minute() default -1;
	int second() default -1;
}
 