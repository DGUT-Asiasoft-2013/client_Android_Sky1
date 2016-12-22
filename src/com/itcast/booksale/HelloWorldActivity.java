package com.itcast.booksale;

import com.example.booksale.R;
import com.itcast.booksale.fragment.pages.BookListFragment;
import com.itcast.booksale.fragment.pages.MyselfFragment;
import com.itcast.booksale.fragment.pages.SubscribeListUserFragment;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnNewClickedListener;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnTabSelectedListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {
	BookListFragment Home = new BookListFragment();// 首页
	SubscribeListUserFragment booking = new SubscribeListUserFragment();// 订阅
	Buy_book_bus_fragment shoppingcar = new Buy_book_bus_fragment();// 购物车
	MyselfFragment myself = new MyselfFragment();// 我的

	
	MainTabbarFragment tabbar;// 整个tabbar

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeFragment(index);// 转换fragment

			}
		});

		tabbar.setOnNewClickedListener(new OnNewClickedListener() {//加号的监听事件

			@Override
			public void onNewClicked() {
				bringUpEditor();

			}
		});
	}

	@Override
	protected void onResume() {// 初始化
		// TODO Auto-generated method stub
		super.onResume();

		if (tabbar.getSelectedItem() < 0) {
			tabbar.setSelectedItem(0);
		}
	}

	protected void changeFragment(int index) {
		Fragment newFrag = null;

		switch (index) {
		case 0:
			newFrag = Home;// 首页
			break;

		case 1:
			newFrag = booking;// 订阅
			break;

		case 2:
			newFrag = shoppingcar;// 购物车
			break;

		case 3:
			newFrag = myself;// 我的
			break;
			
		default:
			break;
		}

		if(newFrag==null)  return;
		
		getFragmentManager().beginTransaction().replace(R.id.content, newFrag).commit();//代替布局
	}

	protected void bringUpEditor() {
		Intent intent=new Intent(HelloWorldActivity.this,ShareBooksActivity.class);
		startActivity(intent);
		finish();

	}

}
