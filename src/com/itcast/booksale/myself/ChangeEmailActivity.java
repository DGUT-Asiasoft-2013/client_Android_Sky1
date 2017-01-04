package com.itcast.booksale.myself;

import java.io.IOException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeEmailActivity extends Activity{

	TextView emailTitle;
	EditText emailhint;
	
	EditText change_email;
	
	Button btn_change;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_email);
		
		emailhint=(EditText) findViewById(R.id.change_edittext);
		emailTitle=(TextView) findViewById(R.id.change_title);
		
		change_email=(EditText) findViewById(R.id.change_edittext);
		
		btn_change=(Button) findViewById(R.id.btn_change);
		
		btn_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goChangeEmail();
			}
		});
	}
	
	protected void goChangeEmail() {
		String email=change_email.getText().toString();
		
		if(!isEmail(email)){
			Toast.makeText(ChangeEmailActivity.this,"邮箱地址输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("email", email)
				.build();
		
		Request request=Servelet.requestuildApi("change/email")
						.post(body)
						.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				byte[] ar=arg1.body().bytes();
				
				try{
					final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
					
					ChangeEmailActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(succeed){
								ChangeEmailActivity.this.onResponse(arg0,"成功");
								
							}else {
								ChangeEmailActivity.this.onFailure(arg0,new Exception("失败"));
							}
						}
					});
				}catch (final Exception e) {
					ChangeEmailActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							ChangeEmailActivity.this.onFailure(arg0,e);
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				Toast.makeText(ChangeEmailActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_SHORT)
				.show();
				
				ChangeEmailActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(ChangeEmailActivity.this)
						.setTitle("失败")
						.setMessage(arg1.getLocalizedMessage())
						.show();
					}
				});
			}
		});
	}
	
	void onFailure(Call arg0,Exception exception){
		Toast.makeText(ChangeEmailActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
		.show();
	}
	
	void onResponse(Call arg0,String string){
		Toast.makeText(ChangeEmailActivity.this, "成功修改邮箱", Toast.LENGTH_SHORT)
		.show();
		
		//finish掉上一个个人资料
		PersonalActivity.instance.finish();
		
		Intent intent =new Intent(ChangeEmailActivity.this, PersonalActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		emailhint.setHint("请输入新的邮箱");
		emailTitle.setText("邮箱");
	}
	
	//验证是否为邮箱
	 public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	 public static boolean isEmail(String email) {
	        return Pattern.matches(REGEX_EMAIL, email);
	    }
	 
}
