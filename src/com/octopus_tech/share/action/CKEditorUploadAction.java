package com.octopus_tech.share.action;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.util.ServletContextAware;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/*
@ParentPackage("struts-default")
@Namespace("ckeditor")
@Action(value = "upload")
@Results({
	@Result(name=ActionSupport.SUCCESS, location = "/WEB-INF/share/json.jsp"),
	@Result(name=ActionSupport.ERROR, location = "/WEB-INF/share/error.jsp"),
	@Result(name="exception", location = "/WEB-INF/share/exception.jsp")
})
@ExceptionMappings({
	@ExceptionMapping(exception = "java.lang.Exception", result = "exception")
})
*/
public class CKEditorUploadAction extends ActionSupport  implements ServletContextAware
{
	private static final long serialVersionUID = 1299960033122863603L;
	
	public File upload;
	public String uploadFileName;
	public String uploadContentType;
	
	public String json;

	private ServletContext servletContext;
	
	@Override
	public String execute() throws Exception
	{
		if(!CKEditorBrowseAction.validate2())
		{
			return ERROR;
		}
		
		if(upload == null || uploadFileName == null || uploadFileName.length() == 0 || uploadContentType == null)
		{
			return ERROR;
		}
		
		File dir = new File(servletContext.getRealPath("/uploads"));
		dir.mkdirs();
		
		String filename = String.format("%s.%s", 
			UUID.randomUUID().toString(),
			FilenameUtils.getExtension(uploadFileName)
		);
		File dest = new File(dir, filename);
		FileUtils.moveFile(upload, dest);
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("uploaded", 1);
		json.put("fileName", filename);
		json.put("url", "/uploads/" + filename);
		
		this.json = new Gson().toJson(json);
		return "SUCCESS";
	}

	@Override
	public void setServletContext(ServletContext context) 
	{
		this.servletContext = context;
	}
}
