package com.octopus_tech.goc.crud;

import java.util.List;

import com.octopus_tech.goc.model.News;
import com.octopus_tech.goc.model.School;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.annotation.db.CRUDCC;
import com.octopus_tech.share.annotation.db.CRUDCC.Operator;
import com.octopus_tech.share.annotation.db.CRUDCC.Type;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDLimit;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.annotation.db.CRUDSort;
import com.octopus_tech.share.annotation.db.CRUDSort.Order;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(News.class)
public interface NewsQ extends ICRUDRepo<News>
{
	List<News> listByUser(User user);
	
	@CRUDSort(field = "order")
	@CRUDCC(field = "status", operator = Operator.Equal, type = Type.TString, stringValue = News.STATUS_SHOW)
	List<News> listBySchool(
			@CRUDEQ @CRUDField("school") School school
	);
}
