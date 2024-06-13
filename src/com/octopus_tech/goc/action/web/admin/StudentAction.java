package com.octopus_tech.goc.action.web.admin;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.UserQ;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.util.Argon2Util;

import de.mkammerer.argon2.Argon2;

public class StudentAction extends DBHelperBasicAction 
{
	private static final long serialVersionUID = -3720082089916614718L;
	
	private UserQ userQ;
	
	public String _mode;

	public Integer id;
	public Integer page = 0;
	public String loginId;
	public String password;
	public String nameZh;
	public String nameEn;
	
	public List<User> users;
	public User user;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger) throws Exception 
	{
		if(_mode == null)
		{
			return ERROR;
		}
		
		User _user = (User)session.get("user");
		if(_user == null || _user.getAdminLevel() <= 1)
		{
			return ERROR;
		}
		
		if("add".equals(_mode))
		{
			return SUCCESS;
		}
		else if("update".equals(_mode))
		{
			if("GET".equals(request.getMethod()))
			{
				if(id == null || id.intValue() <= 0)
				{
					return ERROR;
				}
				
				Optional<User> user = userQ.getById(id.intValue());
				if(!user.isPresent())
				{
					return ERROR;
				}
				this.user = user.get();
				return SUCCESS;
			}
			else if("POST".equals(request.getMethod()))
			{
				if(id == null || id.intValue() <= 0)
				{
					return ERROR;
				}
				
				Optional<User> user = userQ.getById(id.intValue());
				if(user.isPresent())
				{
					if(user.get().getAdminLevel() != 0)
					{
						return ERROR;
					}
					if(_user.getAdminLevel() < 2 && !user.get().getSchool().equals(_user.getSchool()))
					{
						return ERROR;
					}
					
					if(password != null && password.trim().length() >= 6)
					{
						user.get().setPassword(Argon2Util.generateHash(password));
					}
					if(nameZh != null && nameZh.trim().length() > 0)
					{
						user.get().setNameZh(nameZh.trim());
					}
					if(nameEn != null && nameEn.trim().length() > 0)
					{
						user.get().setNameEn(nameEn.trim());
					}
					userQ.update(user.get());
					return SUCCESS;
				}
				return ERROR;
			}
			else
			{
				return ERROR;
			}
		}
		else if("list".equals(_mode))
		{
			if(_user.getAdminLevel() == 2)
			{
				users = userQ.listAllStudentOfAllSchools(page.intValue());
			}
			else
			{
				users = userQ.listAllStudentInSchool(_user.getSchool(), page.intValue());
			}
			return SUCCESS;
		}
		else if("delete".equals(_mode))
		{
			if(id == null || id.intValue() <= 0)
			{
				return ERROR;
			}
			
			Optional<User> user2 = userQ.getById(id.intValue());
			if(user2.isPresent())
			{
				if(user2.get().getAdminLevel() != 0)
				{
					return ERROR;
				}
				if(_user.getAdminLevel() < 2 && !user2.get().getSchool().equals(_user.getSchool()))
				{
					return ERROR;
				}
				
				user2.get().setDeleted(1);
				userQ.update(user2.get());
			}
			
			
			return SUCCESS;
		}
		
		return ERROR;
	}

}
