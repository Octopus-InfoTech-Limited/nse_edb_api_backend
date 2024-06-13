package com.octopus_tech.share.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class StructFilter extends org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{
		super.doFilter(req, res, chain);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
	}
}
