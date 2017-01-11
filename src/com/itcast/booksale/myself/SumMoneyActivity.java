package com.itcast.booksale.myself;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class SumMoneyActivity extends Activity{
View view_recharge;
TextView sumMoney;

View view_list;
User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sum_money);
		
		view_recharge=findViewById(R.id.line_btn_recharge);
		sumMoney=(TextView) findViewById(R.id.sum_money);
		view_list=findViewById(R.id.line_btn_recharge_list);
		
		view_recharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SumMoneyActivity.this, RechargeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		view_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SumMoneyActivity.this, RechargeListActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			Request request=Servelet.requestuildApi("me")
					.method("get", null)
					.build();
			
			Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(final Call arg0, final Response arg1) throws IOException {
					byte[] ar=arg1.body().bytes();
					try {
						user=new ObjectMapper().readValue(ar, User.class);
						
						SumMoneyActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								reload();
							}
						});
					} catch (final Exception e) {
						SumMoneyActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(SumMoneyActivity.this)
								.setMessage(e.getMessage())
								.show();
							}
						});
					}
				}
				
				@Override
				public void onFailure(final Call arg0, final IOException arg1) {
					SumMoneyActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(SumMoneyActivity.this)
							.setMessage(arg1.getMessage())
							.show();
						}
					});
				}
			});
			
			
		}
	
	public void reload(){
		if(TextUtils.isEmpty(user.getAccount())){
			Toast.makeText(SumMoneyActivity.this, R.string.sum_nologin, Toast.LENGTH_SHORT)
			.show();
		}
	    sumMoney.setVisibility(View.VISIBLE);
	    sumMoney.setTextColor(Color.BLACK);;
		sumMoney.setText(String.valueOf(user.getSumMoney()));
		
		
	}
	

}
