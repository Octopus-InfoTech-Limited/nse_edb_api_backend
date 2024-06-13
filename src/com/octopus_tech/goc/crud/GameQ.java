package com.octopus_tech.goc.crud;

import com.octopus_tech.goc.model.Game;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(Game.class)
public interface GameQ extends ICRUDRepo<Game> 
{

}
