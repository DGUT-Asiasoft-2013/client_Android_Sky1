package com.itcast.booksale;

import com.itcast.booksale.R;
import com.itcast.booksale.fragment.pages.BookListFragment;
import com.itcast.booksale.fragment.pages.MyselfFragment;
import com.itcast.booksale.fragment.pages.SubscribeListUserFragment;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnNewClickedListener;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment.OnTabSelectedListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {
	BookListFragment Home = new BookListFragment();// 锟斤拷页
	SubscribeListUserFragment booking = new SubscribeListUserFragment();// 锟斤拷锟斤拷
	Buy_book_bus_fragment shoppingcar = new Buy_book_bus_fragment();// 锟斤拷锟斤车
	MyselfFragment myself = new MyselfFragment();// 锟揭碉拷

	
	MainTabbarFragment tabbar;// 锟斤拷锟斤拷tabbar

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		if (tabbar.getSelectedIndex() < 0) {
			tabbar.setSelectedItem(0);
		}
//		tabbar.setSelectedItem(0);
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
	
	   /*//瀹氫箟涓�涓〉闈㈠垏鎹�
		public static Fragment chageFragment(Fragment currentFragment
				,Fragment chooseFragment
				,FragmentTransaction ft)
		{
			if (currentFragment!=chooseFragment) {
				//濡傛灉褰撳墠椤甸潰涓嶇瓑浜庨�夋嫨鐨勯〉闈�,闅愯棌褰撳墠椤甸潰
				ft.hide(currentFragment);
				if (chooseFragment.isAdded()) {
					//濡傛灉閫夋嫨鐨勯〉闈㈠凡缁忚鍒ゆ柇鏄惁琚坊鍔犲埌浜咥ctivity閲岄潰鍘讳簡锛屽垯鏄剧ず鍏�
					ft.show(chooseFragment);
					
				}
				else {
					ft.add(R.id.content, chooseFragment);
					ft.hide(currentFragment);
				}
				
				
			}
			ft.commitAllowingStateLoss();
			return chooseFragment;
		}
		
		//鍒囨崲鑷抽�夋嫨椤甸潰
		public void ChangetoChooseFragment() {
			//鑾峰緱FragmentTransaction瀵硅薄
			FragmentTransaction fTransaction=getFragmentManager().beginTransaction()
			
			
		}*/

}
