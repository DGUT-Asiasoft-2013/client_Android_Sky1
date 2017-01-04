package com.itcast.booksale.myself;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
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

public class ChangePhoneActivity extends Activity{
TextView phonetitle;
EditText phonehint;


EditText change_phone;

Button btn_change;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_phone);
		
		phonehint=(EditText) findViewById(R.id.change_edittext);
		phonetitle=(TextView) findViewById(R.id.change_title);
	
		change_phone=(EditText) findViewById(R.id.change_edittext);
		
		btn_change=(Button) findViewById(R.id.btn_change);
		
		btn_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goChangePhone();
			}
		});
}

protected void goChangePhone() {
	String phone=change_phone.getText().toString();
	
	if(!isMobileNO(phone)){
		Toast.makeText(ChangePhoneActivity.this,"手机号码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
		return;
	}
	MultipartBody body=new MultipartBody.Builder()
			.addFormDataPart("phone", phone)
			.build();
	
	Request request=Servelet.requestuildApi("change/phone")
			.post(body)
			.build();
	
	Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
		
		@Override
		public void onResponse(final Call arg0, Response arg1) throws IOException {
			// TODO Auto-generated method stub
			byte[] ar=arg1.body().bytes();
			
			try {
				final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
				
				ChangePhoneActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(succeed){
							ChangePhoneActivity.this.onResponse(arg0,"成功");
						}else {
							ChangePhoneActivity.this.onFailure(arg0,new Exception("失败"));
						}
						
					}
				});
			} catch (final Exception e) {
				ChangePhoneActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ChangePhoneActivity.this.onFailure(arg0,e);
						
					}
				});
			}
		}
		
		@Override
		public void onFailure(Call arg0, final IOException arg1) {
			Toast.makeText(ChangePhoneActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_SHORT)
			.show();
			
			ChangePhoneActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					new AlertDialog.Builder(ChangePhoneActivity.this)
					.setTitle("失败")
					.setMessage(arg1.getLocalizedMessage())
					.show();
				}
			});
		}
	});
}

void onFailure(Call arg0,Exception exception){
	Toast.makeText(ChangePhoneActivity.this , exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
	.show();
}

void onResponse(Call arg0,String string){
	Toast.makeText(ChangePhoneActivity.this, "成功修改电话号码", Toast.LENGTH_SHORT)
	.show();
	
	//finish掉上一个个人资料
	PersonalActivity.instance.finish();
	
	Intent intent=new Intent(ChangePhoneActivity.this, PersonalActivity.class);
	startActivity(intent);
	finish();
}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		phonehint.setHint("请输入电话");
		phonetitle.setText("电话");
	}

//验证是否为手机号码
public static boolean isMobileNO(String mobiles){//判断是否为真的手机号码
	Pattern p=Pattern.compile("^((13[0-9])|(14[5|7])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	Matcher m=p.matcher(mobiles);
	return m.matches();
}
}
