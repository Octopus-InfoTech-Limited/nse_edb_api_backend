package com.octopus_tech.share.sso.edconnect;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RetrievingUserInformationResponse extends EDConnectErrorResponse
{
	public String id;

	@SerializedName("profile_id")
	public Integer profileId;

	@SerializedName("cname")
	public String cname;

	@SerializedName("ename")
	public String ename;
	
	@SerializedName("last_update")
	public String lastUpdate;
	
	@SerializedName("roles")
	public List<String> roles;
	
	@SerializedName("schoolcode")
	public String schoolcode;

	@SerializedName("level")
	public String level;

	@SerializedName("class_name")
	public String className;

	@SerializedName("class_no")
	public Integer classNo;

	@SerializedName("c_school_name")
	public String cSchoolName;

	@SerializedName("e_school_name")
	public String eSchoolName;
}
