package com.itcast.booksale.fragment.widgets;

import com.example.booksale.R;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainTabbarFragment extends Fragment{
	View tabHome,tabSubscribes,tabShoppingCar,tabMyself,tabInformation;//首页，订阅，购物车，我的，发送商品信息
	View[] tabs;//标签选项卡
	TextView tabTvShoppingcar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_main_tabbar, null);//加载布局

		tabHome=view.findViewById(R.id.tab_home);//首页
		tabSubscribes=view.findViewById(R.id.tab_subscribes);//订阅
		tabShoppingCar=view.findViewById(R.id.tab_shoppingCar);//购物车
		tabMyself=view.findViewById(R.id.tab_myself);//我的
		tabInformation=view.findViewById(R.id.tab_information);//发送商品信息
		
		tabTvShoppingcar=(TextView) view.findViewById(R.id.tab_tv_shoppingcar);//购物车的文字
		
		if (tabTvShoppingcar.isSelected()) {
			tabTvShoppingcar.setTextColor(Color.GREEN);
			
		}
				
		tabs=new View[]{//选项卡数组
				tabHome,tabSubscribes,tabShoppingCar,tabMyself};
		
		for(final View tab:tabs){//为选项卡设置点击监听事件
			tab.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onTabClicked(tab);
				}
			});
		}
		
		tabInformation.setOnClickListener(new View.OnClickListener() {//为发送信息设置点击监听事件
			
			@Override
			public void onClick(View v) {
				onInforClicked();			
			}
		});
		return view;
	}
	
	public static interface OnTabSelectedListener{//接口类
		void onTabSelected(int index);
	}

	OnTabSelectedListener onTabSelectedListener;
	
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		this.onTabSelectedListener=onTabSelectedListener;
	}
	
	public void setSelectedItem(int index){
		if(index>=0&&index<tabs.length){//当index大于0并且小于选项卡总数目
			onTabClicked(tabs[index]);
		}
	}
	
	public int getSelectedItem(){//获取可选择的选项卡数目
		return tabs.length;
	}
	
	protected void onTabClicked(View tab) {
		int selectedIndex=-1;
		
		for(int i=0;i<tabs.length;i++){//
			View otherTab=tabs[i];
			
			if(otherTab==tab){
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

}
