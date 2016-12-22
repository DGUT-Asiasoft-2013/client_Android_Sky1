package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加卖书的信息
 * @author Administrator
 *
 */
public class ShareBooksActivity extends Activity{

	//图书信息(8个)
	EditText editBookTitle,editBookAuthor
	,editPrice,editBookPublisher,editISBN
	,editTag,editBookSummary,editText,editBookNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_share_books);
		
		editBookTitle = (EditText) findViewById(R.id.input_book_title);//图书名称
		editBookAuthor = (EditText) findViewById(R.id.input_book_author);//图书作者
		editPrice = (EditText) findViewById(R.id.input_book_price);//出售价格
		editBookPublisher = (EditText) findViewById(R.id.input_book_publisher);//出版社
		editTag = (EditText) findViewById(R.id.input_book_tag);//图书标签
		editISBN = (EditText) findViewById(R.id.input_book_isbn);//ISBN
		editBookSummary = (EditText) findViewById(R.id.input_book_summary);//图书摘要
		editText = (EditText) findViewById(R.id.input_user_text);//卖家留言
		editBookNumber=(EditText) findViewById(R.id.input_book_number);

	    //确定出售
		findViewById(R.id.btn_share_book).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将信息存进数据库
				sendContent();
			}
		});
	}
	
	void sendContent(){
		
		//获取图书信息
		String bookTitle = editBookTitle.getText().toString();
		String bookAuthor = editBookAuthor.getText().toString();
		String bookPrice = editPrice.getText().toString();
		String bookPublisher = editBookPublisher.getText().toString();
		String bookTag = editTag.getText().toString();
		String bookSummary = editBookSummary.getText().toString();
		String text = editText.getText().toString();
		String ISBN = editISBN.getText().toString();
		String booknumber=editBookNumber.getText().toString();
		  
		//下面的addFormDataPart("title", bookTitle)左边的title应该跟服务器的一样，记住
		MultipartBody bookBody = new MultipartBody.Builder()
				.addFormDataPart("title", bookTitle)
				.addFormDataPart("author", bookAuthor)
				.addFormDataPart("price", bookPrice)
				.addFormDataPart("publisher", bookPublisher)
				.addFormDataPart("summary", bookSummary)
				.addFormDataPart("tag", bookTag)
				.addFormDataPart("book_isbn", ISBN) 
				.addFormDataPart("text", text)
				.addFormDataPart("booknumber", booknumber)
				.build();
		
		Request request = Servelet.requestuildApi("sellbooks")
				.method("post", null)
				.post(bookBody)
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						ShareBooksActivity.this.onSucceed(responseBody);
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						ShareBooksActivity.this.onFailure(arg1);
					}
				});
				
			}
		});
	}
	
	void onSucceed(final String text){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(ShareBooksActivity.this)
				.setTitle("连接成功，提交")
				.setMessage(text)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//返回主界面
						goMainView();
					}
				}).show();
			}
		});
		
	}
	
	void onFailure(final Exception e){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(ShareBooksActivity.this)
				.setTitle("连接，提交失败")
				.setMessage(e.getMessage())
				.show();
			}
		});
		
	}
	void goMainView(){


		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);

		finish();

	}
			
}
