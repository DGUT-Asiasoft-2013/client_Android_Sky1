package com.itcast.booksale.myself;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.SettingPayActivity;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.pages.MyselfFragment;
import com.itcast.booksale.servelet.Servelet;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class RechargeActivity extends Activity {
	EditText editText_recharge;
	Button btn_recharge;

	Button recharge_10, recharge_20, recharge_30, recharge_50, recharge_100, recharge_200, recharge_300, recharge_500,
			recharge_1000;
	User user;

	// MyselfFragment fragUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);

		editText_recharge = (EditText) findViewById(R.id.edittext_recharge);
		btn_recharge = (Button) findViewById(R.id.btn_recharge);

		recharge_10 = (Button) findViewById(R.id.btn_recharge_10);
		recharge_20 = (Button) findViewById(R.id.btn_recharge_20);
		recharge_30 = (Button) findViewById(R.id.btn_recharge_30);
		recharge_50 = (Button) findViewById(R.id.btn_recharge_50);
		recharge_100 = (Button) findViewById(R.id.btn_recharge_100);
		recharge_200 = (Button) findViewById(R.id.btn_recharge_200);
		recharge_300 = (Button) findViewById(R.id.btn_recharge_300);
		recharge_500 = (Button) findViewById(R.id.btn_recharge_500);
		recharge_1000 = (Button) findViewById(R.id.btn_recharge_1000);

		recharge_10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("10.0");
			}
		});

		recharge_20.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("20.0");
			}
		});

		recharge_30.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("30.0");
			}
		});

		recharge_50.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("50.0");
			}
		});

		recharge_100.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("100.0");
			}
		});

		recharge_200.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("200.0");
			}
		});

		recharge_300.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("300.0");
			}
		});

		recharge_500.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("500.0");
			}
		});

		recharge_1000.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText_recharge.setText("1000.0");
			}
		});

		btn_recharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goPay();
				//goRecharge();
			}
		});
	}

	protected void goPay() {
		// TODO Auto-generated method stub
		Request request=Servelet.requestuildApi("me/if")
				.method("get", null)
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				// TODO Auto-generated method stub
				byte[] ar=arg1.body().bytes();
				
				try {
					final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
					
					RechargeActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(succeed){
								RechargeActivity.this.onResponsePay(arg0,user);
							}else{
								Intent intent=new Intent(RechargeActivity.this, SettingPayActivity.class);
								startActivity(intent);
							}
						}
					});
				} catch (final Exception e) {
					// TODO: handle exception
					RechargeActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							RechargeActivity.this.onFailurePay(arg0,e);
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				RechargeActivity.this.onFailurePay(arg0,arg1);
			}
		});
	}

	protected void onFailurePay(Call arg0, Exception arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(RechargeActivity.this, "网络错误", Toast.LENGTH_SHORT)
		.show();
	}

	protected void onResponsePay(Call arg0, User user2) {
		// TODO Auto-generated method stub
		goRecharge();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setIsNumber(true);

		Request request = Servelet.requestuildApi("me").method("get", null).build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					byte[] ar = arg1.body().bytes();
					user = new ObjectMapper().readValue(ar, User.class);
					RechargeActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							reload();
						}
					});
				} catch (final Exception e) {
					RechargeActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(RechargeActivity.this).setMessage(e.getMessage()).show();
						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				RechargeActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(RechargeActivity.this).setMessage(arg1.getMessage()).show();
					}
				});
			}
		});
	}

	public String reload() {
		String current_user = user.getAccount().toString();
		// Toast.makeText(RechargeActivity.this, current_user,
		// Toast.LENGTH_SHORT).show();
		return current_user;
	}

	public void setIsNumber(boolean isNumber) {// shezhiweishuzi
		if (isNumber) {
			editText_recharge.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_CLASS_NUMBER);
		} else {
			editText_recharge.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}
	}

	protected void goRecharge() {
		String money = editText_recharge.getText().toString();
		String current_user = reload().toString();
		// Toast.makeText(RechargeActivity.this, money,
		// Toast.LENGTH_SHORT).show();
		if (money.length() == 0) {
			Toast.makeText(RechargeActivity.this, "充值金额不能为0.00", Toast.LENGTH_SHORT).show();
			return;
		}

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				/* .addFormDataPart("currentUser", current_user) */
				.addFormDataPart("recharge", money);

		Request request = Servelet.requestuildApi("me/recharge").post(requestBodyBuilder.build()).build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				final String ar = arg1.body().toString();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							RechargeActivity.this.onReponse(arg0, ar);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(RechargeActivity.this).setTitle("失败").setMessage("网络错误！")
								.setNegativeButton("确定", null).show();

					}
				});
			}
		});
	}

	protected void onReponse(Call arg0, String ar) {
		Toast.makeText(RechargeActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(RechargeActivity.this, SumMoneyActivity.class);
		startActivity(intent);
		finish();
	}
}
