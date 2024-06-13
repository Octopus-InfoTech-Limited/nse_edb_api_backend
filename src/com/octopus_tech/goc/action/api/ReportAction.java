package com.octopus_tech.goc.action.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import com.octopus_tech.goc.model.Game;
import com.octopus_tech.goc.model.GameResult;
import com.octopus_tech.goc.model.GameScore;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;

public class ReportAction extends BasicApiAction  {
	private static final long serialVersionUID = 3809489765092686149L;
	
	public String json;
	public String method;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception
	{
		User user = (User)session.get("user");
		if(user == null)
		{
			return ERROR;
		}
		
		if ("userListCount".equals(method)) {
			return userListCount(dbHelper, logger, responseMap);
		} else if ("schoolListCount".equals(method)) {
			return schoolListCount(dbHelper, logger, responseMap);
		}
		return ERROR;
	}
	
	protected String userListCount(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		String sqlStr = "select count(*) from user";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		
		json = gson.toJson(responseMap);
		
		return SUCCESS;
	}
	
	protected String schoolListCount(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		String sqlStr = "SELECT school_code,name_zh, name_en FROM nse.school where name_zh != \"\" and name_zh != \"noschool\" and name_zh != \"nonschool\" and name_zh not like \"%TESTING%\" and name_zh not like \"%測試%\" and name_zh not like \"%八達%\" and name_zh not like \"%教育局%\" ";
		
		dbHelper.beginTransactionIfNeeded();
		Query query = dbHelper.getHibernateSession().createNativeQuery(sqlStr);
		responseMap.put("list", query.getResultList());
		
		json = gson.toJson(responseMap);
		
		return SUCCESS;
	}
}
