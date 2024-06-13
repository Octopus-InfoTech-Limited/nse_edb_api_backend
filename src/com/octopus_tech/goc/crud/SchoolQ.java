package com.octopus_tech.goc.crud;

import java.util.Optional;

import com.octopus_tech.goc.model.School;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(School.class)
public interface SchoolQ extends ICRUDRepo<School>
{
	Optional<School> getBySchoolCode(@CRUDEQ @CRUDField("schoolCode") String schoolCode);
}
