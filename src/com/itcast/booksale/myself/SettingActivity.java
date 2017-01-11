package com.itcast.booksale.myself;

import java.io.IOException;

import com.itcast.booksale.HelloWorldActivity;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.PasswordRecoverActivity;
import com.itcast.booksale.R;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.RegisterFirstActivity;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ���õ�Activity
 * @author Administrator
 *
 */
public class SettingActivity extends Activity{
	//public static Activity instance;
	
	View s_login,s_register,s_back,s_password_recover;
	
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		s_login=findViewById(R.id.line_btn_login);//��¼��ť
		s_register=findViewById(R.id.line_btn_register);//ע�ᰴť
		s_back=findViewById(R.id.line_btn_back);//�˳���¼
		s_password_recover=findViewById(R.id.line_btn_password_recover);//�޸�����
		
		s_login.setOnClickListener(new View.OnClickListener() {//Ϊ��¼��ť��ӵ�������¼�
			
			@Override
			public void onClick(View v) {
				LoginActivity.b=false;
				Intent intent=new Intent(SettingActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		s_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(SettingActivity.this, RegisterFirstActivity.class);
				startActivity(intent);		
			}
		});	
		
		s_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
						goback();
			}
		});
		
		s_password_recover.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goPasswordRecover();
			}
		});
	}

	protected void goPasswordRecover() {//�޸�����
		Intent intent =new Intent(SettingActivity.this, PasswordRecoverActivity.class);
		startActivity(intent);
	}

	protected void goback() {//�Ƴ���¼�Լ�ȥ��session		
		Request request=Servelet.requestuildApi("exit")
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(SettingActivity.this, R.string.set_exit, Toast.LENGTH_SHORT).show();

						Intent intent=new Intent(SettingActivity.this, HelloWorldActivity.class);
						startActivity(intent);
						finish();
					}
				});	
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(SettingActivity.this, R.string.set_exit_fail, Toast.LENGTH_SHORT).show();

					}
				});
			}
		});
	}
}
