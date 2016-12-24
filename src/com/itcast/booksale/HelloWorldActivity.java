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
	BookListFragment Home = new BookListFragment();// ��ҳ
	SubscribeListUserFragment booking = new SubscribeListUserFragment();// ����
	Buy_book_bus_fragment shoppingcar = new Buy_book_bus_fragment();// ���ﳵ
	MyselfFragment myself = new MyselfFragment();// �ҵ�

	
	MainTabbarFragment tabbar;// ����tabbar

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeFragment(index);// ת��fragment

			}
		});

		tabbar.setOnNewClickedListener(new OnNewClickedListener() {//�Ӻŵļ����¼�

			@Override
			public void onNewClicked() {
				bringUpEditor();

			}
		});
	}

	@Override
	protected void onResume() {// ��ʼ��
		// TODO Auto-generated method stub
		super.onResume();

		/*if (tabbar.getSelectedItem() < 0) {
			tabbar.setSelectedItem(0);
		}*/
		tabbar.setSelectedItem(0);
	}

	protected void changeFragment(int index) {
		FragmentTransaction fTransaction=getFragmentManager().beginTransaction();
		Fragment newFrag = null;

		String tag=null;
		switch (index) {
		case 0:
			newFrag = Home;// ��ҳ
//			tag="home";
			break;

		case 1:
			newFrag = booking;// ����
			break;

		case 2:
			newFrag = shoppingcar;// ���ﳵ
			break;

		case 3:
			newFrag = myself;// �ҵ�
			break;
			
		default:
			break;
		}

		if(newFrag==null)  return;
//		newFrag=chageFragment(newFrag, chooseFragment, fTransaction);
		getFragmentManager().beginTransaction().replace(R.id.content, newFrag).commit();//装换fragment
	}

	protected void bringUpEditor() {
		Intent intent=new Intent(HelloWorldActivity.this,ShareBooksActivity.class);
		startActivity(intent);
	}
	
	   /*//定义一个页面切换
		public static Fragment chageFragment(Fragment currentFragment
				,Fragment chooseFragment
				,FragmentTransaction ft)
		{
			if (currentFragment!=chooseFragment) {
				//如果当前页面不等于选择的页面,隐藏当前页面
				ft.hide(currentFragment);
				if (chooseFragment.isAdded()) {
					//如果选择的页面已经被判断是否被添加到了Activity里面去了，则显示其
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
		
		//切换至选择页面
		public void ChangetoChooseFragment() {
			//获得FragmentTransaction对象
			FragmentTransaction fTransaction=getFragmentManager().beginTransaction()
			
			
		}*/

}
