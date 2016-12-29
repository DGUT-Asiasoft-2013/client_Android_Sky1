package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.PasswordRecoverStep1Fragment;
import com.itcast.booksale.fragment.widgets.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.itcast.booksale.fragment.widgets.PasswordRecoverStep2Fragment;
import com.itcast.booksale.fragment.widgets.PasswordRecoverStep2Fragment.OnSubmitClickedListener;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverActivity extends Activity {
	PasswordRecoverStep1Fragment step1Fragment=new PasswordRecoverStep1Fragment();//动态添加fragment忘记密码的第一步
	PasswordRecoverStep2Fragment step2Fragment=new PasswordRecoverStep2Fragment();//动态添加fragment忘记密码的第二步

	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recover);//加载布局
		
		step1Fragment.setOnGoNextListener(new OnGoNextListener() {//设置第一步点击监听事件

			@Override
			public void onGoNext() {
				goStep2();
			}

		});
		
		step2Fragment.setOnSubmitClickedListener(new OnSubmitClickedListener() {//设置第二步点击监听事件
			
			@Override
			public void onSubmitClicked() {
				goSubmit();
				
			}
		}); 
		
		getFragmentManager().beginTransaction().replace(R.id.container, step1Fragment).commit();//��step1Fragment�滻��ǰ��container����
	}

	protected void goStep2() {//定义第二步的方法
		/*getFragmentManager().beginTransaction() // 通过FragmentManager来取得FragmentTransaction的实例
		.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left,
				R.animator.slide_out_right)
		.replace(R.id.container, step2Fragment).addToBackStack(null)// 用一个fragment代替另一个fragment
		.commit();// 提交
*/		
		String fragPhone=step1Fragment.getText();
		
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("phone", fragPhone)
				.build();
		
		Request request=Servelet.requestuildApi("phone")
				.post(body)
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				//String ar=arg1.body().string();
				byte[] ar=arg1.body().bytes();
				try{
					final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
					
					PasswordRecoverActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(succeed){
								Toast.makeText(PasswordRecoverActivity.this, "验证码已发送，请注意查收信息", Toast.LENGTH_SHORT)
								.show();
								PasswordRecoverActivity.this.Step2OnReponse(arg0,arg1);
							}else {
								PasswordRecoverActivity.this.onFailure(arg0,new Exception("此手机未注册过"));
							}
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(PasswordRecoverActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
							
						}
					});
					return;
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(PasswordRecoverActivity.this)
						.setTitle("请求失败")
						.setMessage(arg1.getLocalizedMessage())
						.show();
						
					}
				});
				
			}
		});
	}
	
	protected void Step2OnReponse(Call arg0, Response arg1) {
		// setCustomAnimations()方法必须在add、remove、replace调用之前被设置，否则不起作用
		getFragmentManager().beginTransaction() // 通过FragmentManager来取得FragmentTransaction的实例
		.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left,
				R.animator.slide_out_right)
		.replace(R.id.container, step2Fragment).addToBackStack(null)// 用一个fragment代替另一个fragment
		.commit();// 提交
	}

	protected void goSubmit() {
		String fragPhone=step1Fragment.getText();//获得邮箱
		String fragPassword=step2Fragment.getText();//获得密码
		
		//生成请求体
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("phone", fragPhone)//添加邮箱
				.addFormDataPart("passwordHash", fragPassword)//添加密码
				.build();	
		
		//创建一个Request，获取url
		Request request=Servelet.requestuildApi("passwordrecover")
				.post(body)
				.build();//向服务器请求打开URL
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {//唤起
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					final Boolean succeed=new ObjectMapper().readValue(arg1.body().bytes(), Boolean.class);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(succeed){
								//Toast.makeText(PasswordRecoverActivity.this, "chengg",Toast.LENGTH_SHORT).show();
								PasswordRecoverActivity.this.onResponse(arg0, "succeed is true");
							}else{
								//Toast.makeText(PasswordRecoverActivity.this, "shibai",Toast.LENGTH_SHORT).show();
								PasswordRecoverActivity.this.onFailure(arg0, new Exception("此邮箱尚未注册！"));
							}
							
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							PasswordRecoverActivity.this.onFailure(arg0, e);
							
						}
					});
				}		
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {//服务器连接失败
				Toast.makeText(PasswordRecoverActivity.this,arg1.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(PasswordRecoverActivity.this)
										.setTitle("请求失败")
										.setMessage(arg1.getLocalizedMessage()).show();
						
					}
				});
			}
		});
	}

	protected void onFailure(Call arg0, Exception exception) {
		Toast.makeText(PasswordRecoverActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		
	}

	protected void onResponse(Call arg0, String string) {
		Toast.makeText(PasswordRecoverActivity.this, "成功", Toast.LENGTH_SHORT).show();
		
		Intent intent=new Intent(PasswordRecoverActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
		
	}

	
}

