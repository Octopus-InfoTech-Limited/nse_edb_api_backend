<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ss" uri="http://www.octopus-tech.com/share" %>
<%@ taglib prefix="goc" uri="http://www.octopus-tech.com/goc" %>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="com.octopus_tech.goc.model.User" %>
<%@ page import="com.google.gson.Gson" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
</head>
<body>
	<div>
		<%
		if(session.getAttribute("user") != null)
		{
			User user = (User)session.getAttribute("user");
			
			Gson gson = new Gson();
			String aaa = gson.toJson(user);
			out.println(aaa);
		}
		%>
	</div>

	<a href="<c:url value="/api/authentication" />">EDConnect sign on</a>
	<a href="<c:url value="/api/signout" />">EDConnect sign out</a>
</body>
</html>
