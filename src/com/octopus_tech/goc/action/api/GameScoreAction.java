package com.octopus_tech.goc.action.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.octopus_tech.goc.helpers.StringResultCache;
import com.octopus_tech.share.util.EnhancedProperties;
import com.octopus_tech.share.util.PropertiesHelper;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import com.octopus_tech.goc.crud.GameQ;
import com.octopus_tech.goc.crud.GameScoreQ;
import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.GameResult;
import com.octopus_tech.goc.model.GameScore;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;

public class GameScoreAction extends BasicApiAction
{
	private static final long serialVersionUID = 3809489765092686149L;
	
	public Integer gameId;
	public Integer score;
	
	private GameQ gameQ;
	private GameScoreQ gameScoreQ;
	
	public String json;
	public String method;

	private static final StringResultCache cachedGameScoreFilterStudentScoreRank = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterSchoolStudentNumRank = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterSchoolScoreRank = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterSchoolScoreRankPri = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterSchoolScoreRankSec = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterCompetitionScorePri = new StringResultCache();
	private static final StringResultCache cachedGameScoreFilterCompetitionScoreSec = new StringResultCache();

	private static final StringResultCache resultCacheForGameScoreFilterStudentScoreRank = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterSchoolStudentNumRank = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterSchoolScoreRank = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterSchoolScoreRankPri = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterSchoolScoreRankSec = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterCompetitionScorePri = new StringResultCache();
	private static final StringResultCache resultCacheForGameScoreFilterCompetitionScoreSec = new StringResultCache();

	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties(servletContext);

		String _cachedGameScoreFilterStudentScoreRank = cachedGameScoreFilterStudentScoreRank.getCachedResult();
		if (_cachedGameScoreFilterStudentScoreRank == null) {
			cachedGameScoreFilterStudentScoreRank.cacheStringResult(ep.getComplexProperty("gamescore.filter.studentscorerank", " u.roles = 'student' "));
			_cachedGameScoreFilterStudentScoreRank = cachedGameScoreFilterStudentScoreRank.getCachedResult();
		}

		String _cachedGameScoreFilterSchoolStudentNumRank = cachedGameScoreFilterSchoolStudentNumRank.getCachedResult();
		if (_cachedGameScoreFilterSchoolStudentNumRank == null) {
			cachedGameScoreFilterSchoolStudentNumRank.cacheStringResult(ep.getComplexProperty("gamescore.filter.schoolstudentnumrank", " u.roles = 'student' "));
			_cachedGameScoreFilterSchoolStudentNumRank = cachedGameScoreFilterSchoolStudentNumRank.getCachedResult();
		}

		String _cachedGameScoreFilterSchoolScoreRank = cachedGameScoreFilterSchoolScoreRank.getCachedResult();
		if (_cachedGameScoreFilterSchoolScoreRank == null) {
			cachedGameScoreFilterSchoolScoreRank.cacheStringResult(ep.getComplexProperty("gamescore.filter.schoolscorerank", " u.roles = 'student' "));
			_cachedGameScoreFilterSchoolScoreRank = cachedGameScoreFilterSchoolScoreRank.getCachedResult();
		}

		String _cachedGameScoreFilterSchoolScoreRankPri = cachedGameScoreFilterSchoolScoreRankPri.getCachedResult();
		if (_cachedGameScoreFilterSchoolScoreRankPri == null) {
			cachedGameScoreFilterSchoolScoreRankPri.cacheStringResult(ep.getComplexProperty("gamescore.filter.schoolscorerankpri", " u.roles = 'student' and u.school_level = 1 " ));
			_cachedGameScoreFilterSchoolScoreRankPri = cachedGameScoreFilterSchoolScoreRankPri.getCachedResult();
		}

		String _cachedGameScoreFilterSchoolScoreRankSec = cachedGameScoreFilterSchoolScoreRankSec.getCachedResult();
		if (_cachedGameScoreFilterSchoolScoreRankSec == null) {
			cachedGameScoreFilterSchoolScoreRankSec.cacheStringResult(ep.getComplexProperty("gamescore.filter.schoolscoreranksec", " u.roles = 'student' and u.school_level = 2 " ));
			_cachedGameScoreFilterSchoolScoreRankSec = cachedGameScoreFilterSchoolScoreRankSec.getCachedResult();
		}

		String _cachedGameScoreFilterCompetitionScorePri = cachedGameScoreFilterCompetitionScorePri.getCachedResult();
		if (_cachedGameScoreFilterCompetitionScorePri == null) {
			cachedGameScoreFilterCompetitionScorePri.cacheStringResult(ep.getComplexProperty("gamescore.filter.competitionscorepri", " u.roles = 'student' and u.school_level = 1 and ((gs.finish_date like '2023-06-19%') or (gs.finish_date like '2023-06-20%')) " ));
			_cachedGameScoreFilterCompetitionScorePri = cachedGameScoreFilterCompetitionScorePri.getCachedResult();
		}

		String _cachedGameScoreFilterCompetitionScoreSec = cachedGameScoreFilterCompetitionScoreSec.getCachedResult();
		if (_cachedGameScoreFilterCompetitionScoreSec == null) {
			cachedGameScoreFilterCompetitionScoreSec.cacheStringResult(ep.getComplexProperty("gamescore.filter.competitionscoresec", " u.roles = 'student' and u.school_level = 2 and ((gs.finish_date like '2023-06-19%') or (gs.finish_date like '2023-06-20%')) " ));
			_cachedGameScoreFilterCompetitionScoreSec = cachedGameScoreFilterCompetitionScoreSec.getCachedResult();
		}

		if ("studentScoreRank".equals(method)) {
			return studentScoreRank(dbHelper, logger, responseMap, resultCacheForGameScoreFilterStudentScoreRank, _cachedGameScoreFilterStudentScoreRank);
		} else if ("schoolStudentNumRank".equals(method)) {
			return schoolStudentNumRank(dbHelper, logger, responseMap, resultCacheForGameScoreFilterSchoolStudentNumRank, _cachedGameScoreFilterSchoolStudentNumRank);
		} else if ("schoolScoreRank".equals(method)) {
			return schoolScoreRank(dbHelper, logger, responseMap, resultCacheForGameScoreFilterSchoolScoreRank, _cachedGameScoreFilterSchoolScoreRank);
		} else if ("schoolScoreRankPri".equals(method)) {
			return schoolScoreRank(dbHelper, logger, responseMap, resultCacheForGameScoreFilterSchoolScoreRankPri, _cachedGameScoreFilterSchoolScoreRankPri);
		} else if ("schoolScoreRankSec".equals(method)) {
			return schoolScoreRank(dbHelper, logger, responseMap, resultCacheForGameScoreFilterSchoolScoreRankSec, _cachedGameScoreFilterSchoolScoreRankSec);
		} else if ("competitionScorePri".equals(method)) {
			return competitionScore(dbHelper, logger, responseMap, resultCacheForGameScoreFilterCompetitionScorePri, _cachedGameScoreFilterCompetitionScorePri);
		} else if ("competitionScoreSec".equals(method)) {
			return competitionScore(dbHelper, logger, responseMap, resultCacheForGameScoreFilterCompetitionScoreSec, _cachedGameScoreFilterCompetitionScoreSec);
		}

		User user = (User)session.get("user");
		if(user == null)
		{
			return ERROR;
		}
		
		if("GET".equals(request.getMethod()))
		{
			if(gameId == null)
			{
				return ERROR;
			}
			
			Optional<Game> game = gameQ.getById(gameId);
			if(!game.isPresent())
			{
				return ERROR;
			}
			
			Optional<GameScore> gameScore = gameScoreQ.getByGameAndUser(game.get(), user);
			if(!gameScore.isPresent())
			{
				responseMap.put("score", null);
			}
			else
			{
				List<GameScore> _gameScore = gameScoreQ.listByGameAndUser(game.get(), user);
				ArrayList<GameResult> gameScoreArray = new ArrayList<GameResult>();
				for(GameScore gs : _gameScore){
					GameResult gr = new GameResult();
					gr.setFinsih_date(gs.getFinish_date());
					gr.setScore(gs.getScore());
					gameScoreArray.add(gr); 
				}
				
				responseMap.put("score", gameScoreArray);
//				responseMap.put("score", gameScore.get().getScore());
				
			}
			json = gson.toJson(responseMap);
			return SUCCESS;
		}
		else if("POST".equals(request.getMethod()))
		{
			if(gameId == null)
			{
				return ERROR;
			}
			
			if(score == null || score.intValue() < 0)
			{
				return ERROR;
			}
			
			Optional<Game> game = gameQ.getById(gameId);
			if(!game.isPresent())
			{
				return ERROR;
			}
			
			Optional<GameScore> gameScore = gameScoreQ.getByGameAndUser(game.get(), user);
//			if(!gameScore.isPresent())
//			{
			
			boolean isDailyQuizDone = false;
			if (gameId.equals(16)) {
				List<GameScore> dailyQuizGameScoreList = gameScoreQ.listByGameAndUser(game.get(), user);
				if (dailyQuizGameScoreList.size() > 0) {
					Calendar calendar;
					
					calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.add(Calendar.SECOND, -1);
					long yesterday = calendar.getTime().getTime();
					
					calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.add(Calendar.SECOND, 1);
					long tomorrow = calendar.getTime().getTime();
					
					GameScore dailyQuizGameScore = dailyQuizGameScoreList.get(0);
					long time = dailyQuizGameScore.getFinish_date().getTime();
					if(yesterday < time && time < tomorrow) {
						isDailyQuizDone = true;
					}
				}
			}
			
			if (!isDailyQuizDone) {
				GameScore sc = new GameScore();
				sc.setGame(game.get());
				sc.setScore(score.intValue());
				sc.setUser(user);
				sc.setFinish_date(new Date());
				gameScoreQ.add(sc);
			} else {
				responseMap.put("error message", "daily quiz is alredy done");
			}
//			}
//			else
//			{
//				
//				GameScore sc = gameScore.get();
//				sc.setScore(score.intValue());
//				gameScoreQ.update(sc);
//			}
			
			responseMap.put("result", 0);
			json = gson.toJson(responseMap);
			
			return SUCCESS;
		}
		return ERROR;
	}

	protected synchronized String studentScoreRank(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap,
												   StringResultCache resultCacheRef, String gameScoreFilter) throws Exception {

		String cachedResult = resultCacheRef.getCachedResult();
		if (cachedResult != null) {
			json = cachedResult;
			return SUCCESS;
		}

		String sqlStr = "select "
				+ "u.id as user_id, "
				+ "u.name_zh as user_name_zh, "
				+ "u.name_en as user_name_en, "
				+ "u.level as user_level, "
				+ "u.class_name as user_class_name, "
				+ "u.class_no as user_class_no, "
				+ "s.id as school_id, "
				+ "s.name_zh as school_name_zh, "
				+ "s.name_en as schoo_name_en, "
				+ "sum(gs.score) as total_score "
				+ "from user u "
				+ "inner join game_score gs on u.id = gs.user_id "
				+ "inner join school s on u.school_id = s.id "
				+ "where  "
				+ "  " + gameScoreFilter + "  "
				+ "group by u.id "
				+ "order by total_score desc, u.id ";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		json = gson.toJson(responseMap);
		resultCacheRef.cacheStringResult(json);
		return SUCCESS;
	}

	protected synchronized String schoolStudentNumRank(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap,
													   StringResultCache resultCacheRef, String gameScoreFilter) throws Exception {

		String cachedResult = resultCacheRef.getCachedResult();
		if (cachedResult != null) {
			json = cachedResult;
			return SUCCESS;
		}

		String sqlStr = "select "
				+ "s.id as school_id, "
				+ "s.name_zh as school_name_zh, "
				+ "s.name_en as school_name_en, "
				+ "count(*) as student_num "
				+ "from user u "
				+ "inner join school s on u.school_id = s.id "
				+ "where "
				+ "  " + gameScoreFilter + "  "
				+ "group by s.id "
				+ "order by student_num desc, s.id ";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		json = gson.toJson(responseMap);
		resultCacheRef.cacheStringResult(json);
		return SUCCESS;
	}
	
	protected synchronized String schoolScoreRank(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap,
												  StringResultCache resultCacheRef, String gameScoreFilter) throws Exception {

		String cachedResult = resultCacheRef.getCachedResult();
		if (cachedResult != null) {
			json = cachedResult;
			return SUCCESS;
		}

		String sqlStr = "select "
				+ "s.id as school_id, "
				+ "s.name_zh as school_name_zh, "
				+ "s.name_en as school_name_en, "
				+ "sum(gs.score) as total_score "
				+ "from school s "
				+ "inner join user u on u.school_id = s.id "
				+ "inner join game_score gs on gs.user_id = u.id "
				+ "where "
				+ "  " + gameScoreFilter + "  "
				+ "group by s.id "
				+ "order by total_score desc ";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		json = gson.toJson(responseMap);
		resultCacheRef.cacheStringResult(json);
		return SUCCESS;
	}

	protected synchronized String competitionScore(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap,
												   StringResultCache resultCacheRef, String gameScoreFilter) throws Exception {

		String cachedResult = resultCacheRef.getCachedResult();
		if (cachedResult != null) {
			json = cachedResult;
			return SUCCESS;
		}

		String sqlStr = "select "
				+ "s.id as school_id, "
				+ "s.name_zh as school_name_zh, "
				+ "s.name_en as school_name_en, "
				+ "sum(gs.score) as total_score "
				+ "from school s "
				+ "inner join user u on u.school_id = s.id "
				+ "inner join game_score gs on gs.user_id = u.id "
				+ "where "
				+ " " + gameScoreFilter + " "
				+ "group by s.id "
				+ "order by total_score desc ";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		resultCacheRef.cacheStringResult(json);
		return SUCCESS;
	}
}
