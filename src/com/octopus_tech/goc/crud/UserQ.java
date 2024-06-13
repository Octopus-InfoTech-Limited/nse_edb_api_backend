package com.octopus_tech.goc.crud;

import java.util.List;
import java.util.Optional;

import com.octopus_tech.goc.model.School;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDLimit;
import com.octopus_tech.share.annotation.db.CRUDPage;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.annotation.db.CRUDSort;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(User.class)
public interface UserQ extends ICRUDRepo<User> 
{
	Optional<User> getByLoginId(@CRUDEQ @CRUDField("loginId") String loginId);
	
	@CRUDLimit(10)
	@CRUDSort(field = "name_en")
	List<User> listAllStudentInSchool(@CRUDEQ @CRUDField("school") School school, @CRUDPage int page);
	
	@CRUDLimit(10)
	@CRUDSort(field = "name_en")
	List<User> listAllStudentOfAllSchools(@CRUDPage int page);
	
	Optional<User> getByProfileId(@CRUDEQ @CRUDField("edConnectProfileId") int edConnectProfileId);
}
