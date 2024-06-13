package com.octopus_tech.share.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@CRUDParamAnnotation
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CRUDSortGroup
{
	CRUDSort[] value() default {};
}
