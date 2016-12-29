package com.itcast.booksale;

import java.util.ArrayList;
import java.util.List;

import com.itcast.booksale.BooksContentActivity.CommentAdapter;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OrdersActivity extends Activity {
	List<OrderLists> list;
	ListView ordersListView;

	Bookbus bookbus;//get bookbus's massage what were putted

	//ye mian shu xing
	private AvatarView bookUserAvatar; // 图书卖家照片
	private TextView bookUserName; // 卖书卖家姓名
	private TextView btn_order;//提交订单
	private TextView momey_all;//总金额
	private String AllPay;

	private BookAvatarView bookAvatar; // 图书照片
	private TextView bookTitle; // 图书标题
	private TextView bookAuthor;//图书作者
	private TextView bookSummary; //图书简介
	private TextView bookPrice;// 售价
	private TextView books_buy_numb;//购买数量


	//Pay Type Spinner
	private Spinner payTypeSpinner;
	private List<String> payType_list;
	private ArrayAdapter<String> payType_adapter;
	String payType_text;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders_view);
		//get bookbus
		bookbus = (Bookbus) getIntent().getSerializableExtra("bookbus");
		Log.d("bookbussssss", bookbus.getId().getBook().getAuthor());
		ordersListView = (ListView) findViewById(R.id.buy_orders);
		AllPay = getIntent().getStringExtra("AllPay");
		initorders(); // 初始化
		//设置
		bookUserAvatar.load(bookbus.getId().getUser());
		bookUserName.setText(bookbus.getId().getUser().getName());
		momey_all.setText(AllPay);
		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//提交订单

			}
		});
		ordersListView.setAdapter(orderListAdapter);
		

	}

	void initorders() {
		bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);
		bookUserName = (TextView) findViewById(R.id.user_name);
		momey_all = (TextView) findViewById(R.id.momey_all);
		btn_order = (TextView) findViewById(R.id.btn_order);
		payTypeSpinner = (Spinner) findViewById(R.id.spinner_pay_tag);
		//添加下拉框数据
		payType_list = new ArrayList<String>();
		payType_list.add("在线交易");
		payType_list.add("私下交易");
		
		
		//适配器
		payType_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,payType_list);
		//设置样式
		payType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		payTypeSpinner.setAdapter(payType_adapter);

		payTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//选中下拉框后设置类型
				payType_text= adapter.getItem(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	//---------------------------
	BaseAdapter orderListAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.activity_orders_view_listcell, null);
			} else {
				view = convertView;
			}

			// 设置数据，并获取
			//-----------
			//获取订单
	
			bookAvatar = (BookAvatarView) view.findViewById(R.id.book_avatar);
			bookTitle = (TextView) view.findViewById(R.id.book_title); // 图书标题
			bookAuthor = (TextView) view.findViewById(R.id.book_author);//图书作者
			bookSummary = (TextView) view.findViewById(R.id.text_about_book); //图书简介
			bookPrice = (TextView) view.findViewById(R.id.book_price);// 售价
			books_buy_numb = (TextView) view.findViewById(R.id.books_numb);//购买数量
			
			
			final OrderLists orders = list.get(position);
			//--
			//设置内容
			bookAvatar.load(Servelet.urlstring + orders.getId().getBook().getBookavatar());
			bookTitle.setText(orders.getId().getBook().getTitle());
			bookAuthor.setText(orders.getId().getBook().getAuthor());
			Log.d("bookAuthor11111----",bookAuthor.toString());
			bookSummary.setText(bookbus.getId().getBook().getSummary());
			bookPrice.setText(String.valueOf(orders.getId().getBook().getPrice()));
//			books_buy_numb.setText("x"+orders.getBooksAdded());
			
						

			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}
	};
	//-----------------------------
}
