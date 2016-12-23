package com.itcast.booksale.fragment.pages;

import java.io.IOException;

import com.itcast.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.myself.OrderActivity;
import com.itcast.booksale.myself.PersonalActivity;
import com.itcast.booksale.myself.SettingActivity;
import com.itcast.booksale.myself.SumMoneyActivity;
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
View view_setting,view_personal,view_sum_money,view_order;//����,��������

TextView user_name;//�û���
ProgressBar progressBar;//������
AvatarView avatar;//�û�ͷ��

TextView fragTextMyself;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_myself, null);//���ز���

     		user_name=(TextView) view.findViewById(R.id.current_user);//�û���
			progressBar=(ProgressBar) view.findViewById(R.id.progress);//������
			avatar=(AvatarView) view.findViewById(R.id.avatar);//ͷ��
			
			fragTextMyself=(TextView) view.findViewById(R.id.tab_tv_myself);
	
			view_setting=view.findViewById(R.id.view_setting);//���õİ�ť
			view_personal=view.findViewById(R.id.view_personal);//�������ϰ�ť
			view_sum_money=view.findViewById(R.id.view_sum_money);//���ʲ��İ�ť
			view_order=view.findViewById(R.id.view_order);//�����İ�ť
			
			view_setting.setOnClickListener(new View.OnClickListener() {//Ϊ���ð�ť��ӵ�������¼�
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),SettingActivity.class);
					startActivity(intent);			
				}
			});
			
			view_personal.setOnClickListener(new View.OnClickListener() {//Ϊ�����������õ�������¼�
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), PersonalActivity.class);
					startActivity(intent);	
				}
			});
			
			view_sum_money.setOnClickListener(new View.OnClickListener() {//Ϊ���ʲ���ť���õ�������¼�
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), SumMoneyActivity.class);
					startActivity(intent);	
				}
			});
			
			view_order.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), OrderActivity.class);
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
