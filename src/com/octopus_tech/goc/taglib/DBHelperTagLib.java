package com.octopus_tech.goc.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.octopus_tech.share.db.DBHelper;

public class DBHelperTagLib extends SimpleTagSupport 
{
	private DBHelper dbHelper = null;
	
	@Override
	public void doTag() throws JspException, IOException 
	{
		try
		{
			dbHelper = new DBHelper();
			getJspBody().invoke(null);
		}
		finally
		{
			dbHelper.close();
			dbHelper = null;
		}
	}

	public DBHelper getDbHelper()
	{
		return dbHelper;
	}

}
