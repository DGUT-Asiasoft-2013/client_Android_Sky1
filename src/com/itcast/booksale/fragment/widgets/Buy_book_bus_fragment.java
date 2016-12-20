package com.itcast.booksale.fragment.widgets;

import java.util.ArrayList;
import java.util.List;

import com.example.booksale.R;
import com.itcast.booksale.entity.Book;

import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 这是购物车的页面显示
 */
public class Buy_book_bus_fragment extends Fragment {

	View view; // 购物车的页面
	
	View top_view; //购物车顶部的按钮布局
	View bottom_view; // 购物车底端的按钮布局
	private ListView lv_shopping_bus; // 购物车的列表
	private List<Book> list_shopping_bus = new ArrayList<Book>();
	
	
	
	private CheckBox AllChoose_Btn;              //底部按钮栏的圆圈按钮
	private TextView count_money_tv;              //底部按钮栏的钱的总数
	
	private Button btn_count_all_bus;            //底部按钮栏的最右边 的结算按钮
	
	
	
	private ImageView back_btn;            //顶部的返回按钮
	private TextView edit;                //顶部的“编辑”
	
	ShoppingBusAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.buy_book_bus, null); // 加载购物车页面
			bottom_view = inflater.inflate(R.layout.buy_book_bus_bottom_btn, null); // 加载购物车底部按钮的布局
			top_view=inflater.inflate(R.layout.buy_bus_top_view_normal, null);          //加载购物车顶部按钮的布局
			
			
			//获得购物车的界面的listview列表
			lv_shopping_bus=(ListView) view.findViewById(R.id.listview_shopping_bus);
			
			
			/**
			 * 顶部的实现
			 */
			back_btn=(ImageView) top_view.findViewById(R.id.back);             //顶部的返回按钮
			edit=(TextView) top_view.findViewById(R.id.subtitle);               //顶部的“编辑”
			
			
			/**
			 * 底部的实现
			 */
			//底部全选的圆圈按钮
			AllChoose_Btn=(CheckBox) bottom_view.findViewById(R.id.isAllChoose_check);
			//为底部全选圆圈按钮添加监听器
			AllChoose_Btn.setOnClickListener(new AllchooseOnClickListener());
			//底部按钮栏的钱的总数
			count_money_tv=(TextView) bottom_view.findViewById(R.id.count_money);
			//需要为这个钱的总数设置前的总数
			//底部按钮栏的最右边 的结算按钮
			btn_count_all_bus=(Button) bottom_view.findViewById(R.id.count_all_bus);
			//添加监听器
			btn_count_all_bus.setOnClickListener(new CountAllBusMoneyOnClickListener());
			
			
			
			//把顶部按钮栏加载在listview的底部，此必须在setAdapter()前
			lv_shopping_bus.addHeaderView(top_view);
			//把底部按钮栏加载在listview的底部，此必须在setAdapter()前
			lv_shopping_bus.addFooterView(bottom_view);
			
			
			lv_shopping_bus.setAdapter(adapter);        //为lv_shopping_bus设置适配器
		}
		return view;
	}
	
	/*
	 * 这是底部按钮栏的最右边的结算按钮的监听器
	 */
	class CountAllBusMoneyOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	/*
	 * 这是底部全选圆圈按钮的监听器
	 */
	
	class AllchooseOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	
	
	/**
	 * 下面为购物车的列表lv_shopping_bus的是适配器
	 */
	
	class ShoppingBusAdapter extends BaseAdapter
	{

		Book book;
		@Override
		public int getCount() {
			return list_shopping_bus==null?0:list_shopping_bus.size();
		}

		@Override
		public Object getItem(int position) {
			return list_shopping_bus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View abView;
			if (convertView==null) {
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				abView=inflater.inflate(R.layout.buy_book_bus_each_content, null);
			}
			else {
				abView=convertView;
			}
			
			TextView shopname=(TextView) abView.findViewById(R.id.shop_name);    //商店名字
			CheckBox each_item_choose_btn=(CheckBox) abView.findViewById(R.id.each_item_choose); //圆圈按钮     
			ImageView bookimage=(ImageView) abView.findViewById(R.id.book_image); //书的图片
			TextView each_bookprice=(TextView) abView.findViewById(R.id.each_item_price);       //图书的价钱
			TextView each_item_reduce=(TextView) abView.findViewById(R.id.each_item_reduce);     //数量的“-”
			TextView each_item_num=(TextView) abView.findViewById(R.id.each_item_num);          //数量
			TextView each_item_add=(TextView) abView.findViewById(R.id.each_item_add);     //数量的“+”	
			
			
			return abView;
		}
		
	}
}
