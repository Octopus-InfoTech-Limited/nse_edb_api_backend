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
	<div>通常發生錯誤不是很嚴重的問題。但當你看見此頁時時，代表發生了以下其中一種情況。</div>
	<ul>
		<li>你所輸入的網址已經失效了或不存在</li>
		<li>你所輸入的資料觸發了安全系統保護</li>
	</ul>
	
	<h1>OH....Something went wrong</h1>
	<div>Here is the reasons that why you see this</div>
	<ul>
		<li>The link is incorrect or no longer exist</li>
		<li>Security system stop the website execution</li>
	</ul>
	
	
	<div>&nbsp;</div>
	<div>可行動作 Available actions</div>
	<div>
		<span><a href="<c:url value="/"/>">返回主頁 Goto homepage</a></span>
		&nbsp;|&nbsp;
		<span><a href="javascript:history.back()">返回上一頁 Go backward</a></span>
	</div>
	
</body>
</html>
