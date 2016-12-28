package com.itcast.booksale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/*
 * 首页，加了欢迎界面
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Handler handler=new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent=new Intent(MainActivity.this, HelloWorldActivity.class);
				startActivity(intent);
				finish();
			}
		}, 1000);
		
		
		/*//创建客户端连接
		OkHttpClient client=Servelet.getOkHttpClient();
		//创建请求
		Request request=Servelet.requestuildApi("hello")
				.method("get", null)
				.build();
		//客户端发起请求
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String string=arg1.body().string();            //此为后台实现，不能在主线程中实现
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();;
						
						//跳转到主界面
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
			}
		});*/
	}
}
