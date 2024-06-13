package com.octopus_tech.goc.crud;

import java.util.List;

import com.octopus_tech.goc.model.ClickRecord;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(ClickRecord.class)
public interface ClickRecordQ extends ICRUDRepo<ClickRecord> {
	List<ClickRecord> listByItem_Id(Integer itemId);
}
