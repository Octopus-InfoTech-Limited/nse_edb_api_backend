package com.octopus_tech.goc.action.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import com.octopus_tech.goc.crud.GameQ;
import com.octopus_tech.goc.crud.GameScoreQ;
import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.GameResult;
import com.octopus_tech.goc.model.GameScore;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;

public class DailyAction extends BasicApiAction
{
	private static final long serialVersionUID = 2847612099506934533L;

	private GameQ gameQ;
	private GameScoreQ gameScoreQ;
	
	public String method;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if (!isAuthenticated()) {
			return ERROR;
		}
		
		if ("login".equals(method)) {
			return dailyLogin(dbHelper, logger, responseMap);
		} else {
			return ERROR;
		}
	}

	protected String dailyLogin(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		User user = (User) session.get("user");
		
		responseMap.put("code",  1);
		responseMap.put("login",  1);
		responseMap.put("quiz",  1);
		
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
		
		Optional<Game> dailyLoginGame = gameQ.getById(15);
		GameScore dailyLoginGameScore = null;
		List<GameScore> dailyLoginGameScoreList = gameScoreQ.listByGameAndUser(dailyLoginGame.get(), user);
		if (dailyLoginGameScoreList.size() > 0) {
			dailyLoginGameScore = dailyLoginGameScoreList.get(0);
			long time = dailyLoginGameScore.getFinish_date().getTime();
			if(!(yesterday < time && time < tomorrow)) {
				dailyLoginGameScore = null;
			}
		}
		
		if (dailyLoginGameScore == null) {
			dailyLoginGameScore = new GameScore();
			dailyLoginGameScore.setUser(user);
			dailyLoginGameScore.setGame(dailyLoginGame.get());
			dailyLoginGameScore.setFinish_date(new Date());
			dailyLoginGameScore.setScore(10);
			gameScoreQ.add(dailyLoginGameScore);
			
			responseMap.put("login", 0);
		} else {
			responseMap.put("login", 0);

			Optional<Game> dailyQuizGame = gameQ.getById(16);
			List<GameScore> dailyQuizGameScoreList = gameScoreQ.listByGameAndUser(dailyQuizGame.get(), user);
			if (dailyQuizGameScoreList.size() > 0) {
				GameScore dailyQuizGameScore = dailyQuizGameScoreList.get(0);
				long time = dailyQuizGameScore.getFinish_date().getTime();
				if(yesterday < time && time < tomorrow) {
					responseMap.put("quiz", 0);
				}
			}
		}
		
		responseMap.put("code", 0);
		
		return SUCCESS;
	}
}
