package com.itcast.booksale.fragment.widgets;

import java.util.ArrayList;
import java.util.List;

import com.example.booksale.R;
import com.itcast.booksale.entity.Book;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
	
	
	
	private CheckBox AllChoose_Btn;              //底部按钮栏的全选圆圈按钮
	private TextView count_money_tv;              //底部按钮栏的钱的总数
	
	private Button btn_count_all_bus;            //底部按钮栏的最右边 的结算按钮
	
	
	
	private ImageView back_btn;            //顶部的返回按钮
	private TextView edit;                //顶部的“编辑”
	
	ShoppingBusAdapter adapter;
	
	private int totalPrice;           //定义总价
	
	/**
	 * 批量模式下用来记录当前选中状态
	 */
	private SparseArray<Boolean> mSelectedState=new SparseArray<Boolean>();

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
			//底部按钮栏的最右边 的"结算"按钮
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
	 * 这是底部按钮栏的最右边的"结算"按钮的监听器
	 */
	class CountAllBusMoneyOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			//获得书的信息
			if (btn_count_all_bus.isSelected()) {
				//如果这个按钮被按了,则弹出一个提示框，显示买的东西
				if (count_money_tv!=null) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setTitle("结算")
							.setMessage("您购买了,需要多少钱")
							.setPositiveButton("确认付款",new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									/**
									 * 付款操作
									 */
								}
							}).show();
						}
					});
					
				}else {
					//取消订单
					
				}
			}
		}
		
	}
	
	/*
	 * 这是底部全选圆圈按钮的监听器
	 */
	
	class AllchooseOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if (AllChoose_Btn.isSelected()) {
				//如果"全选"按钮被选中，则继续执行下面的操作
				totalPrice=0;
				if (list_shopping_bus!=null) {
					mSelectedState.clear();           //清全选状态
					if (list_shopping_bus.size()==0) {
						//如果list的大小为0，则返回
						return;
						
					}
					for(int i=0;i<list_shopping_bus.size();i++)
					{
						//获得每行购物列表的id
						int each_id=list_shopping_bus.get(i).getId();
						
						mSelectedState.put(each_id, true);           //给每个购物行设置点中状态
						//把全部价钱加起来
						totalPrice += list_shopping_bus.get(i).getBooknumber() * list_shopping_bus.get(i).getPrice();
					}
					
					//刷新列表
					adapter.notifyDataSetChanged();
					count_money_tv.setText("￥:"+totalPrice+"元");        //输入钱的总数
					
				}
				
			}
			else {
				//否则
				totalPrice=0;         //钱的总数为0
				mSelectedState.clear();
				//刷新
				adapter.notifyDataSetChanged();
				count_money_tv.setText("￥:"+0.00+"元");
				
			}
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View abView;
			if (convertView==null) {
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				abView=inflater.inflate(R.layout.buy_book_bus_each_content, null);
			}
			else {
				abView=convertView;
			}
			final Book book=list_shopping_bus.get(position);          //获得对应的书本信息
			int each_book_id=list_shopping_bus.get(position).getId();                //获得对应的id
			boolean selected=mSelectedState.get(each_book_id, false);       //标记其的id的选择状态
			
			TextView shopname=(TextView) abView.findViewById(R.id.shop_name);    //商店名字
			CheckBox each_item_choose_btn=(CheckBox) abView.findViewById(R.id.each_item_choose); //圆圈选择按钮     
			ImageView bookimage=(ImageView) abView.findViewById(R.id.book_image); //书的图片
			TextView each_bookprice=(TextView) abView.findViewById(R.id.each_item_price);       //图书的价钱
			TextView each_item_reduce=(TextView) abView.findViewById(R.id.each_item_reduce);     //数量的“-”
			final TextView each_item_num=(TextView) abView.findViewById(R.id.each_item_num);          //数量
			TextView each_item_add=(TextView) abView.findViewById(R.id.each_item_add);     //数量的“+”	
			
			each_item_choose_btn.setSelected(selected);         //设置选择状态
			
			/**
			 * 数量的“+”的监听器
			 */
			each_item_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				//获得id
					int id=book.getId();
					boolean selected=mSelectedState.get(id, false);
					//设置数量
					each_item_num.setText(book.getBooknumber()+1);
					notifyDataSetChanged();         //刷新
					if (selected) {
						//选中了
						totalPrice+=book.getPrice();
						count_money_tv.setText("￥" + totalPrice + "元");         //设置总钱数
						
					}
				}
			});
			
			/**
			 * 数量的“-”的监听器
			 */
			each_item_reduce.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (book.getBooknumber()==1) {
						//如果只有一本书，则返回
						return;
						
					}
					int reduce_id=book.getId();          //获得id
					boolean selected=mSelectedState.get(reduce_id, false);
					//设置数量
					each_item_num.setText(book.getBooknumber()-1);
					notifyDataSetChanged();         //刷新
					
					if (selected) {
						//选中了
						totalPrice-=book.getPrice();
						count_money_tv.setText("￥" + totalPrice + "元");         //设置总钱数
						
					}
				}
			});
			return abView;
		}
		
	}
}
