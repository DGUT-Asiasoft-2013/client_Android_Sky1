package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
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
	 private static final String FILE_NAME="saveUserNamePwd";
	CheckBox autoLogin;
	
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);//加载布局
		
		fragAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);//加载控件账号
		fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//加载控件密码

		autoLogin=(CheckBox) findViewById(R.id.cb_auto_login);
		
	       SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	        //从文件中获取保存的数据
	        String usernameContent = sharedPreferences.getString("username", "");
	        String passwordContent = sharedPreferences.getString("password", "");
	        //判断是否有数据存在，并进行相应处理
	        if(usernameContent != null && !"".equals(usernameContent))
	        	fragAccount.setText(usernameContent);
	        if(passwordContent != null && !"".equals(passwordContent))
	        	fragPassword.setText(passwordContent);
			
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
				if (autoLogin.isChecked()){
					onSaveContent();
				}
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
	
    protected void onSaveContent() {
        super.onStop();
        String usernameContent = fragAccount.getText();
        String passwordContent = fragPassword.getText();
        //获取SharedPreferences时，需要设置两参数
        //第一个是保存的文件的名称，第二个参数是保存的模式（是否只被本应用使用）
        SharedPreferences sharedPreferences =
                getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        //添加要保存的数据
        editor.putString("username", usernameContent);
        editor.putString("password", passwordContent);
        //确认保存
        editor.commit();
    }

	protected void goRegister() {  //跳转到RegisterActivity
		//Intent intent=new Intent(this,RegisterFirstActivity.class);
		Intent intent=new Intent(this, RegisterFirstActivity.class);
		startActivity(intent);
	}

	protected void goLogin() {//定义登录方法
		String current_user=fragAccount.getText();//获取当前用户的账号
		String current_password=fragPassword.getText();//获取当前用户的密码
		
		if(current_password.length()==0){
			Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;    
		}else if (current_user.length()==0) {
			Toast.makeText(LoginActivity.this, "请输入账户", Toast.LENGTH_SHORT).show();
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
				final String string=arg1.body().string();           //后台运行
				
				LoginActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						User user;
						progressDialog.dismiss();//进度条消失
						try {

							if(TextUtils.isEmpty(string)){//判断解析出来的是否为空字符串（如果为空，则数据库中没有此用户）
								Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
							}

							ObjectMapper objectMapper=new ObjectMapper();
							user=objectMapper.readValue(string, User.class);//读取值
							Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
							
							Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);//跳转的页面
							startActivity(itnt);
							finish();
							/*new AlertDialog.Builder(LoginActivity.this).setTitle("成功")
							.setMessage(user.getName()+","+user.getAccount())
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);//跳转的页面
									startActivity(itnt);
								}
							}).show();	*/
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

