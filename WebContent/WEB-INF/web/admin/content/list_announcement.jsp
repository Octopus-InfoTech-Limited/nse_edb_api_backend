<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ss" uri="http://www.octopus-tech.com/share" %>
<%@ taglib prefix="goc" uri="http://www.octopus-tech.com/goc" %>
<c:if test="${not empty in_index}">
	<div class="panel">
		<button class="remove-selected btn btn-outline-danger">Delete</button>
		<button class="modify-selected btn btn-outline-secondary">Modify</button>
		<button class="previous-page btn btn-outline-secondary">&lt; Previous</button>
		<button class="next-page btn btn-outline-secondary">Next %gt;></button>
	</div>
	
	<table class="table">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<th>ID</th>
				<th>Title</th>
				<th>Post date</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${announcements }" var="a">
				<tr>
					<td><input type="checkbox" class="announcement a-cb a-${a.id}" data-id="${a.id}" /></td>
					<td>${a.id }</td>
					<td>${a.title}</td>
					<td>${a.release }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panel">
		<button class="remove-selected btn btn-outline-danger">Delete</button>
		<button class="modify-selected btn btn-outline-secondary">Modify</button>
		<button class="previous-page btn btn-outline-secondary">&lt; Previous</button>
		<button class="next-page btn btn-outline-secondary">Next %gt;></button>
	</div>
	
	<script>
		'use strict';
		
		const g_currentPage = ${page};
		
		function contentPage()
		{
			bindElements();
		}
		
		function bindElements()
		{
			$('.remove-selected').click(removeSelected);
			$('.modify-selected').click(modifySelected);
			$('.previous-page').click(previousPage);
			$('.next-page').click(nextPage);
		}

		function removeSelected($e)
		{
			let cbs = $('.a-cb:checked');
			if(cbs.length == 0)
			{
				alert('No announcements are selected');
				return;
			}
			
			let result = confirm('Are you sure wanted to delete selected announcements?');
			if(result)
			{
				
			}
		}
		
		function modifySelected($e)
		{
			let cbs = $('.a-cb:checked');
			if(cbs.length == 0)
			{
				alert('No announcement is selected');
				return;
			}
			if(cbs.length > 1)
			{
				alert('Too many announcements are selected');
				return;
			}
			
			let id = cbs.data('id');
			
			location.href = `<c:url value="/admin/announcement/update/" />\${id}`;
		}

		function previousPage($e)
		{
			if(g_currentPage == 0)
			{
				return;
			}
			
			location.href = `<c:url value="announcement/list/" />\${g_currentPage - 1}`;
		}

		function nextPage($e)
		{
			location.href = `<c:url value="announcement/list/" />\${g_currentPage + 1}`;
		}
	</script>
	
	<style>
		
	</style>
</c:if>