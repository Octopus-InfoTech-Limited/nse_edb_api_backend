package com.octopus_tech.share.sso.edconnect;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.octopus_tech.share.sso.SSO;
import com.octopus_tech.share.util.EnhancedProperties;

import retrofit2.Call;
import retrofit2.Response;

public class EDConnectSSO implements SSO
{
	public static final int CHANNEL_PRODUCTION = 1;
	public static final int CHANNEL_DEVELOPMENT = 2;

	private static final String DEVELOPMENT_URL = "https://developers.hkedcity.net/edconnect/oauth2/";
	private static final String PRODUCTION_URL = "https://edconnect.hkedcity.net/authen/";
	
	private int channel;
	private String clientId;
	private String redirectUri;
	private String clientSecret;
	private EDConnectApi api;
	
	public EDConnectSSO(EnhancedProperties ep)
	{
		this.channel = ep.getNumber("sso.edconnect.channel", 0).intValue();
		clientId = ep.getComplexProperty("sso.edconnect.client_id", "");
		clientSecret = ep.getComplexProperty("sso.edconnect.client_secret", "");
		redirectUri = ep.getComplexProperty("sso.edconnect.redirect_uri", "");
		
		if(!(channel == CHANNEL_PRODUCTION || channel == CHANNEL_DEVELOPMENT))
		{
			throw new IllegalArgumentException();
		}
		
		api = EDConnectApi.create(channel == CHANNEL_PRODUCTION?PRODUCTION_URL:DEVELOPMENT_URL);
	}
	
	public String getLoginUrl()
	{
		return getLoginUrl(null);
	}
	
	public String getLogoutUrl(String finishUrl)
	{
		try
		{
			if(channel == CHANNEL_PRODUCTION)
			{
				return "https://edconnect.hkedcity.net/authen/logout.php?finish_uri=" + URLEncoder.encode(finishUrl, StandardCharsets.UTF_8.name());
			}
			return "https://developers.hkedcity.net/edconnect/oauth2/logout.php?finish_uri=" + URLEncoder.encode(finishUrl, StandardCharsets.UTF_8.name());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	public String getLoginUrl(String state)
	{
		String ret = getLoginUrl2(state);
		ret = ret.replace("{clientId}", clientId);
		ret = ret.replace("{redirectUri}", redirectUri);
		return ret;
	}
	
	private String getLoginUrl2(String state)
	{
		String ret = getLoginUrl3() + "?response_type=code&client_id={clientId}&redirect_uri={redirectUri}";
		if(state != null)
		{
			ret += "&state=" + state;
		}
		return ret;
	}
	
	
	private String getLoginUrl3()
	{
		if(channel == CHANNEL_DEVELOPMENT)
		{
			return "https://developers.hkedcity.net/edconnect/oauth2/authorize.php";
		}
		return "https://edconnect.hkedcity.net/authen/authorize.php";
	}

	public Optional<RetrievingAccessTokenResponse> retrievingAccessToken(String code) throws EDConnectException
	{
		try
		{
			Response<RetrievingAccessTokenResponse> response = api.retriveingAccessToken(code, clientId, clientSecret, redirectUri, "authorization_code").execute();
			if(!response.isSuccessful())
			{
				return Optional.empty();
			}
			
			RetrievingAccessTokenResponse response2 = response.body();
			if(response2.error != null)
			{
				throw new EDConnectException(response2);
			}
			return Optional.ofNullable(response2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return Optional.empty();
	}

	public Optional<RetrievingUserInformationResponse> retrievingUserInformation(String accessToken) throws EDConnectException
	{
		try
		{
			Response<RetrievingUserInformationResponse> response = api.retrievingUserInformation(accessToken).execute();
			if(!response.isSuccessful())
			{
				return Optional.empty();
			}
			
			RetrievingUserInformationResponse response2 = response.body();
			if(response2.error != null)
			{
				throw new EDConnectException(response2);
			}
			return Optional.ofNullable(response2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return Optional.empty();
	}

	public int getChannel() {
		return channel;
	}
	
	
}
