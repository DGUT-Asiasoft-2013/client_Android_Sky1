package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.R;
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
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册
 * @author Administrator
 *
 */

public class RegisterActivity extends Activity {
	SimpleTextInputCellFragment fragInputCellAccount;//鐢ㄦ埛鐨勮处鍙�
	SimpleTextInputCellFragment fragInputCellName;//鐢ㄦ埛鐨勬樀绉�
	SimpleTextInputCellFragment fragInputCellPassword;//鐢ㄦ埛鐨勫瘑鐮�
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;//閲嶅瀵嗙爜
	SimpleTextInputCellFragment fragInputCellPhone;//鐢ㄦ埛鐨勭數璇濆彿鐮�
	SimpleTextInputCellFragment fragInputCellEmail;//鐢ㄦ埛鐨勯偖绠卞湴鍧�
	SimpleTextInputCellFragment fragInputCellQq;//鐢ㄦ埛鐨凲Q
	
	
	PictureInputCellFragment fragImage;//澶村儚

	ProgressDialog ProgressDialog;//杩涘害瀵硅瘽妗�


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);//鍔犺浇甯冨眬
		
		fragInputCellAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);//鍔犺浇璐﹀彿account
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);//鍔犺浇鏄电Оname
		fragInputCellPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//鍔犺浇瀵嗙爜password
		fragInputCellPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//鍔犺浇閲嶅瀵嗙爜
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_phone);//鍔犺浇鐢佃瘽鍙风爜Phone
		fragInputCellEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//鍔犺浇閭Emai
		fragInputCellQq=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_qq);//鍔犺浇QQ
		
		fragImage=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);//鍔犺浇澶村儚avatar
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {//璁剧疆鎸夐挳btn_submit鐨勭偣鍑讳簨浠�
			
			@Override
			public void onClick(View v) {
				Submit();
			}
		});
	}


	@Override
		protected void onResume() {//鍒濆鍖栨帶浠�
			// TODO Auto-generated method stub
			super.onResume();
			
			fragInputCellAccount.setLabelText("账号*:");//璐﹀彿
			{
				fragInputCellAccount.setHintText("请输入账号(必填)");
			}
			
			fragInputCellName.setLabelText("昵称*:");//鏄电О
			{
				fragInputCellName.setHintText("请输入昵称(必填)");
			}
			
			fragInputCellPassword.setLabelText("密码*:");//瀵嗙爜
			{
				fragInputCellPassword.setHintText("请输入密码(必填)");
				fragInputCellPassword.setIsPassword(true);
			}
			
			fragInputCellPasswordRepeat.setLabelText("重复密码*:");//閲嶅瀵嗙爜
			{
				fragInputCellPasswordRepeat.setHintText("请再次输入密码(必填)");
				fragInputCellPasswordRepeat.setIsPassword(true);
			}
			
			fragInputCellPhone.setLabelText("手机号码*:");//鐢佃瘽
			{
				fragInputCellPhone.setHintText("请输入11位手机号码(必填)");
				fragInputCellPhone.setIsPhone(true);
			}
			
			fragInputCellEmail.setLabelText("邮箱:");//閭
			{
				fragInputCellEmail.setHintText("请输入绑定邮箱");
				fragInputCellEmail.setIsEmail(true);
			}
			
			fragInputCellQq.setLabelText("QQ:");//QQ
			{
				fragInputCellQq.setHintText("请输入QQ");
				fragInputCellQq.setIsNumber(true);
			}
		}
	
	protected void Submit() {  //瀹氫箟鎻愪氦鎸夐挳鐨勬柟娉�
		String password=fragInputCellPassword.getText();//鑾峰彇杈撳叆鐨勫瘑鐮�
		String passwordRepeat=fragInputCellPasswordRepeat.getText();//鑾峰彇杈撳叆鐨勯噸澶嶅瘑鐮�
		
		if(!password.equals(passwordRepeat)){//鍒ゆ柇涓ゆ杈撶殑瀵嗙爜鏄惁涓�鏍凤紝鑻ヤ笉涓�鏍峰垯寮瑰嚭瀵硅瘽鎻愮ず妗�
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("两次输入的密码不正确")
			.setNegativeButton("濂�", null)
			.show();//鏄剧ず瀵硅瘽妗�
			
			return;
		}
		
		String account=fragInputCellAccount.getText();//鑾峰彇杈撳叆鐨勮处鎴�
		String name=fragInputCellName.getText();//鑾峰彇杈撳叆鐨勬樀绉�
		String email=fragInputCellEmail.getText();//鑾峰彇杈撳叆鐨勯偖绠�
		String phone=fragInputCellPhone.getText();//鑾峰彇杈撳叆鐨勭數璇�
		String qq=fragInputCellQq.getText();//鑾峰彇杈撳叆鐨凲Q
	
		
		if(account.length()==0){
			Toast.makeText(RegisterActivity.this,"账户不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if (password.length()==0) {
			Toast.makeText(RegisterActivity.this,"密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if (passwordRepeat.length()==0) {
			Toast.makeText(RegisterActivity.this,"密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if (name.length()==0) {
			Toast.makeText(RegisterActivity.this,"昵称不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if(phone.length()==0) {
			Toast.makeText(RegisterActivity.this,"电话号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(email.length()!=0&&!SimpleTextInputCellFragment.isEmail(email)){
			Toast.makeText(RegisterActivity.this,"邮箱地址输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(phone.length()!=11){
			Toast.makeText(RegisterActivity.this,"手机号码输入错误，请输入11位手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//need to start at 13 or 17 or other normal telnum
		if(!SimpleTextInputCellFragment.isMobileNO(phone)){
			Toast.makeText(RegisterActivity.this,"手机号码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(qq.length()!=0&&!SimpleTextInputCellFragment.isQQ(qq)){
			Toast.makeText(RegisterActivity.this,"QQ号码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//新的功能未能验证
		/*Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String phone=bundle.getString("phone");*/
		
		Toast.makeText(RegisterActivity.this , phone, Toast.LENGTH_SHORT)
		.show();
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)//鐢熸垚璇锋眰浣�
				.addFormDataPart("num", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("password", password)
				.addFormDataPart("phoneNumb", phone)
				.addFormDataPart("qq", qq);
		
		if(fragImage.getPngData()!=null){//鑻ュご鍍忎笉涓虹┖鏃讹紝鑾峰彇澶村儚
			requestBodyBuilder.addFormDataPart("avatar", "avatar",RequestBody.create(MediaType.parse("image/png")
					, fragImage.getPngData()));
		}
		
		//鍙戣捣璇锋眰
		Request request=Servelet.requestuildApi("register")
								.method("post", null)
								.post(requestBodyBuilder.build())
								.build();
		
		//杩涘害鏉�
		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		//瀹㈡埛绔繛鎺ユ湇鍔″櫒
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {//鍙互杩炴帴鏈嶅姟鍣ㄦ椂
				runOnUiThread(new Runnable() {
					String ar=arg1.body().toString();
					@Override
					public void run() {
						progressDialog.dismiss();//杩涘害鏉℃秷澶�						
						
						RegisterActivity.this.onResponse(arg0, ar);		
					}
				});
					
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();// 浣縋rogressDialog娑堝け

						RegisterActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}


	protected void onFailure(Call arg0, Exception arg1) {//鏃犳硶杩炴帴鏈嶅姟鍣ㄦ椂
		new AlertDialog.Builder(this).setTitle("失败").setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null).show();
	}


	protected void onResponse(Call arg0, String ar) {//鎴愬姛
		try {
			/*new AlertDialog.Builder(this).setTitle("成功").setMessage("注册成功")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);//璺宠浆鍒扮櫥闄嗛〉闈�
					startActivity(intent);
				}
			})
			.show();*/
			Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onFailure(arg0, e);
		}
	}
}
