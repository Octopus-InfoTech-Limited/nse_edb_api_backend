package com.octopus_tech.goc.action.api.removed;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.GameQ;
import com.octopus_tech.goc.crud.LeaderBoardQ;
import com.octopus_tech.goc.crud.SchoolQ;
import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.Leaderboard;
import com.octopus_tech.goc.model.School;
import com.octopus_tech.share.db.DBHelper;

/*
public class LeaderBoardAction extends BasicApiAction
{
	private static final long serialVersionUID = -5808982796700067292L;
	
	public SchoolQ schoolQ;
	public LeaderBoardQ leaderBoardQ;
	public GameQ gameQ;

	public Integer school_id;
	public Integer page = 0;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception 
	{
		if(!isAuthenticated())
		{
			return ERROR;
		}
		
		if(!request.getMethod().equals("GET"))
		{
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return SUCCESS;
		}
		
		if(school_id == null)
		{
			Map<Integer, Leaderboard> map = new HashMap<>();
			
			List<Leaderboard> leaderboards = leaderBoardQ.listSchoolGameScoreInAllGames();
			for(Leaderboard lb: leaderboards)
			{
				Leaderboard mapLb = map.getOrDefault(lb.getSchool().getId(), new Leaderboard());
				mapLb.setId(0);
				mapLb.setType(Leaderboard.TYPE_SCHOOL_SCORE);
				mapLb.setScore(mapLb.getScore() + lb.getScore());
				mapLb.setTimestamp(lb.getTimestamp());
				mapLb.setGame(null);
				mapLb.setSchool(lb.getSchool());
				mapLb.setUser(null);
				map.put(lb.getSchool().getId(), mapLb);
			}
			
			leaderboards = map.values()
					.stream()
					.sorted(Comparator.comparing(Leaderboard::getScore))
					.collect(Collectors.toList());
			responseMap.put("leaderboards", leaderboards);
		}
		else
		{
			Optional<School> school = schoolQ.getById(school_id.intValue());
			if(!school.isPresent())
			{
				return;
			}
			
			Map<Integer, Leaderboard> map = new HashMap<>();
			
			List<Leaderboard> leaderboards = leaderBoardQ.listPersonGameScoreWithInSchool(school.get());
			for(Leaderboard lb: leaderboards)
			{
				Leaderboard mapLb = map.getOrDefault(lb.getUser().getId(), new Leaderboard());
				mapLb.setId(0);
				mapLb.setType(Leaderboard.TYPE_PERSONAL_SCORE);
				mapLb.setScore(mapLb.getScore() + lb.getScore());
				mapLb.setTimestamp(lb.getTimestamp());
				mapLb.setGame(null);
				mapLb.setSchool(lb.getSchool());
				mapLb.setUser(lb.getUser());
				map.put(lb.getUser().getId(), mapLb);
			}
			
			leaderboards = map.values()
					.stream()
					.sorted(Comparator.comparing(Leaderboard::getScore))
					.collect(Collectors.toList());
			responseMap.put("leaderboards", leaderboards);
		}
	}
}
*/