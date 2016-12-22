package com.itcast.booksale.myself;

import com.example.booksale.R;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.RegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置的Activity
 * @author Administrator
 *
 */
public class SettingActivity extends Activity{

	View s_login,s_register,s_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		s_login=findViewById(R.id.line_btn_login);//登录按钮
		s_register=findViewById(R.id.line_btn_register);//注册按钮
		s_back=findViewById(R.id.line_btn_back);//退出登录
		
		s_login.setOnClickListener(new View.OnClickListener() {//为登录按钮添加点击监听事件
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SettingActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		s_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(SettingActivity.this, RegisterActivity.class);
				startActivity(intent);		
			}
		});		
	}
}
