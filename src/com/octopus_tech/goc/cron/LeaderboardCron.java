package com.octopus_tech.goc.cron;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import com.octopus_tech.goc.crud.LeaderBoardQ;
import com.octopus_tech.goc.model.Leaderboard;
import com.octopus_tech.share.cron.CronClass;
import com.octopus_tech.share.cron.CronTask;
import com.octopus_tech.share.db.CRUDHelper;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.chrono.Epoch;

/*
@CronClass(second = 0)
@CronClass(second = 10)
@CronClass(second = 20)
@CronClass(second = 30)
@CronClass(second = 40)
@CronClass(second = 50)
*/
/*
@CronClass(hour = 4)
public class LeaderboardCron implements CronTask 
{
	private LeaderBoardQ leaderboardQ;
	private GameLogQ gameLogQ;
	
	@Override
	public void run(DBHelper dbHelper, PrintWriter logger) throws Exception 
	{
		CRUDHelper.autowireCRUD(dbHelper, this);
		
		logger.println("LeaderboardCron is being to run");
		
		List<GameLog> gameLogs = gameLogQ.listAll();
		
		List<Leaderboard> personLbs = buildPersonLeaderboard(dbHelper, logger, gameLogs);
		List<Leaderboard> schoolLbs = buildSchoolLeaderboard(dbHelper, logger, gameLogs);

		dbHelper.customHibernateAction((h, t) ->
		{
			int id = 1;
			
			Epoch now = Epoch.now();
			long timestamp = now.getEpoch();
			
			h.createNativeQuery("DELETE FROM leaderboard").executeUpdate();
			Query query = h.createNativeQuery("INSERT INTO leaderboard VALUES(:id, :type, :score, :timestamp, :game_id, :school_id, :user_id)");
			for(Leaderboard lb: personLbs)
			{
				query.setParameter("id", id++);
				query.setParameter("type", Leaderboard.TYPE_PERSONAL_SCORE);
				query.setParameter("score", lb.getScore());
				query.setParameter("timestamp", timestamp);
				query.setParameter("game_id", lb.getGame().getId());
				query.setParameter("school_id", lb.getSchool().getId());
				query.setParameter("user_id", lb.getUser().getId());
				query.executeUpdate();
			}
			for(Leaderboard lb: schoolLbs)
			{
				query.setParameter("id", id++);
				query.setParameter("type", Leaderboard.TYPE_SCHOOL_SCORE);
				query.setParameter("score", lb.getScore());
				query.setParameter("timestamp", timestamp);
				query.setParameter("game_id", lb.getGame().getId());
				query.setParameter("school_id", lb.getSchool().getId());
				query.setParameter("user_id", null);
				query.executeUpdate();
			}
		});
		
		logger.println("LeaderboardCron finish");
	}

	private List<Leaderboard> buildPersonLeaderboard(DBHelper dbHelper, PrintWriter logger, List<GameLog> gameLogs)
	{
		Map<Integer, Map<Integer, Leaderboard>> map = new HashMap<>();
		
		for(GameLog gl: gameLogs)
		{
			Map<Integer, Leaderboard> userlb = map.getOrDefault(gl.getUser().getId(), new HashMap<>());
			Leaderboard lb = userlb.getOrDefault(gl.getGame().getId(), new Leaderboard());
			
			lb.setScore(lb.getScore() + gl.getScore());
			lb.setGame(gl.getGame());
			lb.setSchool(gl.getUser().getSchool());
			lb.setUser(gl.getUser());
			
			userlb.put(gl.getGame().getId(), lb);
			map.put(gl.getUser().getId(), userlb);
		}
		
		
		return map.values()
				.stream()
				.flatMap(t->t.values().stream())
				.collect(Collectors.toList());
	}

	private List<Leaderboard> buildSchoolLeaderboard(DBHelper dbHelper, PrintWriter logger, List<GameLog> gameLogs)
	{
		Map<Integer, Map<Integer, Leaderboard>> map = new HashMap<>();
		
		for(GameLog gl: gameLogs)
		{
			Map<Integer, Leaderboard> userlb = map.getOrDefault(gl.getUser().getSchool().getId(), new HashMap<>());
			Leaderboard lb = userlb.getOrDefault(gl.getGame().getId(), new Leaderboard());
			
			lb.setScore(lb.getScore() + gl.getScore());
			lb.setGame(gl.getGame());
			lb.setSchool(gl.getUser().getSchool());
			
			userlb.put(gl.getGame().getId(), lb);
			map.put(gl.getUser().getSchool().getId(), userlb);
		}
		
		
		return map.values()
				.stream()
				.flatMap(t->t.values().stream())
				.collect(Collectors.toList());
	}
}
*/
