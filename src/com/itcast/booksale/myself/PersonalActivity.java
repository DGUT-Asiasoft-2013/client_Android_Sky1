package com.itcast.booksale.myself;

import java.io.IOException;

import com.itcast.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ��������
 * @author Administrator
 *
 */
public class PersonalActivity extends Activity{

AvatarView avatar;//ͷ��
TextView p_account,p_email,p_name,p_phone,p_qq;//�˺ţ����䣬�ǳƣ��绰��QQ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		
		avatar=(AvatarView) findViewById(R.id.avatar);//ͷ��
		
		p_account=(TextView) findViewById(R.id.personal_account);//�˻�
		p_email=(TextView) findViewById(R.id.personal_email);//����
		p_name=(TextView) findViewById(R.id.personal_name);//�ǳ�
		p_phone=(TextView) findViewById(R.id.personal_phone);//�绰����
		p_qq=(TextView) findViewById(R.id.personal_qq);//QQ
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
				public void onResponse(final Call arg0, Response arg1) throws IOException {
					String ar=arg1.body().string();
					
					try{
						final User user;
						
						ObjectMapper objectMapper=new ObjectMapper();
						user=objectMapper.readValue(ar, User.class);
						
						PersonalActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								PersonalActivity.this.OnReponse(arg0,user);					
							}
						});
					}catch(final Exception e){
						PersonalActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								PersonalActivity.this.OnFailure(arg0,e);
							}
						});
					}
					
				}
				
				@Override
				public void onFailure(final Call arg0, final IOException arg1) {
					PersonalActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							PersonalActivity.this.OnFailure(arg0, arg1);	
						}
					});
					
				}
			});
		}

	protected void OnFailure(Call arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	protected void OnReponse(Call arg0, User user) {
		avatar.load(user);
		p_name.setText(user.getName());
		p_email.setText(user.getEmail());
		p_phone.setText(user.getPhoneNumb());
		p_account.setText(user.getAccount());
		p_qq.setText(user.getQq());
	}

}
