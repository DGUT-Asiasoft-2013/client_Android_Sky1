package com.itcast.booksale.fragment.widgets;

import com.itcast.booksale.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainTabbarFragment extends Fragment{

	View tabHome,tabSubscribes,tabShoppingCar,tabMyself,tabInformation;//首页，订阅，购物车，我的，发送商品信息
	View[] tabs;//标签选项卡
	TextView tabTvShoppingcar,tabTvMyself;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_main_tabbar, null);//鍔犺浇甯冨眬

		tabHome=view.findViewById(R.id.tab_home);//棣栭〉
		tabSubscribes=view.findViewById(R.id.tab_subscribes);//璁㈤槄
		tabShoppingCar=view.findViewById(R.id.tab_shoppingCar);//璐墿杞︼车
		tabMyself=view.findViewById(R.id.tab_myself);//鎴戠殑
		tabInformation=view.findViewById(R.id.tab_information);//鍙戦�佸晢鍝佷俊鎭�
		

		tabTvShoppingcar=(TextView) view.findViewById(R.id.tab_tv_shoppingcar);//购物车的文字
		tabTvMyself=(TextView) view.findViewById(R.id.tab_tv_myself);//我的

		
				
		tabs=new View[]{//閫夐」鍗℃暟缁�
				tabHome,tabSubscribes,tabShoppingCar,tabMyself};
		
		for(final View tab:tabs){//涓洪�夐」鍗¤缃偣鍑荤洃鍚簨浠�
			tab.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onTabClicked(tab);
				}
			});
		}
		
		tabInformation.setOnClickListener(new View.OnClickListener() {//涓哄彂閫佷俊鎭缃偣鍑荤洃鍚簨浠�

			
			@Override
			public void onClick(View v) {
				onInforClicked();			
			}
		});
		return view;
	}
	
	public static interface OnTabSelectedListener{//鎺ュ彛绫�
		void onTabSelected(int index);
	}

	OnTabSelectedListener onTabSelectedListener;
	
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		this.onTabSelectedListener=onTabSelectedListener;
	}
	
	public void setSelectedItem(int index){
		if(index>=0&&index<tabs.length){//褰搃ndex澶т簬0骞朵笖灏忎簬閫夐」鍗℃�绘暟鐩�
			onTabClicked(tabs[index]);
		}
	}
	
	public int getSelectedItem(){//鑾峰彇鍙�夋嫨鐨勯�夐」鍗℃暟鐩�
		return tabs.length;
	}
	
	protected void onTabClicked(View tab) {
		int selectedIndex=-1;
		
		for(int i=0;i<tabs.length;i++){//
			View otherTab=tabs[i];
			
			if(otherTab==tab){//选中
				otherTab.setSelected(true);
				selectedIndex=i;
			}else{
				otherTab.setSelected(false);
			}
		}
		
		if(onTabSelectedListener!=null && selectedIndex>=0){
			onTabSelectedListener.onTabSelected(selectedIndex);
		}
	}
	
	public static interface OnNewClickedListener{
		void onNewClicked();
	}
	
	OnNewClickedListener onNewClickedListener;
	
	public void setOnNewClickedListener(OnNewClickedListener onNewClickedListener){
		this.onNewClickedListener=onNewClickedListener;
	}

	protected void onInforClicked() {
		if(onNewClickedListener!=null)
			onNewClickedListener.onNewClicked();
	}
	
	//定义一个页面切换
	public static Fragment chageFragment(Fragment currentFragment
			,Fragment chooseFragment
			,FragmentTransaction ft)
	{
		if (currentFragment!=chooseFragment) {
			//如果当前页面不等于选择的页面,隐藏当前页面
			ft.hide(currentFragment);
			if (chooseFragment.isAdded()) {
				//如果选择的页面已经被选择了，则显示其
				ft.show(chooseFragment);
				
			}
			else {
				ft.add(R.id.tab_shoppingCar, chooseFragment);
			}
			
			
		}
		ft.commitAllowingStateLoss();
		return chooseFragment;
	}
	
	//切换页面
	

}
