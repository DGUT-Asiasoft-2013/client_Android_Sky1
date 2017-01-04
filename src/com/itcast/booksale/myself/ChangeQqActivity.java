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

public class ChangeQqActivity extends Activity{

	TextView qqTitle;
	EditText qqhint;
	
	EditText change_qq;
	
	Button btn_change;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_qq);
		
		qqTitle=(TextView) findViewById(R.id.change_title);
		qqhint=(EditText) findViewById(R.id.change_edittext);
	
		change_qq=(EditText) findViewById(R.id.change_edittext);
		
		btn_change=(Button) findViewById(R.id.btn_change);
		
		btn_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goChangeQq();
			}
		});
	
	}
	
	protected void goChangeQq() {
		String qq=change_qq.getText().toString();
		
		if(!isQQ(qq)){
			Toast.makeText(ChangeQqActivity.this,"QQ号码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("qq", qq)
				.build();
		
		Request request=Servelet.requestuildApi("change/qq")
						.post(body)
						.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				byte[] ar=arg1.body().bytes();
				
				try {
					final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
					
					ChangeQqActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(succeed){
								ChangeQqActivity.this.onResponse(arg0,"成功");
							}else {
								ChangeQqActivity.this.onFailure(arg0,new Exception("失败"));
							}
						}
					});
				} catch (final Exception e) {
					ChangeQqActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							ChangeQqActivity.this.onFailure(arg0,e);
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				Toast.makeText(ChangeQqActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_SHORT)
				.show();
				
				ChangeQqActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(ChangeQqActivity.this)
						.setTitle("失败")
						.setMessage(arg1.getLocalizedMessage())
						.show();
					}
				});
			}
		});
	}
	
	void onFailure(Call arg0,Exception exception){
		Toast.makeText(ChangeQqActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
		.show();
	}
	
	void onResponse(Call arg0,String string){
		Toast.makeText(ChangeQqActivity.this, "成功修改QQ号码", Toast.LENGTH_SHORT)
		.show();
		
		//finish掉上一个个人资料
		PersonalActivity.instance.finish();
		
		Intent intent=new Intent(ChangeQqActivity.this, PersonalActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		qqhint.setHint("请输入新的QQ");
		qqTitle.setText("QQ");
	}
	
	//验证是否为QQ
	 public static final String REGEX_QQ = "^[1-9][0-9]{3,11}";
	 public static boolean isQQ(String QQ) {
	        return Pattern.matches(REGEX_QQ, QQ);
	    }
}
