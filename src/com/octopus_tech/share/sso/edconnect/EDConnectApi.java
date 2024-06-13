package com.octopus_tech.share.sso.edconnect;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface EDConnectApi 
{
	@POST("token.php")
	@FormUrlEncoded
	Call<RetrievingAccessTokenResponse> retriveingAccessToken(
			@Field("code") String code,
			@Field("client_id") String clientId,
			@Field("client_secret") String clientSecret,
			@Field("redirect_uri") String redirectUri,
			@Field("grant_type") String grantType
	);
	
	@GET("userinfo.php")
	Call<RetrievingUserInformationResponse> retrievingUserInformation(
			@Query("access_token") String accessToken
	);
	
	static EDConnectApi create(String url)
	{
		HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		LoggingInterceptor interceptor2 = new LoggingInterceptor();
		
		OkHttpClient client = new OkHttpClient.Builder()
				  .addInterceptor(interceptor2)
				  .build();
		
		Retrofit retrofit = new Retrofit.Builder()
			    .baseUrl(url)
			    .client(client)
			    .addConverterFactory(GsonConverterFactory.create())
			    .build();
		return retrofit.create(EDConnectApi.class);
	}
	
	static class LoggingInterceptor implements Interceptor
	{
		Logger logger;
		
		public LoggingInterceptor() {
			logger = LogManager.getLogger(this.getClass());
		}
		
		  @Override public Response intercept(Interceptor.Chain chain) throws IOException 
		  {
			LogManager.getLogger(this.getClass());
			  
			    Request request = chain.request();

			    long t1 = System.nanoTime();
			    logger.info(String.format("Sending request %s on %s%n%s",
			        request.url(), chain.connection(), request.headers()));
			 
			    if(request.body() != null)
			    {
				    Buffer buffer = new Buffer();
				    request.body().writeTo(buffer);
				    logger.info(buffer.readUtf8());
			    }

			    Response response = chain.proceed(request);

			    long t2 = System.nanoTime();
			    logger.info(String.format("Received response for %s in %.1fms%n%s",
			        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
			    logger.info(response.message());

			    return response;
			  }
			}
}
