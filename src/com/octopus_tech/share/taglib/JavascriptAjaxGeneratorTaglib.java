package com.octopus_tech.share.taglib;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octopus_tech.share.util.JavascriptAjaxGenerator;

public class JavascriptAjaxGeneratorTaglib extends SimpleTagSupport
{
	private Logger logger = LogManager.getLogger(JavascriptAjaxGeneratorTaglib.class);
	private String clazz;

	@Override
	public void doTag() throws JspException, IOException
	{
		ServletContext servletContext = ((PageContext) this.getJspContext()).getServletContext();
		
		JspWriter out = this.getJspContext().getOut();
		try
		{
			Class<?> clazz = Class.forName(this.clazz);
			out.print(JavascriptAjaxGenerator.generateJavascriptFunction(clazz, servletContext.getContextPath().replace("/", "")));
		}
		catch(Exception e)
		{
			logger.catching(e);
		}
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	
}
