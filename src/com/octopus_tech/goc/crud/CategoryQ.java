package com.octopus_tech.goc.crud;

import com.octopus_tech.goc.model.Category;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(Category.class)
public interface CategoryQ extends ICRUDRepo<Category> {
}
