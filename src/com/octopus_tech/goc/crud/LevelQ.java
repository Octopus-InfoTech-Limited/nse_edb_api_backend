package com.octopus_tech.goc.crud;

import com.octopus_tech.goc.model.Level;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(Level.class)
public interface LevelQ extends ICRUDRepo<Level> {
}
