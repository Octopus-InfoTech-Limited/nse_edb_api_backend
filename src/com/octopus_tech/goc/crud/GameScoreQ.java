package com.octopus_tech.goc.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.GameScore;
import com.octopus_tech.goc.model.News;
import com.octopus_tech.goc.model.School;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.annotation.db.CRUDCC;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.annotation.db.CRUDSort;
import com.octopus_tech.share.annotation.db.CRUDSort.Order;
import com.octopus_tech.share.annotation.db.CRUDCC.Operator;
import com.octopus_tech.share.annotation.db.CRUDCC.Type;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(GameScore.class)
public interface GameScoreQ extends ICRUDRepo<GameScore>
{
	Optional<GameScore> getByGameAndUser(@CRUDEQ @CRUDField("game") Game game, @CRUDEQ @CRUDField("user") User user);
	
	@CRUDSort(field = "finish_date", order=Order.DESC)
	List<GameScore> listByGameAndUser(@CRUDEQ @CRUDField("game") Game game, @CRUDEQ @CRUDField("user") User user);
}
