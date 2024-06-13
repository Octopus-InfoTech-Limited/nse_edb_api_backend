package com.octopus_tech.goc.action.api;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.auth0.jwt.JWT;
import com.octopus_tech.goc.Constants;
import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.annotation.AjaxParam;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.Argon2Util;
import com.octopus_tech.share.util.ObjectMap;
import com.octopus_tech.share.util.chrono.Epoch;

public class LoginAction extends BasicApiAction {

	private static final long serialVersionUID = -5357738377224486766L;

	private UserQ userQ;

	@AjaxParam(order = 1)
	public String loginId = "";
	@AjaxParam(order = 2)
	public String password = "";
	
	public String method;
	
	public Integer userId = 25;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if ("hashPwd".equals(method)) {
			return hashPwd(dbHelper, logger, responseMap);
		} else {
			return login(dbHelper, logger, responseMap);
		}
	}
	
	protected String login(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if(!request.getMethod().equals("POST")) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return SUCCESS;
		}
		
		if(loginId.length() == 0 || password.length() == 0) {
			responseMap.put("code", 1);
			return SUCCESS;
		}
		Optional<User> user = userQ.getByLoginId(loginId);
		if(!user.isPresent()) {
			responseMap.put("code", 2);
			return SUCCESS;
		}
		
		if(!Argon2Util.verify(user.get().getPassword(), password)) {
			responseMap.put("code", 3);
			return SUCCESS;
		}
		
		user.get().setLastLogin(Epoch.now());
		
		/*String jwt = JWT.create()
			.withIssuedAt(new Date())
			.withSubject("authentication")
			.withClaim("user", ObjectMap.objectToMap(user.get()))
			.sign(Constants.ALGORITHM);*/
			
		session.put("user", user.get());
		responseMap.put("user", user.get());
//		responseMap.put("authenticationToken", jwt);
		
		return SUCCESS;
	}
	
	protected String hashPwd(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		responseMap.put("user password", Argon2Util.generateHash(password));
		
		return SUCCESS;
	}
}