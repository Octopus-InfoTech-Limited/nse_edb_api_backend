package com.octopus_tech.share.db;

import java.util.List;
import java.util.Optional;

import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;

public interface ICRUDRepo<T>
{
	void add(T model);
	
	Optional<T> getById(@CRUDEQ @CRUDField("id") int id);
	List<T> listAll();
	
	void update(T model);
	void trash(T model);
}
