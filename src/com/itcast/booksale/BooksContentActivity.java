package com.itcast.booksale;

import java.io.IOException;
import java.util.List;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Comment;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.Comment_Listfragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
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

/*
 * 书的详情界面
 */
public class BooksContentActivity extends Activity {
	List<Comment> comments;
	int page = 0;
	Comment_Listfragment fragComment;            //Comment_Listfragment是用于展示图书的评论的
	//	Article commentMess;
	private Book book;
	ListView commentListView;              //
	
	private Button btn_subscribe;             //订阅按钮
	private Button btn_massage;             //私信按钮
	private Button btn_comment;              //底部评论按钮
	
	private TextView bookUserName;          //卖书卖家姓名
	private TextView bookTitle;                 //图书标题
	private TextView bookUserPhone;           //卖家电话号码
	private TextView bookUserQQ;              //卖家qq
	private TextView bookUserText;            //卖家备注
	private TextView bookSummaryText;  
	
	private AvatarView bookUserAvatar;           //图书照片
	private boolean issubscribe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_view);
		
		
		//获得书本信息
		book = (Book) getIntent().getSerializableExtra("data");

		fragComment = (Comment_Listfragment) getFragmentManager().findFragmentById(R.id.show_comment_list);

		commentListView = (ListView) findViewById(R.id.comment_list);

		book = (Book) getIntent().getSerializableExtra("data");//获取Book

		
		initmethod();            //初始化
		
		bookUserName.setText(book.getUser().getName());
		bookTitle.setText(book.getTitle());
		bookUserPhone.setText(book.getUser().getPhoneNumb());
		bookUserQQ.setText(book.getUser().getQq());
		bookUserText.setText(book.getText());
		bookSummaryText.setText("   "+book.getSummary());
		
		//设置书籍id给评论用
		fragComment.setBookId(book.getId().toString());//(book.getId().toString());

		//评论
		btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goCommentActivity();//前往评论

			}
		});

		//订阅
		btn_subscribe.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goSubscribeActivity();//前往订阅
			}
		});

		//私信
		btn_massage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goMassageHim();//前往私信

			}
		});

	}
	
	//初始化
	public void initmethod() {
		 bookUserName = (TextView) findViewById(R.id.user_name);//卖家
		 bookTitle = (TextView) findViewById(R.id.book_title);//书名
		 bookUserPhone = (TextView) findViewById(R.id.book_user_phone);//卖家电话
		 bookUserQQ = (TextView) findViewById(R.id.book_user_qq);//卖家QQ
		 bookUserText = (TextView) findViewById(R.id.user_sell_text);//卖家留言
		 bookSummaryText = (TextView) findViewById(R.id.text_about_book);//内容简介
		
		 bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);//卖家头像
		 
		 btn_subscribe=(Button) findViewById(R.id.btn_subscribe);             //订阅按钮
		 btn_massage=(Button) findViewById(R.id.btn_massage);             //私信按钮
		 btn_comment=(Button) findViewById(R.id.btn_comment);           //底部评论按钮
	}


	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}

	void goCommentActivity(){
		

		Intent itnt = new Intent(this,CommentActivity.class);
		itnt.putExtra("data", book);                //把书的信息传给添加评论界面
		startActivity(itnt);
		finish();
	}

	void goSubscribeActivity(){
		//订阅的方法
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("subscribe", String.valueOf(!issubscribe))
				.build(); 

		Request request = Servelet.requestuildApi("saler/"+book.getUser().getName()+"/subscribe")
				.post(body).build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}
		});
	}
	void reload(){
		reloadSubscribe();
		checkLiked();
	}
	void reloadSubscribe(){
		Request request = Servelet.requestuildApi("saler/"+book.getUser().getName()+"/subscribe")
				.get().build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, Integer.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(count);
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(0);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onReloadLikesResult(0);
					}
				});
			}
		});
	}
	void onReloadLikesResult(int count){
		if(count>0){
			btn_subscribe.setText("订阅数("+count+")");
		}else{
			btn_subscribe.setText("订阅");
		}
	}
	void onCheckLikedResult(boolean result){
		issubscribe = result;
		btn_subscribe.setTextColor(result ? Color.RED : Color.BLACK);
	}
	void checkLiked(){
		Request request = Servelet.requestuildApi("saler/"+book.getUser().getName()+"/issubscribe").get().build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(result);
						}
					});
				}catch(final Exception e){
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckLikedResult(false);
					}
				});				
			}
		});
	}


	void goMassageHim(){
		Book book = (Book) getIntent().getSerializableExtra("data");
		User user = (User) book.getUser();//把user信息传过去
		Intent itnt = new Intent(this,HelloWorldActivity.class);            //!!!--------跳转到私信的活动页面
		itnt.putExtra("data", user);
		startActivity(itnt);
	}
}
