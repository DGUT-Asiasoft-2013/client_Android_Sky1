package com.itcast.booksale;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelloWorldActivity extends Activity {
	private static boolean isExit=false;//ding yi yi ge bian liang ,biao shi shi fou tui chu 

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

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
		Request request=Servelet.requestuildApi("/me")
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
						} catch (JsonParseException e) {
							e.printStackTrace();

						} catch (JsonMappingException e) {
							e.printStackTrace();

						}
						catch (IOException e) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	//---------------------------------------
	//隐藏软键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	//----------------------------------------------------==============

}


