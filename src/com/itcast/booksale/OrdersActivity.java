package com.itcast.booksale;

import java.util.List;

import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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





	}

	void initorders() {
		bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);
		bookUserName = (TextView) findViewById(R.id.user_name);
		momey_all = (TextView) findViewById(R.id.momey_all);
		btn_order = (TextView) findViewById(R.id.btn_order);
	}
	//---------------------------
	class OrdersAdapter extends BaseAdapter {
		View orderslistview; // 创建orderslistview

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// 如果convertView为空，则为listview设置
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				orderslistview = inflater.inflate(R.layout.activity_orders_view_listcell, null);
			} else {
				orderslistview = convertView;
			}
			
			//获取订单
			OrderLists orders = list.get(position);
			bookAvatar = (BookAvatarView) orderslistview.findViewById(R.id.book_avatar);
			bookTitle = (TextView) orderslistview.findViewById(R.id.book_title); // 图书标题
			bookAuthor = (TextView) orderslistview.findViewById(R.id.book_author);//图书作者
			bookSummary = (TextView) orderslistview.findViewById(R.id.text_about_book); //图书简介
			bookPrice = (TextView) orderslistview.findViewById(R.id.book_price);// 售价
			books_buy_numb = (TextView) orderslistview.findViewById(R.id.books_numb);//购买数量
			
			//设置内容
			bookAvatar.load(Servelet.urlstring + orders.getId().getBook().getBookavatar());
			bookTitle.setText(orders.getId().getBook().getTitle());
			bookAuthor.setText(orders.getId().getBook().getAuthor());
			bookSummary.setText(orders.getId().getBook().getSummary());
			bookPrice.setText(String.valueOf(orders.getId().getBook().getPrice()));
			books_buy_numb.setText("x"+orders.getBooksAdded());
			
			
			return orderslistview;
		}

	}

	//-----------------------------
}
