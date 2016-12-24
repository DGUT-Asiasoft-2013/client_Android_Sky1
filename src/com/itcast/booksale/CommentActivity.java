package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.R;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 此为用户添加评论的页面
 * 
 * @author Administrator
 *
 */
public class CommentActivity extends Activity {

	Book book;
	
	private EditText add_comment_text; // 用户添加的评论
	private Button btn_comment_commit;           //提交按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book_comment);
		init();
		//接收BooksContentActivity传过来的book信息
		book=(Book) getIntent().getSerializableExtra("data");          
		btn_comment_commit.setOnClickListener(new CommitClickListener());
	}

	public void init() {
		add_comment_text = (EditText) findViewById(R.id.add_comment_content);
		btn_comment_commit = (Button) findViewById(R.id.add_btn_comment);

	}

	/*
	 * 提交按钮的监听器
	 */
	class CommitClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AddComment();
			
		}

	}

	public void AddComment() {
		String comment_text = add_comment_text.getText().toString();

		// 这里addFormDataPart()的第一个参数需要跟服务器的一样
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("content", comment_text).build();

		//发起请求
		Request request = Servelet.requestuildApi("/book/" + book.getId() + "/comment").method("post", null)//获取书的id
				.post(body).build();

		// 客户端连接
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {

				try {


					String ar = arg1.body().string();// String ar = arg1.body().string();为后台运行的，不能再前台运行，所以把它放在前面来

					CommentActivity.this.onResponse(arg0, ar);
					
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(CommentActivity.this).setTitle("失败").setMessage(e.toString())
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											add_comment_text.setText("");
										}
									}).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				CommentActivity.this.onFailure(arg0, arg1);
			}
		});
	}

	public void onResponse(Call arg0, final String arg1) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(CommentActivity.this).setTitle("提交成功").setMessage(arg1.toString())
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								add_comment_text.setText("");
//								Intent intent=new Intent(CommentActivity.this, BooksContentActivity.class);
//								startActivity(intent);
								finish();
							}
						}).show();
				
				
			}
		});

	}

	public void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this).setTitle("评论提交失败").setMessage(arg1.getMessage())
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						add_comment_text.setText("");
					}
				}).show();
	}

}
