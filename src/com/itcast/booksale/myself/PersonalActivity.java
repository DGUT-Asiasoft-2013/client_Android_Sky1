package com.itcast.booksale.myself;

import java.io.IOException;

import com.itcast.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 锟斤拷锟斤拷锟斤拷锟斤拷
 * @author Administrator
 *
 */
public class PersonalActivity extends Activity{
static Activity instance;//销毁Activity的一个静态亮

private static final int REQUESTCODE_CAMERA = 0;
private static final int REQUESTCODE_ALBUM = 0;
AvatarView avatar;//头锟斤拷
TextView p_account,p_email,p_name,p_phone,p_qq;//锟剿号ｏ拷锟斤拷锟戒，锟角称ｏ拷锟界话锟斤拷QQ

View p_change_account,p_change_email,p_change_name,p_change_phone,p_change_qq,p_change_avatar;//修改

TextView avatar_none;//没头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_personal);
		
		avatar_none=(TextView) findViewById(R.id.avatar_none);//头像的文字
		
		avatar=(AvatarView) findViewById(R.id.avatar);//头锟斤拷
		
		//fragAvatar=(AvatarChangeFragment) getFragmentManager().findFragmentById(R.id.p_change_avatar);
		
		p_account=(TextView) findViewById(R.id.personal_account);//锟剿伙拷
		p_email=(TextView) findViewById(R.id.personal_email);//锟斤拷锟斤拷
		p_name=(TextView) findViewById(R.id.personal_name);//锟角筹拷
		p_phone=(TextView) findViewById(R.id.personal_phone);//锟界话锟斤拷锟斤拷
		p_qq=(TextView) findViewById(R.id.personal_qq);//QQ
		
		p_change_account=findViewById(R.id.p_change_acount);
		p_change_email=findViewById(R.id.p_change_email);
		p_change_name=findViewById(R.id.p_change_name);
		p_change_phone=findViewById(R.id.p_change_phone);
		p_change_qq=findViewById(R.id.p_change_qq);
		p_change_avatar=findViewById(R.id.p_change_avatar);
		
		p_change_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(PersonalActivity.this, "对不起，账号作为登录ID不能更改", Toast.LENGTH_SHORT).show();
			}
		});
		
		p_change_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(PersonalActivity.this, ChangeNameActivity.class);
				startActivity(intent);
			}
		});
		
		p_change_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalActivity.this, ChangeEmailActivity.class);
				startActivity(intent);
			}
		});
		
		p_change_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalActivity.this, ChangePhoneActivity.class);
				startActivity(intent);
			}
		});
		
		p_change_qq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(PersonalActivity.this, ChangeQqActivity.class);
				startActivity(intent);
			}
		});
		
		p_change_avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onChangeAvatar();
			}
		});
	}
	


protected void onChangeAvatar() {
	String[] items={"拍照","相册"};
	
	new AlertDialog.Builder(this)
	.setTitle("修改头像")
	.setItems(items, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				takePhoto();
				break;

			case 1:
				pickFromAlbm();
			default:
				break;
			}
		}
	}).setPositiveButton("取消", null)
	  .show();
	
}

protected void takePhoto() {
	Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	startActivityForResult(intent, REQUESTCODE_CAMERA);
}

protected void pickFromAlbm() {
	Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
	intent.setType("image/*");
	startActivityForResult(intent, REQUESTCODE_ALBUM);
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
		if(TextUtils.isEmpty(user.getAvatar())){
			//avatar.setBackgroundResource(R.drawable.avatar_not_login);
			avatar_none.setText("点击添加头像");
		}
		avatar.load(user);
		p_name.setText(user.getName());
		p_email.setText(user.getEmail());
		p_phone.setText(user.getPhoneNumb());
		p_account.setText(user.getAccount());
		p_qq.setText(user.getQq());
	}

}
