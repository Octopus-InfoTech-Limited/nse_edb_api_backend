package com.octopus_tech.share.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSOptionFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		if(request instanceof HttpServletRequest)
		{
			HttpServletRequest request2 = (HttpServletRequest)request;
			if("OPTIONS".equals(request2.getMethod()))
			{
				HttpServletResponse response2 = (HttpServletResponse)response;
				response2.setStatus(HttpServletResponse.SC_OK);
				response2.addHeader("Allow", "OPTIONS, GET, HEAD, POST, PUT, DELETE");
				response2.addHeader("Access-Control-Request-Method", "GET, HEAD, POST, PUT, DELETE");
				response2.addHeader("Access-Control-Request-Headers", "X-PINGOTHER, Content-Type");
				response2.addHeader("Access-Control-Allow-Origin", "*");
				response2.addHeader("Access-Control-Max-Age", "86400");
				response2.setContentLength(0);
				response2.flushBuffer();
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy()
	{
	}
}
