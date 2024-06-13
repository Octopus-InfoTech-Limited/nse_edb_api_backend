package com.octopus_tech.share.sso.edconnect;

import com.google.gson.annotations.SerializedName;

public class EDConnectErrorResponse
{
	public EDConnectErrorResponse()
	{
	}
	
	public EDConnectErrorResponse(String error, String errorDescription)
	{
		super();
		this.error = error;
		this.errorDescription = errorDescription;
	}

	@SerializedName("error")
	public String error;

	@SerializedName("error_description")
	public String errorDescription;
}
