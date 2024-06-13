package com.octopus_tech.goc.action.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.AnnouncementQ;
import com.octopus_tech.goc.crud.NewsQ;
import com.octopus_tech.goc.model.Announcement;
import com.octopus_tech.goc.model.News;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.chrono.Epoch;
import com.octopus_tech.share.util.chrono.TimeVector;

public class AnnouncementAction extends BasicApiAction
{
	private static final long serialVersionUID = -6066645943714784470L;
	
	private AnnouncementQ announcementQ;
	
	public String _action = "";
	public Integer id;
	
	public String title;
	public String content;
	public Long start;
	public Long end;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception 
	{
		if("GET".equals(request.getMethod()))
		{
			if(id != null && id.intValue() > 0)
			{
				Optional<Announcement> a = announcementQ.getById(id.intValue());
				if(a.isPresent())
				{
					if(a.get().getDeleted() == 0)
					{
						responseMap.put("announcement", a.get());
					}
				}
			}
			else
			{
				List<Announcement> announcementList = announcementQ.listAnnouncementsBetween(Epoch.now(), Epoch.now());
				responseMap.put("announcements", announcementList);
			}
		}
		/*
		else if("POST".equals(request.getMethod()))
		{
			if(id == null)
			{
				User user = (User)session.get("user");
				if(user == null || user.getAdminLevel() < 2)
				{
					responseMap.put("code", 3);
					return;
				}
				
				if(title == null || title.length() == 0)
				{
					return;
				}
	
				if(content == null || content.length() == 0)
				{
					return;
				}
				
				if(start == null)
				{
					start = Epoch.now().getEpoch();
				}
				
				if(end == null)
				{
					end = new Epoch(start).add(new TimeVector(7, 0, 0, 0)).getEpoch();
				}
				
				if(end <= start)
				{
					responseMap.put("code", 2);
					return;
				}
				
				Announcement a = new Announcement();
				a.setTitle(title);
				a.setContent(content);
				a.setStart(new Epoch(start));
				a.setEnd(new Epoch(end));
				a.setLastmod(Epoch.now());
				announcementQ.add(a);
	
				responseMap.put("code", 0);
				responseMap.put("announcement", a);
			}
			else
			{
				User user = (User)session.get("user");
				if(user == null || user.getAdminLevel() < 2)
				{
					responseMap.put("code", 3);
					return;
				}
				
				if(id <= 0)
				{
					return;
				}
				Optional<Announcement> announcement = announcementQ.getById(id);
				if(!announcement.isPresent())
				{
					return;
				}

				Announcement a = announcement.get();
				if(title != null && title.length() != 0)
				{
					a.setTitle(title);
				}
				if(content != null && content.length() != 0)
				{
					a.setContent(content);
				}
				if((end != null && start != null) && end <= start)
				{
					responseMap.put("code", 2);
					return;
				}

				if(start != null)
				{
					a.setStart(new Epoch(start));
				}
				if(end != null)
				{
					a.setEnd(new Epoch(end));
				}
				if(a.getStart().equalAndAfter(a.getEnd()))
				{
					responseMap.put("code", 2);
					return;
				}
				
				a.setLastmod(Epoch.now());
				announcementQ.update(a);
				
				responseMap.put("code", 0);
				responseMap.put("announcement", a);
			}
		}
		else if("DELETE".equals(request.getMethod()))
		{
			User user = (User)session.get("user");
			if(user == null || user.getAdminLevel() < 2)
			{
				responseMap.put("code", 2);
				return;
			}
			
			if(id <= 0)
			{
				return;
			}
			Optional<Announcement> a = announcementQ.getById(id);
			if(!a.isPresent())
			{
				return;
			}
			a.get().setDeleted(1);
			announcementQ.update(a.get());
			
			responseMap.put("code", 0);
		}
		*/
		else
		{
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		return SUCCESS;
	}
}