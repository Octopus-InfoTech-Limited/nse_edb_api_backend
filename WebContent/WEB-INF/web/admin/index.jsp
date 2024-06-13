<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ss" uri="http://www.octopus-tech.com/share" %>
<%@ taglib prefix="goc" uri="http://www.octopus-tech.com/goc" %>
<goc:setUser />
<c:if test="${empty user }">
<% response.sendRedirect("../login.jsp"); %>
</c:if>
<c:if test="${not empty user }">
<!doctype html>
<html lang="zh_HK">
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/admin/css/common.css"/>"/>
	<script type="text/javascript" src="<c:url value="js/jquery.min.js"/>"></script>
</head>
<body>
	<div id="root">
		<div class="left-drawer">
			<%@ include file="left_drawer.jsp" %>
		</div>
	
		<div class="right-content">
			<h2></h2>
			
			<div class="content">
				<c:if test="${empty content_page}">
				<-- Please select an item
				</c:if>
				<c:if test="${not empty content_page}">
					<c:set property="in_index" value="1" />
					<%@ include file="content-/${content_page}" %>
				</c:if>
			</div>
		</div>
	</div>
	
	<script>
		'use strict';
		$(function()
		{
			if(typeof contentPage == 'function')
			{
				contentPage();
			}
		});
	</script>
</body>
</html>
</c:if>
