package com.itcast.booksale.myself;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.inputcells.ChangeInputCellFragment;
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

/**
 * 改变昵称
 * 
 * @author Administrator
 *
 */
public class ChangeNameActivity extends Activity {
	//ChangeInputCellFragment fragNameTitle,fragNameHint;
	TextView nametitle;
	EditText namehint;
	
//	ChangeInputCellFragment fragName;
	EditText change_name;
	
	Button btn_change;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_name);
		
		
	/*	fragNameTitle=(ChangeInputCellFragment) getFragmentManager().findFragmentById(R.id.change_title);
		fragNameHint=(ChangeInputCellFragment) getFragmentManager().findFragmentById(R.id.change_edittext);*/
		
		nametitle=(TextView) findViewById(R.id.change_title);
		namehint=(EditText) findViewById(R.id.change_edittext);
	
//		fragName=(ChangeInputCellFragment) getFragmentManager().findFragmentById(R.id.change_edittext);
		
		change_name=(EditText) findViewById(R.id.change_edittext);
		btn_change=(Button) findViewById(R.id.btn_change);
		
		btn_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goChangeName();
			}
		});
	}
	
	protected void goChangeName() {
		// TODO Auto-generated method stub
		String name=change_name.getText().toString();
		
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("name", name)
				.build();
		
		Request request=Servelet.requestuildApi("change/name")
						.post(body)
						.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				byte[] ar=arg1.body().bytes();
				
				try {
					final Boolean succeed=new ObjectMapper().readValue(ar, Boolean.class);
							
					ChangeNameActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(succeed){
								ChangeNameActivity.this.onResponse(arg0,"成功");
								
							}else {
								ChangeNameActivity.this.onFailure(arg0,new Exception("失败"));
							}
						}
					});		
				} catch (final Exception e) {
					ChangeNameActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							ChangeNameActivity.this.onFailure(arg0,e);
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				Toast.makeText(ChangeNameActivity.this , arg1.getLocalizedMessage(), Toast.LENGTH_SHORT)
				.show();
				
				ChangeNameActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(ChangeNameActivity.this)
						.setTitle("失败")
						.setMessage(arg1.getLocalizedMessage())
						.show();
					}
				});
			}
		});
	}
	
	void onFailure(Call arg0,Exception exception){
		Toast.makeText(ChangeNameActivity.this , exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
		.show();
	}
	
	void onResponse(Call arg0,String string){
		Toast.makeText(ChangeNameActivity.this, "成功修改昵称", Toast.LENGTH_SHORT)
		.show();
		
		//finish掉上一个个人资料
		PersonalActivity.instance.finish();
		
		Intent intent=new Intent(ChangeNameActivity.this,PersonalActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*fragNameHint.setHintText("输入昵称");
		
		fragNameTitle.setTitleText("昵称");*/
		
		nametitle.setText("昵称");
		namehint.setHint("请输入新昵称");
	}
}
