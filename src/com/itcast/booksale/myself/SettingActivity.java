package com.itcast.booksale.myself;

import com.itcast.booksale.R;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.RegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * ���õ�Activity
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
		
		s_login=findViewById(R.id.line_btn_login);//��¼��ť
		s_register=findViewById(R.id.line_btn_register);//ע�ᰴť
		s_back=findViewById(R.id.line_btn_back);//�˳���¼
		
		s_login.setOnClickListener(new View.OnClickListener() {//Ϊ��¼��ť��ӵ�������¼�
			
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
