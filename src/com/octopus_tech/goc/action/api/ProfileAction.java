package com.octopus_tech.goc.action.api;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;

public class ProfileAction extends BasicApiAction
{
	private static final long serialVersionUID = -8524472730419850308L;
	
	private UserQ userQ;
	
	public Integer avatarIndex;
	public String avatarImageUrl;

	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception 
	{
		if("POST".equals(request.getMethod()))
		{
			if(!isAuthenticated())
			{
				return ERROR;
			}
			
			User user = (User)session.get("user");
			if(avatarIndex != null && avatarIndex.intValue() >= 0)
			{
				user.setAvatarIndex(avatarIndex.intValue());
			}
			if(avatarImageUrl != null && avatarImageUrl.trim().length() > 0)
			{
				user.setAvatarImageUrl(avatarImageUrl);
			}
			userQ.update(user);
			
			responseMap.put("user", user);
		}
		else if("GET".equals(request.getMethod()))
		{
			User user = (User)session.get("user");
			if(user == null)
			{
				return ERROR;
			}
			responseMap.put("user", user);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		return SUCCESS;
	}

}
