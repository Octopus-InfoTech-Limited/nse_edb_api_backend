package com.octopus_tech.goc.action.web;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class EDConnectSSOCallbackRedirectAction extends ActionSupport implements ServletResponseAware, ServletRequestAware
{
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String state;
	public String code;
	
	@Override
	public String execute() throws Exception 
	{
		if(state == null||code == null)
		{
			return ERROR;
		}
		
		String applicationPath = "/" + ((HttpServletRequest) request).getContextPath()
				.replace("/", "");
		if(!"/".equals(applicationPath))
		{
			applicationPath += "/";
		}

		String state2 = URLEncoder.encode(state);
		String code2 = URLEncoder.encode(code);
		
		response.sendRedirect(applicationPath + "api/sso?state=" + state2 + "&code=" + code2);
		return NONE;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) 
	{
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) 
	{
		this.request = request;
	}

}
