package com.octopus_tech.share.sso.edconnect;

import com.google.gson.annotations.SerializedName;

public class RetrievingAccessTokenResponse extends EDConnectErrorResponse
{
	@SerializedName(value = "access_token")
	public String accessToken;
	
	@SerializedName(value = "refresh_token")
	public String refreshToken;
	
	@SerializedName(value = "expires_in")
	public String expiresIn;
	
	@SerializedName(value = "scope")
	public String scope;
}
