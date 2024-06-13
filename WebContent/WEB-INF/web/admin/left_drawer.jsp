<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="http://www.octopus-tech.com/share" %>
<%@ taglib prefix="goc" uri="http://www.octopus-tech.com/goc" %>

<goc:setUser />

<c:if test="${not empty user and user.adminLevel >= 2 }">
<div class="category">Announcement</div>
<a href="<c:url value="/admin/announcement/list"/>"><div class="item">List</div></a>
<a href="<c:url value="/admin/announcement/add"/>"><div class="item">Add</div></a>


<div class="category">Leaderboard</div>
<a href="<c:url value="/admin/leaderboard/update?force=1"/>"><div class="item">Force update</div></a>
<a href="<c:url value="/admin/leaderboard/update?empty=1"/>"><div class="item">Clear all</div></a>


<div class="category">Crons</div>
<a href="<c:url value="/admin/cron"/>"><div class="item">List</div></a>
<a href="<c:url value="/admin/cron/error"/>"><div class="item">Logs</div></a>
</c:if>

<c:if test="${not empty user and user.adminLevel >= 1 }">
<div class="category">News</div>
<a href="<c:url value="/admin/news/list"/>"><div class="item">List</div></a>
<a href="<c:url value="/admin/news/add"/>"><div class="item">Add</div></a>

<div class="category">Student</div>
<a href="<c:url value="/admin/student/list"/>"><div class="item">List</div></a>
<a href="<c:url value="/admin/student/add"/>"><div class="item">Add</a></div></a>
</c:if>

<c:if test="${not empty user and user.adminLevel >= 0 }">
<div class="category">&nbsp;</div>
<div class="item"><a href="<c:url value="/admin/logout"/>">Logout</a></div>
</c:if>