package com.octopus_tech.goc.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.octopus_tech.share.db.CRUDHelper;
import com.octopus_tech.share.db.ICRUDRepo;

public class QueryUserTaglib extends SimpleTagSupport
{
	private String repo;
	private String method;
	
	@Override
	public void doTag() throws JspException, IOException 
	{
		PageContext pageContext = (PageContext) this.getJspContext();
		
		JspTag parent = SimpleTagSupport.findAncestorWithClass(this, DBHelperTagLib.class);
		if(parent == null)
		{
			throw new JspException("query tag not in db tag");
		}
		
		DBHelperTagLib tag = (DBHelperTagLib)parent;
		
		ICRUDRepo<?> crud;
		try
		{
			crud = (ICRUDRepo<?>) CRUDHelper.createCRUD(tag.getDbHelper(), Class.forName(repo));
		}
		catch(Exception e)
		{
			throw new JspException("cannot create repo " + repo, e);
		}
		
		
	}
}
