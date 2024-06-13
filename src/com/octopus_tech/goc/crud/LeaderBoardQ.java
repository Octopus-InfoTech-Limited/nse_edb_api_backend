package com.octopus_tech.goc.crud;

import java.util.List;

import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.Leaderboard;
import com.octopus_tech.goc.model.School;
import com.octopus_tech.share.annotation.db.CRUDCC;
import com.octopus_tech.share.annotation.db.CRUDCC.Operator;
import com.octopus_tech.share.annotation.db.CRUDCC.Type;
import com.octopus_tech.share.annotation.db.CRUDEQ;
import com.octopus_tech.share.annotation.db.CRUDField;
import com.octopus_tech.share.annotation.db.CRUDLimit;
import com.octopus_tech.share.annotation.db.CRUDPage;
import com.octopus_tech.share.annotation.db.CRUDRepoModel;
import com.octopus_tech.share.annotation.db.CRUDSort;
import com.octopus_tech.share.annotation.db.CRUDSort.Order;
import com.octopus_tech.share.db.ICRUDRepo;

@CRUDRepoModel(Leaderboard.class)
public interface LeaderBoardQ extends ICRUDRepo<Leaderboard>
{
	@CRUDSort(field = "score", order = Order.DESC)
	@CRUDLimit(10)
	@CRUDCC(field = "type", type = Type.TString, operator = Operator.Equal, stringValue = Leaderboard.TYPE_PERSONAL_SCORE)
	List<Leaderboard> listTop10GameScoreWithInSchool(
			@CRUDEQ @CRUDField("game") Game game,
			@CRUDEQ @CRUDField("school") School school,
			@CRUDPage int page
	);
	

	@CRUDSort(field = "score", order = Order.DESC)
	@CRUDLimit(100)
	@CRUDCC(field = "type", type = Type.TString, operator = Operator.Equal, stringValue = Leaderboard.TYPE_PERSONAL_SCORE)
	List<Leaderboard> listTop100GameScoreWithInSchool(
			@CRUDEQ @CRUDField("game") Game game,
			@CRUDEQ @CRUDField("school") School school,
			@CRUDPage int page
	);
	

	@CRUDSort(field = "score", order = Order.DESC)
	@CRUDLimit(100)
	@CRUDCC(field = "type", type = Type.TString, operator = Operator.Equal, stringValue = Leaderboard.TYPE_SCHOOL_SCORE)
	List<Leaderboard> listTop100GameScore(
			@CRUDEQ @CRUDField("game") Game game,
			@CRUDPage int page
	);

	@CRUDSort(field = "score", order = Order.DESC)
	@CRUDCC(field = "type", type = Type.TString, operator = Operator.Equal, stringValue = Leaderboard.TYPE_SCHOOL_SCORE)
	List<Leaderboard> listSchoolGameScoreInAllGames();
	

	@CRUDSort(field = "score", order = Order.DESC)
	@CRUDCC(field = "type", type = Type.TString, operator = Operator.Equal, stringValue = Leaderboard.TYPE_PERSONAL_SCORE)
	List<Leaderboard> listPersonGameScoreWithInSchool(
			@CRUDEQ @CRUDField("school") School school
	);
}
