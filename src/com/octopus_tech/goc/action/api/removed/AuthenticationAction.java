package com.octopus_tech.goc.action.api.removed;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.auth0.jwt.JWT;
import com.octopus_tech.goc.Constants;
import com.octopus_tech.goc.action.api.BasicApiAction;
import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.annotation.AjaxParam;
import com.octopus_tech.share.annotation.AjaxSupport;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.Argon2Util;
import com.octopus_tech.share.util.ObjectMap;
import com.octopus_tech.share.util.chrono.Epoch;

/*
@AjaxSupport(
		method = "POST",
		url = "/api/authentication"
)
public class AuthenticationAction extends BasicApiAction 
{
	private static final long serialVersionUID = -1986972468081037374L;

	private UserQ userQ;
	
	public String _mode;
	
	@AjaxParam(order = 1)
	public String loginId = "";
	@AjaxParam(order = 2)
	public String password = "";
	
	public Integer avatarIndex;
	
	@Override
	protected void execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception
	{
		if("authentication".equals(_mode))
		{
			if(!request.getMethod().equals("POST"))
			{
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				return;
			}
			
			if(loginId.length() == 0 || password.length() == 0)
			{
				responseMap.put("code", 1);
				return;
			}
			Optional<User> user = userQ.getByLoginId(loginId);
			if(!user.isPresent())
			{
				responseMap.put("code", 2);
				return;
			}
			
			if(!Argon2Util.verify(user.get().getPassword(), password))
			{
				responseMap.put("code", 3);
				return;
			}
			
			if(user.get().getAvatarIndex() == -1)
			{
				if(avatarIndex == null || avatarIndex.intValue() < 0)
				{
					responseMap.put("code", 4);
					return;
				}
				
				user.get().setAvatarIndex(avatarIndex.intValue());
			}
			
			user.get().setLastLogin(Epoch.now());
			
			String jwt = JWT.create()
				.withIssuedAt(new Date())
				.withSubject("authentication")
				.withClaim("user", ObjectMap.objectToMap(user.get()))
				.sign(Constants.ALGORITHM);
				
			session.put("user", user.get());
			responseMap.put("user", user.get());
			responseMap.put("authenticationToken", jwt);
		}
		else if("logout".equals(_mode))
		{
			if(!request.getMethod().equals("POST"))
			{
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				return;
			}
			session.clear();
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
}
*/