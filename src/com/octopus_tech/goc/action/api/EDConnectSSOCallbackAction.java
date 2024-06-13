package com.octopus_tech.goc.action.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.HttpParametersAware;

import com.octopus_tech.goc.crud.SchoolQ;
import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.School;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.sso.edconnect.EDConnectErrorResponse;
import com.octopus_tech.share.sso.edconnect.EDConnectException;
import com.octopus_tech.share.sso.edconnect.EDConnectSSO;
import com.octopus_tech.share.sso.edconnect.RetrievingAccessTokenResponse;
import com.octopus_tech.share.sso.edconnect.RetrievingUserInformationResponse;
import com.octopus_tech.share.util.EnhancedProperties;
import com.octopus_tech.share.util.PropertiesHelper;
import com.octopus_tech.share.util.chrono.Epoch;

public class EDConnectSSOCallbackAction extends DBHelperBasicAction
{
	private static final long serialVersionUID = 2575548055038197930L;
	
	public String code;
	public String state;
	public String error;
	public String errorDescription = "";
	
	private SchoolQ schoolQ;
	private UserQ userQ;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger) throws Exception 
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties(servletContext);
		EDConnectSSO sso = new EDConnectSSO(ep);
		
		if(error != null)
		{
			throw new EDConnectException(new EDConnectErrorResponse(error, errorDescription));
		}
		
		if(code == null)
		{
			return ERROR;
		}
		
		Optional<RetrievingAccessTokenResponse> accessToken = sso.retrievingAccessToken(code);
		if(!accessToken.isPresent())
		{
			return ERROR;
		}
		
		Optional<RetrievingUserInformationResponse> userInfo = sso.retrievingUserInformation(accessToken.get().accessToken);
		if(!userInfo.isPresent() || userInfo.get().profileId == null)
		{
			return ERROR;
		}
		RetrievingUserInformationResponse userInfo2 = userInfo.get();
		
		User user = null;
		
		Optional<User> dbUser = userQ.getByProfileId(userInfo.get().profileId.intValue());

		if(dbUser.isPresent())
		{
			user = dbUser.get();
			
			user.setLevel(userInfo2.level);
			user.setClassName(userInfo2.className);
			user.setClassNo(userInfo2.classNo);
			
			List<String> roleList = userInfo2.roles;
			roleList = roleList.stream()
					.map(role -> {
						if (role != null) {
							return role.toLowerCase().trim();
						}
						return role;
					})
					.filter(role -> role != null && role.length() > 0)
					.collect(Collectors.toList());
			user.setRoles(String.join(",", roleList));
			
			userQ.update(user);
			
//			School school = user.getSchool();
		}
		else
		{
			System.out.println("I: schoolcode=" + userInfo2.schoolcode);
			Optional<School> dbSchool;
			if (userInfo2.schoolcode != null) {
				dbSchool = schoolQ.getBySchoolCode(userInfo2.schoolcode);
				if(!dbSchool.isPresent())
				{
					try {
						School s = new School();
						s.setSchoolCode(userInfo2.schoolcode);
						s.setNameZh(userInfo2.cSchoolName);
						s.setNameEn(userInfo2.eSchoolName);
						schoolQ.add(s);
						dbSchool = Optional.of(s);
					} catch (Exception e) {
						System.out.println("E: Cant create school");
						/*
						School s = new School();
						s.setSchoolCode("nonschool");
						s.setNameZh("nonschool");
						s.setNameEn("nonschool");
						schoolQ.add(s);
						dbSchool = Optional.of(s);
						*/
					}
				}
			} else {
				String schoolCode = "noschool";
				dbSchool = schoolQ.getBySchoolCode(schoolCode);
				if(!dbSchool.isPresent()) {
					School s = new School();
					s.setSchoolCode(schoolCode);
					s.setNameZh(schoolCode);
					s.setNameEn(schoolCode);
					schoolQ.add(s);
					dbSchool = Optional.of(s);
				}
			}
			user = new User();
			user.setLoginId("!edconnect-" + userInfo2.id);
			user.setEdConnectProfileId(userInfo2.profileId);
			user.setPassword("SSO CONNECT");
			user.setNameZh(userInfo2.cname);
			user.setNameEn(userInfo2.ename);
			user.setAvatarIndex(1);
			user.setAvatarImageUrl("");
			user.setRegister(Epoch.now());
			user.setSchool(dbSchool.get());
			user.setAdminLevel(0);
			user.setSchoolLevel(0);
			
			user.setLevel(userInfo2.level);
			user.setClassName(userInfo2.className);
			user.setClassNo(userInfo2.classNo);
			
			List<String> roleList = userInfo2.roles;
			roleList = roleList.stream()
					.map(role -> {
						if (role != null) {
							return role.toLowerCase().trim();
						}
						return role;
					})
					.filter(role -> role != null && role.length() > 0)
					.collect(Collectors.toList());
			user.setRoles(String.join(",", roleList));
			
			if(userInfo2.level != null && userInfo2.level.length() > 0)
			{
				user.setSchoolLevel(userInfo2.level.charAt(0) == 'S'?2:1);
			}
			user.setDeleted(0);
		}
		
		user.setLastLogin(Epoch.now());
		dbHelper.saveOrUpdate(user);
		
		session.put("user", user);
		
		response.sendRedirect(applicationPath);
		return SUCCESS;
	}


}
