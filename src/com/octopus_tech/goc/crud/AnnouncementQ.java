package com.octopus_tech.goc.crud;

import java.util.List;

import com.octopus_tech.goc.model.Announcement;
import com.octopus_tech.share.annotation.db.CRUDCC;
import com.octopus_tech.share.annotation.db.CRUDCC.Operator;
import com.octopus_tech.share.annotation.db.CRUDCC.Type;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDGT;
import com.octopus_tech.share.annotation.db.CRUDLT;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;
import com.octopus_tech.share.util.chrono.Epoch;

@CRUDRepoModel(Announcement.class)
public interface AnnouncementQ extends ICRUDRepo<Announcement> 
{
	@CRUDCC(field = "deleted", type = Type.TLong, operator = Operator.Equal, longValue = 0)
	List<Announcement> listAnnouncements();
	
	@CRUDCC(field = "deleted", type = Type.TLong, operator = Operator.Equal, longValue = 0)
	List<Announcement> listAnnouncementsBetween(
			@CRUDLT(includeEqual = true) @CRUDField("start") Epoch start,
			@CRUDGT(includeEqual = true) @CRUDField("end") Epoch end
	);
}
