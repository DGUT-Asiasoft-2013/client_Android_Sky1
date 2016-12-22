package com.itcast.booksale.fragment.pages;

import java.io.IOException;

import com.example.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.servelet.Servelet;

import android.R.string;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MyselfFragment extends Fragment{
View view;


TextView user_name;//用户名
ProgressBar progressBar;//进度条
AvatarView avatar;//用户头像

TextView fragTextMyself;
Button btn_login,btn_register;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_myself, null);//加载布局
			
			user_name=(TextView) view.findViewById(R.id.current_user);//用户名
			progressBar=(ProgressBar) view.findViewById(R.id.progress);//进度条
			avatar=(AvatarView) view.findViewById(R.id.avatar);//头像
			
			fragTextMyself=(TextView) view.findViewById(R.id.tab_tv_myself);
			
			btn_login=(Button) view.findViewById(R.id.login);//登录
			btn_register=(Button) view.findViewById(R.id.register);//注册
			
			btn_login.setOnClickListener(new View.OnClickListener() {//为登录按钮设置点击监听事件
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
					
				}
			});
			
			btn_register.setOnClickListener(new View.OnClickListener() {//为注册按钮设置点击监听事件
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),RegisterActivity.class);
					startActivity(intent);		
				}
			});
		}
		return view;
	}
	
	@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
		
			user_name.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			
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
						
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								MyselfFragment.this.onReponse(arg0,user);
							}
						});
					}catch(final Exception e){
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								MyselfFragment.this.OnFailure(arg0,e);
								
							}
						});
					}
					
				}
				
				@Override
				public void onFailure(final Call arg0, final IOException arg1) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							MyselfFragment.this.OnFailure(arg0,arg1);
							
						}
					});
					
				}
			});
		}

	protected void onReponse(Call arg0, User user) {
		progressBar.setVisibility(View.GONE);
		avatar.load(user);
		user_name.setVisibility(View.VISIBLE);
		user_name.setTextColor(Color.BLACK);
		user_name.setText("Hi,"+user.getName());
		
	}

	protected void OnFailure(Call arg0, Exception e) {
		progressBar.setVisibility(View.GONE);
		user_name.setVisibility(View.VISIBLE);
		user_name.setTextColor(Color.RED);
		user_name.setText(e.getMessage());
	}
}
