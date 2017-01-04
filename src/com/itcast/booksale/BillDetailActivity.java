package com.itcast.booksale;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BillDetailActivity  extends Activity{
	OrderLists data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_detail);
		data = (OrderLists) getIntent().getSerializableExtra("order");
		
		//绑定各个组件
		AvatarView avatar_User = (AvatarView) findViewById(R.id.bill_user_avatar);
		TextView text_userName =  (TextView) findViewById(R.id.bill_user_name);
		BookAvatarView avatarBookAuthor = (BookAvatarView) findViewById(R.id.bill_book_avatar);
		TextView textAboutBook = (TextView) findViewById(R.id.bill_text_about_book);
		TextView textPayWay = (TextView) findViewById(R.id.bill_text_pay_tag);
		TextView textPayNum  =(TextView) findViewById(R.id.bill_text_pay_num);
		
		
		//为他们赋值
		avatar_User.load(data.getBook().getUser());
		text_userName.setText(data.getBook().getUser().getAccount());
		avatarBookAuthor.load(Servelet.urlstring+data.getBook().getBookavatar());
		textAboutBook.setText(data.getBook().getSummary());
		textPayWay.setText(data.getPayway());
		textPayNum.setText(data.getPayMoney());
	}

}
