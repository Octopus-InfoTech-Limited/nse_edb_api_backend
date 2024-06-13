package com.octopus_tech.share.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HTMLUtf8ify implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException 
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletResponse response2 = (HttpServletResponse)response;
		//response2.setContentType("text/html;charset=UTF-8");
		response2.setCharacterEncoding("UTF-8");
		chain.doFilter(request, new WeirdoHttpServletResponse(response2));
	}
	
	@Override
	public void destroy() 
	{
	}

	private static class WeirdoHttpServletResponse implements HttpServletResponse
	{
		HttpServletResponse response;

		public WeirdoHttpServletResponse(HttpServletResponse response) {
			super();
			this.response = response;
		}

		public void addCookie(Cookie cookie) {
			response.addCookie(cookie);
		}

		public boolean containsHeader(String name) {
			return response.containsHeader(name);
		}

		public String encodeURL(String url) {
			return response.encodeURL(url);
		}

		public String getCharacterEncoding() {
			return response.getCharacterEncoding();
		}

		public String encodeRedirectURL(String url) {
			return response.encodeRedirectURL(url);
		}

		public String getContentType() {
			return response.getContentType();
		}

		public String encodeUrl(String url) {
			return response.encodeUrl(url);
		}

		public String encodeRedirectUrl(String url) {
			return response.encodeRedirectUrl(url);
		}

		public void sendError(int sc, String msg) throws IOException {
			response.sendError(sc, msg);
		}

		public ServletOutputStream getOutputStream() throws IOException {
			return response.getOutputStream();
		}

		public PrintWriter getWriter() throws IOException {
			return response.getWriter();
		}

		public void sendError(int sc) throws IOException {
			response.sendError(sc);
		}

		public void setCharacterEncoding(String charset) {
			response.setCharacterEncoding(charset);
		}

		public void sendRedirect(String location) throws IOException {
			response.sendRedirect(location);
		}

		public void setDateHeader(String name, long date) {
			response.setDateHeader(name, date);
		}

		public void setContentLength(int len) {
			response.setContentLength(len);
		}

		public void setContentLengthLong(long len) {
			response.setContentLengthLong(len);
		}

		public void addDateHeader(String name, long date) {
			response.addDateHeader(name, date);
		}

		public void setContentType(String type) 
		{
			response.setContentType(type);
		}

		public void setHeader(String name, String value) {
			response.setHeader(name, value);
		}

		public void addHeader(String name, String value) {
			response.addHeader(name, value);
		}

		public void setBufferSize(int size) {
			response.setBufferSize(size);
		}

		public void setIntHeader(String name, int value) {
			response.setIntHeader(name, value);
		}

		public void addIntHeader(String name, int value) {
			response.addIntHeader(name, value);
		}

		public void setStatus(int sc) {
			response.setStatus(sc);
		}

		public int getBufferSize() {
			return response.getBufferSize();
		}

		public void flushBuffer() throws IOException {
			response.flushBuffer();
		}

		public void setStatus(int sc, String sm) {
			response.setStatus(sc, sm);
		}

		public void resetBuffer() {
			response.resetBuffer();
		}

		public int getStatus() {
			return response.getStatus();
		}

		public boolean isCommitted() {
			return response.isCommitted();
		}

		public String getHeader(String name) {
			return response.getHeader(name);
		}

		public void reset() {
			response.reset();
		}

		public Collection<String> getHeaders(String name) {
			return response.getHeaders(name);
		}

		public void setLocale(Locale loc) {
			response.setLocale(loc);
		}

		public Collection<String> getHeaderNames() {
			return response.getHeaderNames();
		}

		public void setTrailerFields(Supplier<Map<String, String>> supplier) {
			response.setTrailerFields(supplier);
		}

		public Locale getLocale() {
			return response.getLocale();
		}

		public Supplier<Map<String, String>> getTrailerFields() {
			return response.getTrailerFields();
		}
		
		
	}
}
