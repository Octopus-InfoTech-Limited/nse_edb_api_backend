package com.octopus_tech.share.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.octopus_tech.share.model.DBModel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CRUDRepoModel 
{
	Class<? extends DBModel> value();
}
