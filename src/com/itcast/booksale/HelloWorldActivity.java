package com.itcast.booksale;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.pages.BookListFragment;
import com.itcast.booksale.fragment.pages.MyselfFragment;
import com.itcast.booksale.fragment.pages.SubscribeListUserFragment;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnNewClickedListener;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnTabSelectedListener;
import com.itcast.booksale.servelet.Servelet;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelloWorldActivity extends Activity {
	BookListFragment Home = new BookListFragment();// 锟斤拷页
	SubscribeListUserFragment booking = new SubscribeListUserFragment();// 锟斤拷锟斤拷
	Buy_book_bus_fragment shoppingcar = new Buy_book_bus_fragment();// 锟斤拷锟斤车
	MyselfFragment myself = new MyselfFragment();// 锟揭碉拷
	TextView t;
	String count;
	MainTabbarFragment tabbar;// 锟斤拷锟斤拷tabbar
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		t = (TextView)(findViewById(R.id.frag_tabbar).findViewById(R.id.tab_subscribe_count));
		getMe();
		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeFragment(index);// 转锟斤拷fragment

			}
		});

		tabbar.setOnNewClickedListener(new OnNewClickedListener() {//锟接号的硷拷锟斤拷锟铰硷拷

			@Override
			public void onNewClicked() {
				bringUpEditor();

			}
		});


	}

	@Override
	protected void onResume() {// 锟斤拷始锟斤拷
		// TODO Auto-generated method stub
		super.onResume();
		if (tabbar.getSelectedItem() < 0) {
			tabbar.setSelectedItem(0);
		}
	}

	protected void changeFragment(int index) {
		FragmentTransaction fTransaction=getFragmentManager().beginTransaction();
		Fragment newFrag = null;

		String tag=null;
		switch (index) {
		case 0:
			newFrag = Home;// 锟斤拷页
			//			tag="home";
			break;

		case 1:
			newFrag = booking;// 锟斤拷锟斤拷
			break;

		case 2:
			newFrag = shoppingcar;// 锟斤拷锟斤车
			break;

		case 3:
			newFrag = myself;// 锟揭碉拷
			break;

		default:
			break;
		}

		if(newFrag==null)  return;
		//		newFrag=chageFragment(newFrag, chooseFragment, fTransaction);
		getFragmentManager().beginTransaction().replace(R.id.content, newFrag).commit();//瑁呮崲fragment
	}

	protected void bringUpEditor() {
		Intent intent=new Intent(HelloWorldActivity.this,ShareBooksActivity.class);
		startActivity(intent);
	}
	void getMe(){
		Request request=Servelet.requestuildApi("me")
				.method("get", null)
				.build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(final Call arg0,final Response arg1) throws IOException {
				final String str = arg1.body().string();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							user=new ObjectMapper().readValue(str, User.class);
							HelloWorldThread h = new HelloWorldThread(user);
							h.start();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
			}
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
			}
		});
	}
	public class HelloWorldThread extends Thread{
		User user;
		public HelloWorldThread(User user) {
			this.user = user;
		}

		@Override
		public void run() {
			while(true){
				OkHttpClient client= Servelet.getOkHttpClient();

				Request request =Servelet.requestuildApi("/subscribe/"+user.getId()+"/count")
						.method("get", null)
						.build();
				client.newCall(request).enqueue(new Callback() {

					@Override
					public void onResponse(Call arg0, Response arg1) throws IOException {
						Integer i = new ObjectMapper().readValue(arg1.body().string(),Integer.class);
						count = i.toString();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(count.equals("0")){
									t.setText("");
								}else{
									t.setText(count);
								}
								Log.d("count", count);
							}
						});
					}
					@Override
					public void onFailure(Call arg0, IOException arg1) {
					}
				});
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}


