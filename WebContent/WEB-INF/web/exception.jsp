<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ss" uri="http://www.octopus-tech.com/share" %>
<%@ taglib prefix="goc" uri="http://www.octopus-tech.com/goc" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
</head>
<body>
	<h1>OH....發生錯誤</h1>
	<div>伺服器發生錯誤了，請通知有關方面發生錯誤</div>
	
	<h1>OH....Something went wrong</h1>
	<div>Server occurred unrecoverable error, please contact and inform this error to customer service.</div>
	
	<div>&nbsp;</div>
	<c:if test="${not empty _exceptionTrackId }">
		<div>錯誤ID: <span>${_exceptionTrackId }</span></div>
		<div>Error Track Code: <span>${_exceptionTrackId }</span></div>
	</c:if>
	
	<div>&nbsp;</div>
	<div>可行動作 Available actions</div>
	<div>
		<span><a href="<c:url value="/"/>">返回主頁 Goto homepage</a></span>
		&nbsp;|&nbsp;
		<span><a href="javascript:history.back()">返回上一頁 Go backward</a></span>
	</div>
	
</body>
</html>
