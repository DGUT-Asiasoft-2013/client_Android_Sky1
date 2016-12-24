package com.itcast.booksale.servelet;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Servelet {

	static OkHttpClient client;
	
	static
	{
		CookieManager cookieManager=new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		client=new OkHttpClient.Builder()
				.cookieJar(new JavaNetCookieJar(cookieManager))
				.build();
	}
	
	/*
	 * 返回client
	 */
	public static OkHttpClient getOkHttpClient() {
		
		return client;

	}
	public static String urlstring="http://172.27.15.2:8080/membercenter/";


	//建立请求
	public static Request.Builder requestuildApi(String api)
	{
		return new Request.Builder().url(urlstring+"api/"+api);
		
	}
}
