package com.itcast.booksale;

import java.io.IOException;
import java.util.List;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Comment;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.Comment_Listfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class BooksContentActivity extends Activity {

	List<Comment> comments;
	int page = 0;
	Comment_Listfragment fragComment;
	//	Article commentMess;
	private Book book;
	ListView commentListView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);



		setContentView(R.layout.activity_books_view);

		fragComment = (Comment_Listfragment) getFragmentManager().findFragmentById(R.id.show_comment_list);

		commentListView = (ListView) findViewById(R.id.comment_list);

		book = (Book) getIntent().getSerializableExtra("data");//获取Book

		TextView bookUserName = (TextView) findViewById(R.id.user_name);//卖家
		TextView bookTitle = (TextView) findViewById(R.id.book_title);//书名
		TextView bookUserPhone = (TextView) findViewById(R.id.book_user_phone);//卖家电话
		TextView bookUserQQ = (TextView) findViewById(R.id.book_user_qq);//卖家QQ
		TextView bookUserText = (TextView) findViewById(R.id.user_sell_text);//卖家留言
		TextView bookSummaryText = (TextView) findViewById(R.id.text_about_book);//内容简介
		
		AvatarView bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);//卖家头像
		
		
		bookUserName.setText(book.getUser().getName());
		bookTitle.setText(book.getTitle());
		bookUserPhone.setText(book.getUser().getPhoneNumb());
		bookUserQQ.setText(book.getUser().getQq());
		bookUserText.setText(book.getText());
		bookSummaryText.setText("   "+book.getSummary());
		
		//设置书籍id给评论用
		fragComment.setBookId(book.getId().toString());//(book.getId().toString());

		//评论
		findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goCommentActivity();//前往评论

			}
		});

		//订阅
		findViewById(R.id.btn_subscribe).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goSubscribeActivity();//前往订阅
			}
		});

		//私信
		findViewById(R.id.btn_massage).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goMassageHim();//前往私信

			}
		});

	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	void goCommentActivity(){
		//把书本信息传到评论类
		Book book = (Book) getIntent().getSerializableExtra("data");

		Intent itnt = new Intent(this,CommentActivity.class);
		itnt.putExtra("data", book);
		startActivity(itnt);
		finish();
	}

	void goSubscribeActivity(){
		//订阅的方法
	}

	void goMassageHim(){
		Book book = (Book) getIntent().getSerializableExtra("data");
		User user = (User) book.getUser();//把user信息传过去
		Intent itnt = new Intent(this,SendPrivateMessage.class);
		itnt.putExtra("data", user);
		startActivity(itnt);
	}
}
