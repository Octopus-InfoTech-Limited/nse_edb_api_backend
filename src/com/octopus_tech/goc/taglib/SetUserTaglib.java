package com.octopus_tech.goc.taglib;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.octopus_tech.goc.model.User;

public class SetUserTaglib extends SimpleTagSupport
{
	@Override
	public void doTag() throws JspException, IOException
	{
		PageContext pageContext = (PageContext) this.getJspContext();
		HttpSession session = pageContext.getSession();
		
		User user = (User)session.getAttribute("user");
		if(user != null)
		{
			pageContext.setAttribute("user", user);
		}
	}
}
