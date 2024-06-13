package com.octopus_tech.goc.action.web.admin;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.AnnouncementQ;
import com.octopus_tech.goc.model.Announcement;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.chrono.Epoch;

public class AnnouncementAction extends DBHelperBasicAction
{
	private static final long serialVersionUID = 5911327119464210838L;

	private AnnouncementQ announcementQ;
	
	public String _mode;
	
	public Integer id;
	public String title;
	public String content;
	public Long start;
	public Long end;
	
	public List<Announcement> announcements;
	public Announcement announcement;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger) throws Exception
	{
		if(_mode == null)
		{
			return ERROR;
		}
		
		User user = (User)session.get("user");
		if(user.getAdminLevel() < 2)
		{
			return ERROR;
		}
		
		if("add".equals(_mode))
		{
			if("GET".equals(request.getMethod()))
			{
				return SUCCESS;
			}
			else if("POST".equals(request.getMethod()))
			{
				if(title == null || title.trim().length() == 0)
				{
					return ERROR;
				}
				
				if(content == null)
				{
					return ERROR;
				}
				
				Announcement a = new Announcement();
				a.setTitle(title.trim());
				a.setContent(content);
				a.setStart(start==null?Epoch.now():new Epoch(start.longValue()));
				Epoch endEpoch;
				if(end != null)
				{
					endEpoch = new Epoch(end.longValue());
				}
				else
				{
					endEpoch = Epoch.now().add(86400 * 30);
				}
				
				if(endEpoch.equalAndAfter(a.getStart()))
				{
					return ERROR;
				}
				a.setEnd(endEpoch);
				a.setLastmod(Epoch.now());
				a.setDeleted(0);
				announcementQ.add(a);
				
				return SUCCESS;
			}
			else
			{
				return ERROR;
			}
		}
		else if("edit".equals("_mode"))
		{
			if(id == null || id.intValue() <= 0)
			{
				return ERROR;
			}
			
			Optional<Announcement> announcement = announcementQ.getById(id.intValue());
			if(announcement.isPresent())
			{
				this.announcement = announcement.get();
				return SUCCESS;
			}
			else
			{
				return ERROR;
			}
		}
		else if("list".equals("_mode"))
		{
			announcements = announcementQ.listAll();
			return SUCCESS;
		}
		else if("delete".equals("_mode"))
		{
			if(id == null || id.intValue() <= 0)
			{
				return ERROR;
			}
			
			Optional<Announcement> announcement = announcementQ.getById(id.intValue());
			if(announcement.isPresent())
			{
				announcement.get().setDeleted(1);
				announcement.get().setLastmod(Epoch.now());
				announcementQ.update(announcement.get());
				return SUCCESS;
			}
			else
			{
				return ERROR;
			}
		}
		return SUCCESS;
	}

}
