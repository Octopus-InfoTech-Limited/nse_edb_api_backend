package com.octopus_tech.goc.crud;

import com.octopus_tech.goc.model.Item;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(Item.class)
public interface ItemQ extends ICRUDRepo<Item> {
}
