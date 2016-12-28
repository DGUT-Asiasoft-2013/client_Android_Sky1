package com.itcast.booksale.fragment.pages;

import java.io.IOException;

import com.itcast.booksale.R;
import com.itcast.booksale.R.drawable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.LoginActivity;
import com.itcast.booksale.PrivateMessageListActivity;
import com.itcast.booksale.RegisterActivity;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.myself.MyOrderActivity;
import com.itcast.booksale.myself.PersonalActivity;
import com.itcast.booksale.myself.SettingActivity;
import com.itcast.booksale.myself.SumMoneyActivity;
import com.itcast.booksale.servelet.Servelet;

import android.R.string;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MyselfFragment extends Fragment{
View view;
View view_setting,view_personal,view_sum_money,view_order,view_private_message;//锟斤拷锟斤拷,锟斤拷锟斤拷锟斤拷锟斤拷

TextView user_name;//锟矫伙拷锟斤拷
ProgressBar progressBar;//锟斤拷锟斤拷锟斤拷
AvatarView avatar;//锟矫伙拷头锟斤拷

View line_login_register;

TextView fragTextMyself;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_myself, null);//锟斤拷锟截诧拷锟斤拷

     		user_name=(TextView) view.findViewById(R.id.current_user);//锟矫伙拷锟斤拷
			progressBar=(ProgressBar) view.findViewById(R.id.progress);//锟斤拷锟斤拷锟斤拷
			avatar=(AvatarView) view.findViewById(R.id.avatar);//头锟斤拷
			
			fragTextMyself=(TextView) view.findViewById(R.id.tab_tv_myself);
	
			view_setting=view.findViewById(R.id.view_setting);//锟斤拷锟矫的帮拷钮
			view_personal=view.findViewById(R.id.view_personal);//锟斤拷锟斤拷锟斤拷锟较帮拷钮
			view_sum_money=view.findViewById(R.id.view_sum_money);//锟斤拷锟绞诧拷锟侥帮拷钮
			view_order=view.findViewById(R.id.view_order);//锟斤拷锟斤拷锟侥帮拷钮
			view_private_message=view.findViewById(R.id.view_private_message);
			
			line_login_register=view.findViewById(R.id.line_login_register);
			
			view_setting.setOnClickListener(new View.OnClickListener() {//为锟斤拷锟矫帮拷钮锟斤拷拥锟斤拷锟斤拷锟斤拷锟铰硷拷
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),SettingActivity.class);
					startActivity(intent);			
				}
			});
			
			view_personal.setOnClickListener(new View.OnClickListener() {//为锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫碉拷锟斤拷锟斤拷锟斤拷录锟�
				
				@Override
				public void onClick(View v) {
					/*Intent intent=new Intent(getActivity(), PersonalActivity.class);
					startActivity(intent);	*/
					goPersonal();
				}
			});
			
			view_sum_money.setOnClickListener(new View.OnClickListener() {//为锟斤拷锟绞诧拷锟斤拷钮锟斤拷锟矫碉拷锟斤拷锟斤拷锟斤拷录锟�
				
				@Override
				public void onClick(View v) {
					/*Intent intent=new Intent(getActivity(), SumMoneyActivity.class);
					startActivity(intent);	*/
					goMoney();
				}
			});
			
			view_order.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), MyOrderActivity.class);
					startActivity(intent);
				}
			});
			
			view_private_message.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*Intent intent=new Intent(getActivity(), PrivateMessageListActivity.class);
					startActivity(intent);*/
					goMessage();
				}
			});
			
			line_login_register.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
			});
		}
		return view;
	}
	
	protected void goMessage() {
		// TODO Auto-generated method stub
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
							MyselfFragment.this.onReponseMessage(arg0,user);
						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							MyselfFragment.this.OnFailureMyself(arg0,e);
							
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyselfFragment.this.OnFailureMyself(arg0,arg1);
						
					}
				});
				
			}
		});
	}

	protected void onReponseMessage(Call arg0, User user) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(), PrivateMessageListActivity.class);
		startActivity(intent);
	}

	protected void goMoney() {
		// TODO Auto-generated method stub
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
							MyselfFragment.this.onReponseMoney(arg0,user);
						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							MyselfFragment.this.OnFailureMyself(arg0,e);
							
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyselfFragment.this.OnFailureMyself(arg0,arg1);
						
					}
				});
				
			}
		});
	}


	protected void goPersonal() {
		// TODO Auto-generated method stub
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
							MyselfFragment.this.onReponseMyself(arg0,user);
						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							MyselfFragment.this.OnFailureMyself(arg0,e);
							
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyselfFragment.this.OnFailureMyself(arg0,arg1);
						
					}
				});
				
			}
		});
	}

	protected void onReponseMoney(Call arg0, User user) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(), SumMoneyActivity.class);
		startActivity(intent);
	}

	protected void OnFailureMyself(Call arg0, Exception e) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "对不起，你未登录", Toast.LENGTH_SHORT)
		.show();
	}

	protected void onReponseMyself(Call arg0, User user) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(), PersonalActivity.class);
		startActivity(intent);
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
		if(TextUtils.isEmpty(user.getAvatar())){
			avatar.setBackgroundResource(R.drawable.ic_launcher);
		}
		avatar.load(user);
		user_name.setVisibility(View.VISIBLE);
		user_name.setTextColor(Color.BLACK);
		user_name.setText("Hi,"+user.getName());
		
	}

	protected void OnFailure(Call arg0, Exception e) {
		progressBar.setVisibility(View.GONE);
		user_name.setVisibility(View.VISIBLE);
		user_name.setTextColor(Color.RED);
		//user_name.setText(e.getMessage());
		avatar.setBackgroundResource(R.drawable.ic_launcher);//设置未登录的图片
	}
}
