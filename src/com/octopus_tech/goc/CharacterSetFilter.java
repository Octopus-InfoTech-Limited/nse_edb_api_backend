package com.octopus_tech.goc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterSetFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
        // response.setContentType("text/html; charset=UTF-8");
        // response.setCharacterEncoding("UTF-8");
        next.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		
	}
	@Override
	public void destroy() {
		
	}
	
    // ...
}