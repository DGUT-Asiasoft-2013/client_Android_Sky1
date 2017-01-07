package com.itcast.booksale;
import java.util.ArrayList;
import java.util.List;

import com.itcast.booksale.R;
import com.itcast.booksale.R.id;
import com.itcast.booksale.R.layout;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BillDetailActivity  extends Activity{
	//	OrderLists data;
	List<Bookbus> data;
	LinearLayout add_bookList;//添加多本书籍
	LinearLayout bookList;//书籍的布局
	LayoutInflater inflater;

	//--------------------------
	AvatarView avatar_User;
	TextView text_userName;
	TextView textPayWay;
	TextView textPayNum;

	BookAvatarView bookAvatar; // 图书照片
	TextView bookTitle; // 图书标题
	TextView bookAuthor;//图书作者
	TextView bookSummary; //图书简介
	TextView bookPrice;// 售价
	TextView books_buy_numb;//购买数量
	String AllPay;
	String payType;
	//--------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_detail);
		inflater = LayoutInflater.from(this);

		//get
		if(data==null){
			data = (List<Bookbus>) getIntent().getSerializableExtra("order");
		}else{
			data = new ArrayList<Bookbus>();
		}
		
		//		data = (OrderLists) getIntent().getSerializableExtra("order");
		add_bookList = (LinearLayout) findViewById(R.id.lin_ii);
		AllPay = getIntent().getStringExtra("AllPay");
		payType = getIntent().getStringExtra("payType");
		initorders();

		for(int i=0;i<data.size();i++){
			addBookList();
			setorders(data.get(i));//设置父类信息
			setBooks(data.get(i));//设置书籍信息
		}
		//-----------------------------------------------------------------------------------------------------



	}

	//------------------------------------------------------------------------------------------------------

	void initorders(){
		//绑定各个组件
		avatar_User = (AvatarView) findViewById(R.id.bill_user_avatar);
		text_userName =  (TextView) findViewById(R.id.bill_user_name);
		textPayWay = (TextView) findViewById(R.id.bill_text_pay_tag);
		textPayNum  =(TextView) findViewById(R.id.bill_text_pay_num);
	}
	//------------
	void initBook(){
		//获取订单
		bookAvatar = (BookAvatarView) bookList.findViewById(R.id.book_avatar);
		bookTitle = (TextView) bookList.findViewById(R.id.book_title); // 图书标题
		bookAuthor = (TextView) bookList.findViewById(R.id.book_author);//图书作者
		bookSummary = (TextView) bookList.findViewById(R.id.text_about_book); //图书简介
		bookPrice = (TextView) bookList.findViewById(R.id.book_price);// 售价
		books_buy_numb = (TextView) bookList.findViewById(R.id.books_numb);//购买数量

	}
	void addBookList(){
		//---------------书籍布局
		bookList = (LinearLayout) inflater.inflate(R.layout.cell_order_list,null).findViewById(R.id.order_list_cell);
		initBook();// 初始化书籍布局
		//添加新的书籍布局
		add_bookList.addView(bookList);

	}

	void setorders(Bookbus order){
		//设置
		avatar_User.load(Servelet.urlstring+order.getId().getUser().getAvatar());
		text_userName.setText(order.getId().getUser().getName());
		textPayNum.setText(AllPay);
		textPayWay.setText(payType);
	}

	void setBooks(Bookbus order){
		//设置内容
		bookAvatar.load(Servelet.urlstring + order.getId().getBook().getBookavatar());
		bookTitle.setText(order.getId().getBook().getTitle());

		bookAuthor.setText(order.getId().getBook().getAuthor());
		bookSummary.setText(order.getId().getBook().getSummary());
		bookPrice.setText(String.valueOf(order.getId().getBook().getPrice()));
	}
	//监听返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			//do something...
			data.clear();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
