package com.octopus_tech.goc.action.api.removed;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.action.api.BasicApiAction;
import com.octopus_tech.goc.crud.GameQ;
import com.octopus_tech.goc.model.Game;
import com.octopus_tech.share.db.DBHelper;

/*
public class GameAction extends BasicApiAction
{
	private static final long serialVersionUID = -4833292694979993045L;
	
	public GameQ gameQ;

	@Override
	protected void execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception
	{
		if(!isAuthenticated())
		{
			responseUnauthorised();
			return;
		}
		
		if("GET".equals(request.getMethod()))
		{
			List<Game> games = gameQ.listAll();
			responseMap.put("games", games);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}
}
*/
