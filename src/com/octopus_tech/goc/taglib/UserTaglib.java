package com.octopus_tech.goc.taglib;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.octopus_tech.goc.model.User;

public class UserTaglib extends SimpleTagSupport
{
	@Override
	public void doTag() throws JspException, IOException
	{
		ServletContext servletContext = ((PageContext) this.getJspContext()).getServletContext();
		HttpSession session = ((PageContext)this.getJspContext()).getSession();
		
		JspWriter out = this.getJspContext().getOut();
		
		User user = (User)session.getAttribute("user");
		if(user != null)
		{
			getJspBody().invoke(out);
		}
	}
}
