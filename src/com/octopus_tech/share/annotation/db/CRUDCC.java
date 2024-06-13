package com.octopus_tech.share.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@CRUDMethodAnnotation
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CRUDCCGroup.class)
public @interface CRUDCC 
{
	String field();
	Operator operator();
	Type type();
	String stringValue() default "";
	long longValue() default 0;
	double doubleValue() default 0.0;
	
	public enum Operator
	{
		Equal, NotEqual, LessThen, LessAndEqual, GreatThen, GreatAndEqual, IsNull, IsNotNull
	}
	public enum Type
	{
		TString, TLong, TDouble, TNull
	}
}
