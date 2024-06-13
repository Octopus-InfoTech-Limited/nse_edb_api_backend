package com.octopus_tech.share.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.octopus_tech.share.model.DBModel;

@CRUDParamAnnotation
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CRUDOtherTable 
{
	String value();
	Class<? extends DBModel> model();
	String field() default "id";
}
