package com.itcast.booksale;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SubscribeListBookActivity extends  Activity{
	//首页
	View booksView;
	List<Book> booksData;
	ListView bookListView;
	int page = 0;

	Buy_book_bus_fragment bookbus=new Buy_book_bus_fragment();          //购物车页面
	User saler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_page_subscribe_book_list);
		saler = (User) getIntent().getSerializableExtra("data");
		if(booksView == null){
			//				booksView = (View)findViewById(R.layout.fragment_page_subscribe_book_list);
			bookListView = (ListView)findViewById(R.id.list_book);

			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					goBookIntroduction(position);//跳转到书籍简介
				}
			});
			bookListView.setAdapter(bookListAdapter);
		}

	}
	BaseAdapter bookListAdapter = new BaseAdapter() {


		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_cell_books_listcell_step2, null);

			}else{
				view = convertView;
			}


			// 设置数据，并获取
			//bookUser
			AvatarView avatar = (AvatarView) view.findViewById(R.id.user_avatar);//卖家头像
			TextView userName = (TextView) view.findViewById(R.id.user_name);//卖家昵称
			TextView textDate = (TextView) view.findViewById(R.id.edit_date);// 编写日期
			//bookBody
			BookAvatarView bookAvatar = (BookAvatarView) view.findViewById(R.id.book_avatar);// 封面
			TextView bookCellTitle = (TextView) view.findViewById(R.id.book_title);// 书名
			TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);// 作者
			TextView bookSummary = (TextView) view.findViewById(R.id.text_about_book);//简介
			TextView bookPrice = (TextView) view.findViewById(R.id.book_price);// 售价
			//bookOther
			final TextView fans_btn = (TextView) view.findViewById(R.id.tv_fans);//关注量
			TextView comments_btn = (TextView) view.findViewById(R.id.tv_comments);//评论


			final Book book = booksData.get(position);

			// add ClickListener for the xiangtao_btn
			String countOfLikes = "saler/"+book.getUser().getId()+"/subscribe";
			String countOfComments = "/count/"+book.getId()+"/comments";
			getCountOfSomething(book,fans_btn,countOfLikes,0);
			getCountOfSomething(book, comments_btn, countOfComments,1);

			//bookuser
			avatar.load(book.getUser());
			userName.setText(book.getUser().getName());
			String list_createDate = DateFormat.format("yyyy-MM-dd hh:mm", booksData.get(position).getCreateDate())
					.toString();

			textDate.setText(list_createDate);
			//bookbody
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());//书的封面
			bookCellTitle.setText(book.getTitle());
			bookAuthor.setText(book.getAuthor());
			bookSummary.setText(book.getSummary());
			bookPrice.setText(book.getPrice()+" 元");

			//--------------------------------------------


			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return booksData.get(position);
		}

		@Override
		public int getCount() {
			return booksData==null ? 0 : booksData.size();
		}
	};

	//----------------显示关注量和评论数
	void getCountOfSomething(Book book,final TextView textView,String url,final int some){
		Request request = Servelet.requestuildApi(url)
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
							if(some == 0){
								textView.setText("♥"+count);
							}else{
								textView.setText("评论("+count+")");
							}
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(some == 0){
								textView.setText("♥");
							}else{
								textView.setText("评论("+0+")");
							}
							
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

					}
				});
			}

		});
	}
	//----------------

	//转到书本详情页面
	void goBookIntroduction(int position){
		Book book = booksData.get(position);

		Intent itnt = new Intent(SubscribeListBookActivity.this, BooksContentActivity.class);
		itnt.putExtra("data", book);             //传书的内容给BooksContentActivity

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	void reload(){
		Request request = Servelet.requestuildApi("/book/s/"+saler.getName())
				.get()
				.build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String value = arg1.body().string();
					final Page<Book> data = new ObjectMapper()
							.readValue(value,
									new TypeReference<Page<Book>>() {});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SubscribeListBookActivity.this.page = data.getNumber();//放进主线程进行，确保数据部位空
							SubscribeListBookActivity.this.booksData = data.getContent();
							bookListAdapter.notifyDataSetInvalidated();

						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(null)
							.setMessage(e.getMessage())
							.show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(null)
						.setMessage(e.getMessage())
						.show();
					}
				});

			}

		});
	}

}
