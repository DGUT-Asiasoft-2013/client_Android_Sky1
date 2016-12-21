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
	View tabHome,tabSubscribes,tabShoppingCar,tabMyself,tabInformation;//��ҳ�����ģ����ﳵ���ҵģ�������Ʒ��Ϣ
	View[] tabs;//��ǩѡ�
	TextView tabTvShoppingcar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_main_tabbar, null);//���ز���

		tabHome=view.findViewById(R.id.tab_home);//��ҳ
		tabSubscribes=view.findViewById(R.id.tab_subscribes);//����
		tabShoppingCar=view.findViewById(R.id.tab_shoppingCar);//���ﳵ
		tabMyself=view.findViewById(R.id.tab_myself);//�ҵ�
		tabInformation=view.findViewById(R.id.tab_information);//������Ʒ��Ϣ
		
		tabTvShoppingcar=(TextView) view.findViewById(R.id.tab_tv_shoppingcar);//���ﳵ������
		
		if (tabTvShoppingcar.isSelected()) {
			tabTvShoppingcar.setTextColor(Color.GREEN);
			
		}
				
		tabs=new View[]{//ѡ�����
				tabHome,tabSubscribes,tabShoppingCar,tabMyself};
		
		for(final View tab:tabs){//Ϊѡ����õ�������¼�
			tab.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onTabClicked(tab);
				}
			});
		}
		
		tabInformation.setOnClickListener(new View.OnClickListener() {//Ϊ������Ϣ���õ�������¼�
			
			@Override
			public void onClick(View v) {
				onInforClicked();			
			}
		});
		return view;
	}
	
	public static interface OnTabSelectedListener{//�ӿ���
		void onTabSelected(int index);
	}

	OnTabSelectedListener onTabSelectedListener;
	
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		this.onTabSelectedListener=onTabSelectedListener;
	}
	
	public void setSelectedItem(int index){
		if(index>=0&&index<tabs.length){//��index����0����С��ѡ�����Ŀ
			onTabClicked(tabs[index]);
		}
	}
	
	public int getSelectedItem(){//��ȡ��ѡ���ѡ���Ŀ
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
