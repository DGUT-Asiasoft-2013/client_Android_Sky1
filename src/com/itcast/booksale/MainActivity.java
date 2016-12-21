package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

		//创建客户端连接
		OkHttpClient client=Servelet.getOkHttpClient();
		//创建请求
		Request request=Servelet.requestuildApi("hello")
				.method("get", null)
				.build();
		
		
		//客户端发起请求
		client.newCall(request)
		.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String string=arg1.body().string();            //此为后台实现，不能在主线程中实现
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("登录成功")
						.setMessage(string)
						.setPositiveButton("确定", null)
						.show();
						
						//跳转到主界面
						Intent intent = new Intent(MainActivity.this, HelloWorldActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				Toast.makeText(MainActivity.this, "对不起你没有连接上", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	
}
