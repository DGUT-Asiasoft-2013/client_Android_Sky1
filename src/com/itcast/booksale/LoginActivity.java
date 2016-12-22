package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.example.booksale.R.layout;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录操作
 * @author Administrator
 *
 */
public class LoginActivity extends Activity {
	SimpleTextInputCellFragment fragAccount,fragPassword;//账号和密码
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);//加载布局
		
		fragAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);//加载控件账号
		fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//加载控件密码
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {  //设置按钮注册的点击监听事件
			
			@Override
			public void onClick(View v) {
				goRegister();
			}
		});
		
		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {    //设置按钮登录的点击监听事件
			
			@Override
			public void onClick(View v) {
				goLogin();
				
			}
		});
		
		findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {  //设置按钮忘记密码的点击监听事件
			
			@Override
			public void onClick(View v) {
				goRecoverPassword();
				
			}
		});
	}
	
	
	@Override
	protected void onResume() { //初始化控件
		super.onResume();
		
		fragAccount.setLabelText("账号:");
		{
			fragAccount.setHintText("请输入账号");
		}
		
		fragPassword.setLabelText("密码:");
		{
			fragPassword.setHintText("请输入密码");
			fragPassword.setIsPassword(true);
		}
	}

	protected void goRegister() {  //跳转到RegisterActivity
		Intent intent=new Intent(this,RegisterActivity.class);
		startActivity(intent);
	}

	protected void goLogin() {//定义登录方法
		String current_user=fragAccount.getText();//获取当前用户的账号
		String current_password=fragPassword.getText();//获取当前用户的密码
		
		if(current_password.length()==0 || current_user.length()==0){
			Toast.makeText(LoginActivity.this, "请输入账户或密码", Toast.LENGTH_SHORT).show();
			return;
		}
		//生成请求体
		MultipartBody.Builder requestBodyBuilder=new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("num", current_user)  //添加当前用户的账号
				.addFormDataPart("password", current_password);//添加当前用户的密码
		
		//创建一个Request，获取url,method参数
		Request request=Servelet.requestuildApi("login")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();//向服务器请求打开URL
		
		//创建进度条
		final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		//客户端连接服务器
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {//服务器启动成功
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						User user;
						progressDialog.dismiss();//进度条消失
							
						try {

							String stirng = arg1.body().string();
							//Toast.makeText(LoginActivity.this, "进来了", Toast.LENGTH_SHORT).show();
							ObjectMapper objectMapper=new ObjectMapper();
							user=objectMapper.readValue(stirng, User.class);//读取值
							
							new AlertDialog.Builder(LoginActivity.this).setTitle("成功")
							.setMessage(user.getName()+","+user.getAccount())
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);//跳转的页面
									startActivity(itnt);	
								}
							}).show();	
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {//服务器启动不成功时
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						progressDialog.dismiss();
						
						new AlertDialog.Builder(LoginActivity.this)
										.setTitle("失败")
										.setMessage("连接失败")
										.setNegativeButton("确定", null)
										.show();
					}
				});
				
			}
		});
	}

	protected void goRecoverPassword() {//跳转到PasswordRecoverActivity
		Intent intent=new Intent(this,PasswordRecoverActivity.class);
		startActivity(intent);
	}
}

