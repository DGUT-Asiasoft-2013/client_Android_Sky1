package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.itcast.booksale.inputcells.PictureInputCellFragment;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {
	SimpleTextInputCellFragment fragInputCellAccount;//用户的账号
	SimpleTextInputCellFragment fragInputCellName;//用户的昵称
	SimpleTextInputCellFragment fragInputCellPassword;//用户的密码
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;//重复密码
	SimpleTextInputCellFragment fragInputCellEmail;//用户的邮箱地址
	SimpleTextInputCellFragment fragInputCellPhone;//用户的电话号码
	SimpleTextInputCellFragment fragInputCellQq;//用户的QQ
	
	
	PictureInputCellFragment fragImage;//头像

	ProgressDialog ProgressDialog;//进度对话框


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);//加载布局
		
		fragInputCellAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);//加载账号account
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);//加载昵称name
		fragInputCellPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//加载密码password
		fragInputCellPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//加载重复密码
		fragInputCellEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//加载邮箱Emai
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_phone);//加载电话号码Phone
		fragInputCellQq=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_qq);//加载QQ
		
		fragImage=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);//加载头像avatar
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {//设置按钮btn_submit的点击事件
			
			@Override
			public void onClick(View v) {
				Submit();
			}
		});
	}


	@Override
		protected void onResume() {//初始化控件
			// TODO Auto-generated method stub
			super.onResume();
			
			fragInputCellAccount.setLabelText("账号:");//账号
			{
				fragInputCellAccount.setHintText("请输入账号");
			}
			
			fragInputCellName.setLabelText("昵称:");//昵称
			{
				fragInputCellName.setHintText("请输入昵称");
			}
			
			fragInputCellPassword.setLabelText("密码:");//密码
			{
				fragInputCellPassword.setHintText("请输入密码");
			}
			
			fragInputCellPasswordRepeat.setLabelText("重复密码:");//重复密码
			{
				fragInputCellPasswordRepeat.setHintText("请再次输入密码");
			}
			
			fragInputCellEmail.setLabelText("邮箱:");//邮箱
			{
				fragInputCellEmail.setHintText("请输入邮箱");
				fragInputCellEmail.setIsEmail(true);
			}
			
			fragInputCellPhone.setLabelText("电话:");//电话
			{
				fragInputCellPhone.setHintText("请输入电话号码");
				fragInputCellPhone.setIsPhone(true);
			}
			
			fragInputCellQq.setLabelText("QQ:");//QQ
			{
				fragInputCellQq.setHintText("请输入QQ号码");
				fragInputCellQq.setIsNumber(true);
			}
		}
	
	protected void Submit() {  //定义提交按钮的方法
		String password=fragInputCellPassword.getText();//获取输入的密码
		String passwordRepeat=fragInputCellPasswordRepeat.getText();//获取输入的重复密码
		
		if(!password.equals(passwordRepeat)){//判断两次输的密码是否一样，若不一样则弹出对话提示框
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("两次输入的密码不一样")
			.setNegativeButton("好", null)
			.show();//显示对话框
			
			return;
		}
		
		String account=fragInputCellAccount.getText();//获取输入的账户
		String name=fragInputCellName.getText();//获取输入的昵称
		String email=fragInputCellEmail.getText();//获取输入的邮箱
		String phone=fragInputCellPhone.getText();//获取输入的电话
		String qq=fragInputCellQq.getText();//获取输入的QQ
	
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)//生成请求体
				.addFormDataPart("num", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("password", password)
				.addFormDataPart("phoneNumb", phone)
				.addFormDataPart("qq", qq);
		
		if(fragImage.getPngData()!=null){//若头像不为空时，获取头像
			requestBodyBuilder.addFormDataPart("avatar", "avatar",RequestBody.create(MediaType.parse("image/png")
					, fragImage.getPngData()));
		}
		
		//发起请求
		Request request=Servelet.requestuildApi("register")
								.method("post", null)
								.post(requestBodyBuilder.build())
								.build();
		
		//进度条
		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		//客户端连接服务器
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {//可以连接服务器时
				runOnUiThread(new Runnable() {
					String ar=arg1.body().toString();
					@Override
					public void run() {
						progressDialog.dismiss();//进度条消失						
						
						RegisterActivity.this.onResponse(arg0, ar);		
					}
				});
					
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();// 使ProgressDialog消失

						RegisterActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}


	protected void onFailure(Call arg0, Exception arg1) {//无法连接服务器时
		new AlertDialog.Builder(this).setTitle("请求失败").setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null).show();
	}


	protected void onResponse(Call arg0, String ar) {//成功
		try {
			new AlertDialog.Builder(this).setTitle("请求成功").setMessage("您的请求成功")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);//跳转到登陆页面
					startActivity(intent);
				}
			})
			.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onFailure(arg0, e);
		}
	}
}
