package api;

import java.net.CookiePolicy;


import java.net.CookieManager;


import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class Server {
	//生成静态的客户端
	static OkHttpClient client;

	//设置cookie
	static{
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

		client = new OkHttpClient.Builder()
				.cookieJar(new JavaNetCookieJar(cookieManager))
				.build();

	}

	//创建让其他类调用客户端的方法
	public static OkHttpClient getSharedClient(){
		return client;
	}

	//-------------------
	public static String serverAddress = "http://172.27.0.14:8080/membercenter/";

	//创建调用服务器的api方法，例如 api=hello
	public static Request.Builder requestBuilderWithApi(String api){
		return new Request.Builder()
				//.url("http://172.27.0.14:8080/membercenter/api/"+api);
				.url(serverAddress+"api/"+api);
	}

}
