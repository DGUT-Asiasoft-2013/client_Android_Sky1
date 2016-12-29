package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

//代验证的新功能
public class RegisterFirstActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellPhone;
	
	Button btn_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_first);
		
		btn_next=(Button) findViewById(R.id.btn_next);
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_phone);
	
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}
	protected void goNext() {
		// TODO Auto-generated method stub
		String phone=fragInputCellPhone.getText();
		
		if(phone.length()==0) {
			Toast.makeText(RegisterFirstActivity.this,"电话号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(phone.length()!=11){
			Toast.makeText(RegisterFirstActivity.this,"手机号码输入错误，请输入11位手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!SimpleTextInputCellFragment.isMobileNO(phone)){
			Toast.makeText(RegisterFirstActivity.this,"手机号码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		Toast.makeText(RegisterFirstActivity.this, "验证码已发送到该手机上，请注意查收！", Toast.LENGTH_SHORT)
		.show();
		
		//String phone=fragInputCellPhone.getText();
		Intent intent=new Intent(RegisterFirstActivity.this,RegisterActivity.class);
		
		intent.putExtra("phone", phone);
		
		startActivity(intent);
		
		/*MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)//鐢熸垚璇锋眰浣�
				.addFormDataPart("phone", phone);
		
		Request request=Servelet.requestuildApi("phone")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();
		
		
        Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {//鍙互杩炴帴鏈嶅姟鍣ㄦ椂
				runOnUiThread(new Runnable() {
					String ar=arg1.body().toString();
					@Override
					public void run() {
						RegisterFirstActivity.this.onResponse(arg0, ar);		
					}
				});
					
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						RegisterFirstActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});*/
	}
	
	protected void onFailure(Call arg0, Exception arg1) {//鏃犳硶杩炴帴鏈嶅姟鍣ㄦ椂
		new AlertDialog.Builder(this)
		.setTitle("错误！")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null)
		.show();
	}
	
	protected void onResponse(Call arg0, String ar) {//鎴愬姛
		try {
			Toast.makeText(RegisterFirstActivity.this, "验证码已发送到该手机上，请注意查收！", Toast.LENGTH_SHORT)
			.show();
			
			String phone=fragInputCellPhone.getText();
			Intent intent=new Intent(RegisterFirstActivity.this,RegisterActivity.class);
			
			intent.putExtra("phone", phone);
			
			startActivity(intent);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			onFailure(arg0, e);
			
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragInputCellPhone.setLabelText("手机号码*:");//鐢佃瘽
		{
			fragInputCellPhone.setHintText("请输入11位手机号码(必填)");
			fragInputCellPhone.setIsPhone(true);
		}
	}
}
