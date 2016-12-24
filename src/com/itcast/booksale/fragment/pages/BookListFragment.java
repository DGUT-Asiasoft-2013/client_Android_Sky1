package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.BooksContentActivity;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment {
	// 首页
	View booksView;
	List<Book> booksData;
	ListView bookListView;
	EditText keyword;// 搜索关键字
	int page = 0;
	String keywords;

//	Buy_book_bus_fragment bookbus = new Buy_book_bus_fragment(); // 购物车页面
	User saler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (booksView == null) {
			booksView = inflater.inflate(R.layout.fragment_page_books_list, null);

			bookListView = (ListView) booksView.findViewById(R.id.books_list);
			keyword = (EditText) booksView.findViewById(R.id.search_keyword);

			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					goBookIntroduction(position);// 跳转到书籍简介
				}

			});

			bookListView.setAdapter(bookListAdapter);

			// 搜索功能(在OnResume中实现)
			// 设置原始列表
			getBooksListByAll();// 获取书籍数据
		}
		return booksView;
	}

	// -------
	// 创建适配器 BaseAdapter listAdapter
	// getView(重要),getItemID,getItem,getCount
	// Suppresslint("InflateParams")
	// 在getView里面,取出视图 layoutInflater *= *.from(parent.getContext())
	// view = in...in(and.r.lay.simple_List_items,null)
	BaseAdapter bookListAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_cell_books_listcell, null);

			} else {
				view = convertView;
			}

			// 设置数据，并获取

			TextView textDate = (TextView) view.findViewById(R.id.edit_date);// 编写日期
			TextView bookCellTitle = (TextView) view.findViewById(R.id.cell_title);// 书名
			TextView bookSummary = (TextView) view.findViewById(R.id.text_about_book);// 作者
			TextView bookPrice = (TextView) view.findViewById(R.id.book_price);// 售价
			Button xiangtao_btn = (Button) view.findViewById(R.id.book_purchase);
			final Book book = booksData.get(position);
			// add ClickListener for the xiangtao_btn
			xiangtao_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					addBookToBookBus(book.getId());

				}

			});
			BookAvatarView bookAvatar = (BookAvatarView) view.findViewById(R.id.book_avatar);// 封面

			String list_createDate = DateFormat.format("yyyy-MM-dd hh:mm", booksData.get(position).getCreateDate())
					.toString();

			textDate.setText(list_createDate);
			bookCellTitle.setText(book.getTitle() + "--" + book.getAuthor());
			bookSummary.setText(book.getSummary());
			bookPrice.setText(book.getPrice() + " 元");
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());// 书的封面

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
			return booksData == null ? 0 : booksData.size();
		}
	};
	// it is to baioji is add to book bus
	private boolean isaddtobookbus;

	/**
	 * 上传购物车信息
	 */
	public void addBookToBookBus(int bookid) {
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("isAddBookToBus", String.valueOf(!isaddtobookbus)).build();

		Request request = Servelet.requestuildApi("book/" + bookid + "/bookbus").post(body).build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String string = arg1.body().string();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								new AlertDialog.Builder(getActivity()).setTitle("success to add bookbus")
										.setMessage(string).setPositiveButton("ok", null).show();

							}
						});
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								new AlertDialog.Builder(getActivity()).setTitle("success to add bookbus")
										.setMessage(arg1.toString())
										.setPositiveButton("ok", null)
										.show();

							}
						});
					}
				});
			}
		});
	}

	// 转到书本详情页面
	void goBookIntroduction(int position) {
		Book book = booksData.get(position);

		Intent itnt = new Intent(getActivity(), BooksContentActivity.class);
		itnt.putExtra("data", book); // 传书的内容给BooksContentActivity

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();

		booksView.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchByKeyword();

			}
		});

	}

	// 生成得到所有书的连接
	void getBooksListByAll() {
		Request request = Servelet.requestuildApi("books").get().build();
		reload(request);
	}

	// 生成得到搜索书籍的书单
	void getBooksListByKeyword() {
		Request request = Servelet.requestuildApi("/book/s/" + keywords).get().build();
		reload(request);
	}

	void searchByKeyword() {
		keywords = keyword.getText().toString();
		if (keywords.length() == 0) {
			Toast.makeText(getActivity(), "请输入关键字", Toast.LENGTH_LONG);
			getBooksListByAll();
		} else {
			Editable edittext = keyword.getText();
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromInputMethod(keyword.getWindowToken(), 0);
			getBooksListByKeyword();

		}
	}

	void reload(Request request) {

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String value = arg1.body().string();
					final Page<Book> data = new ObjectMapper().readValue(value, new TypeReference<Page<Book>>() {
					});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							BookListFragment.this.page = data.getNumber();// 放进主线程进行，确保数据部位空
							BookListFragment.this.booksData = data.getContent();

							bookListAdapter.notifyDataSetInvalidated();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
					}
				});

			}

		});
	}
}