package com.octopus_tech.share.sso.edconnect;

public class EDConnectException extends RuntimeException
{
	private static final long serialVersionUID = 5023850855514791090L;

	private EDConnectErrorResponse error;
	
	public EDConnectException(EDConnectErrorResponse error)
	{
		super(error.errorDescription);
		this.error = error;
	}

	public String getError() {
		return error.error;
	}

	public String getErrorDescription() {
		return error.errorDescription;
	}
	
	public EDConnectErrorResponse getResponse()
	{
		return error;
	}
}
