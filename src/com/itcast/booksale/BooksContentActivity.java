package com.itcast.booksale;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.itcast.booksale.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Comment;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 书的详情界面
 */
public class BooksContentActivity extends Activity {
	int page = 0;

	View view;
	private List<Comment> list = new ArrayList<Comment>();
	CommentAdapter adapter;

	private Book book;
	ListView commentListView; // 评论显示列表

	private Button btn_subscribe; // 订阅按钮
	private Button btn_massage; // 私信按钮
	private Button btn_comment; // 底部评论按钮

	private TextView bookUserName; // 卖书卖家姓名
	private TextView bookTitle; // 图书标题
	private TextView bookUserPhone; // 卖家电话号码
	private TextView bookUserQQ; // 卖家qq
	private TextView bookUserText; // 卖家备注
	private TextView bookSummaryText;

	private Boolean issubscribe;

	private AvatarView bookUserAvatar; // 图书卖家照片
	private BookAvatarView bookAvatar; // 图书照片

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_view);

		book = (Book) getIntent().getSerializableExtra("data");// 从BookListFragment获取Book

		commentListView = (ListView) findViewById(R.id.comment_listview);

		initmethod(); // 初始化

		bookUserName.setText(book.getUser().getName());
		bookTitle.setText(book.getTitle());
		bookUserPhone.setText(book.getUser().getPhoneNumb());
		bookUserQQ.setText(book.getUser().getQq());
		bookUserText.setText(book.getText());
		bookSummaryText.setText("   " + book.getSummary());

		bookUserAvatar.load(Servelet.urlstring + book.getUser().getAvatar());
		bookAvatar.load(Servelet.urlstring + book.getBookavatar());

		// 评论
		btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goCommentActivity();// 前往评论

			}
		});

		// 订阅
		btn_subscribe.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goSubscribeActivity();// 前往订阅
				/*if (btn_subscribe.isClickable()) {
					btn_subscribe.setText("已订阅");
					btn_subscribe.setTextColor(Color.RED);
					btn_subscribe.setBackgroundColor(Color.WHITE);
				}
				else {
					btn_subscribe.setText("订阅");
					btn_subscribe.setTextColor(Color.WHITE);
					btn_subscribe.setBackgroundColor(Color.RED);
				}*/
			}
		});

		// 私信
		btn_massage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goMassageHim();// 前往私信

			}
		});

		adapter = new CommentAdapter();
		commentListView.setAdapter(adapter);

	}

	// 初始化
	public void initmethod() {
		bookUserName = (TextView) findViewById(R.id.user_name);// 卖家
		bookTitle = (TextView) findViewById(R.id.book_title);// 书名
		bookUserPhone = (TextView) findViewById(R.id.book_user_phone);// 卖家电话
		bookUserQQ = (TextView) findViewById(R.id.book_user_qq);// 卖家QQ
		bookUserText = (TextView) findViewById(R.id.user_sell_text);// 卖家留言
		bookSummaryText = (TextView) findViewById(R.id.text_about_book);// 内容简介

		bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);// 卖家头像
		bookAvatar = (BookAvatarView) findViewById(R.id.book_avatar);// 图书照片

		btn_subscribe = (Button) findViewById(R.id.btn_subscribe); // 订阅按钮
		btn_massage = (Button) findViewById(R.id.btn_massage); // 私信按钮
		btn_comment = (Button) findViewById(R.id.btn_comment); // 底部评论按钮
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadComment(); // 调用下载评论方法
		reload();
	}

	/*
	 * 下面这个方法为下载评论
	 */
	public void loadComment() {
		//		OkHttpClient client=Servelet.getOkHttpClient();       //获得客户端
		//获得请求//---获取书的Id
		Request request=Servelet.requestuildApi("book/"+book.getId()+"/comment")
				.get()
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {

					//此为后台进行的，所以不能放在主线程里面进行
					String responseString = arg1.body().string();
					//获得page类的对象
					final Page<Comment> pageComment;

					final ObjectMapper objectMapper=new ObjectMapper();
					Log.d("loading feed list", responseString);
					//把解析下来的东西传入pageComment中
					pageComment=objectMapper.readValue(responseString, new TypeReference<Page<Comment>>() {});


					BooksContentActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//把解析下来的页数传给Comment_Listfragment
							page=pageComment.getNumber();
							//把内容传给list
							list=pageComment.getContent();
							//刷新
							adapter.notifyDataSetInvalidated();
						}
					});
				}catch (JsonParseException e) {
					e.printStackTrace();

				} catch (JsonMappingException e) {
					e.printStackTrace();

				}catch (final Exception e) {
					BooksContentActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {

							new AlertDialog.Builder(BooksContentActivity.this)
							.setTitle("失败ing")
							.setMessage(e.toString())
							.show();
						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				onFailure(arg0, arg1);
			}

		});}

	public void onFailure(Call arg0, final Exception arg1) {

		BooksContentActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BooksContentActivity.this).setTitle("失败").setMessage(arg1.toString()).show();

			}
		});

	}

	class CommentAdapter extends BaseAdapter {
		View commentview; // 创建Commentview

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
				commentview = inflater.inflate(R.layout.book_comment_content_list, null);
			} else {
				commentview = convertView;
			}
			TextView user_comment_name = (TextView) commentview.findViewById(R.id.user_comment_name);
			TextView user_comment_createtime = (TextView) commentview.findViewById(R.id.user_comment_createtime);
			TextView user_comment_content = (TextView) commentview.findViewById(R.id.user_comment_content_tv);
			// 获得下载的某个文章的某个评论
			Comment comment = list.get(position);
			// 设置用户名
			user_comment_name.setText(comment.getCommentor().getName());
			// 将Date类型的时间转换为string类型
			String date = (String) DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate());
			// 设置时间
			user_comment_createtime.setText(date);

			// 设置内容
			user_comment_content.setText(comment.getContent());

			return commentview;
		}

	}

	//go to the CommentActivity() which add the comment
	void goCommentActivity() {

		Intent itnt = new Intent(this, CommentActivity.class);
		itnt.putExtra("data", book); // 把书的信息传给添加评论界面
		startActivity(itnt);

	}

	void goSubscribeActivity() {
		// 订阅的方法
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("subscribe", String.valueOf(!issubscribe))
				.build();
		Request request = Servelet.requestuildApi("saler/"+book.getUser().getId()+"/subscribe")
				.post(body)
				.build();
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
						//						reload();
					}
				});
			}
		});
	}

	void reload() {
		reloadSubscribe();//返回多少个人订阅该卖家
		checkSubscribe();//检查你是否订阅过该卖家
	}

	void reloadSubscribe(){
		Request request = Servelet.requestuildApi("saler/"+book.getUser().getId()+"/subscribe")
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
							onReloadSubscribeResult(count);
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadSubscribeResult(0);
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
						onReloadSubscribeResult(0);
					}
				});
			}

		});}

	void onReloadSubscribeResult(int count) {
		if (count > 0) {
			btn_subscribe.setText("订阅数(" + count + ")");
			btn_subscribe.setTextColor(Color.RED);
			btn_subscribe.setTextSize(16);
		} else {
			btn_subscribe.setText("订阅");
			btn_subscribe.setTextColor(Color.WHITE);
//			btn_subscribe.setBackgroundColor(Color.RED);
//			btn_subscribe.setTextSize(13);
		}
	}

	void onCheckSubscribeResult(boolean result) {
		issubscribe = result;
		btn_subscribe.setTextColor(result ? Color.RED : Color.WHITE);
	}

	void checkSubscribe(){
		Request request = Servelet.requestuildApi("saler/"+book.getUser().getId()+"/issubscribe").get().build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckSubscribeResult(result);
						}
					});
				}catch (JsonParseException e) {
					e.printStackTrace();

				} catch (JsonMappingException e) {
					e.printStackTrace();

				}catch(final Exception e){
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckSubscribeResult(false);
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
						onCheckSubscribeResult(false);
					}
				});
			}

		});}

	void goMassageHim() {
		Book book = (Book) getIntent().getSerializableExtra("data");
		User user = (User) book.getUser();// 把user信息传过去
		//Log.d("user", user.getAccount());
		Intent itnt = new Intent(this, SendPrivateMessageActivity.class); // !!!--------跳转到私信的活动页面

		itnt.putExtra("sendToReceiver", user);
		startActivity(itnt);
	}
}
