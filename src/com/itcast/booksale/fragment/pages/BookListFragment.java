package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.BooksContentActivity;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment {
	//首页
	View booksView;
	List<Book> booksData;
	ListView bookListView;
	int page = 0;
	Buy_book_bus_fragment bookbus=new Buy_book_bus_fragment();          //购物车页面
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(booksView == null){
			booksView = inflater.inflate(R.layout.fragment_page_books_list, null);

			bookListView = (ListView) booksView.findViewById(R.id.books_list);

			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					goBookIntroduction(position);//跳转到书籍简介
				}

			});

			bookListView.setAdapter(bookListAdapter);

		}
		return booksView;
	}

	//-------
	//创建适配器 BaseAdapter listAdapter
	//getView(重要),getItemID,getItem,getCount
	//Suppresslint("InflateParams")
	//在getView里面,取出视图 layoutInflater *= *.from(parent.getContext())
	//view = in...in(and.r.lay.simple_List_items,null)
	BaseAdapter bookListAdapter = new BaseAdapter() {


		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_cell_books_listcell, null);

			}else{
				view = convertView;
			}

			//设置数据，并获取

			TextView textDate = (TextView)view.findViewById(R.id.edit_date);//编写日期
			TextView bookCellTitle = (TextView)view.findViewById(R.id.cell_title);//书名
			TextView bookSummary = (TextView)view.findViewById(R.id.text_about_book);//作者
			TextView bookPrice = (TextView)view.findViewById(R.id.book_price);//售价
			Button xiangtao_btn=(Button) view.findViewById(R.id.book_purchase);
			xiangtao_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentTransaction ft=getFragmentManager().beginTransaction(); 
					MainTabbarFragment.chageFragment(new BookListFragment(), bookbus, ft);         //跳转页面
				}
				
			});
			BookAvatarView bookAvatar = (BookAvatarView)view.findViewById(R.id.book_avatar);//封面

			Book book = booksData.get(position);
			
			String list_createDate = DateFormat
					.format("yyyy-MM-dd hh:mm",
							booksData.get(position).getCreateDate()).toString();

			textDate.setText(list_createDate);
			bookCellTitle.setText(book.getTitle()+"--"+book.getAuthor());
			bookSummary.setText(book.getSummary());
			bookPrice.setText(book.getPrice()+" 元");
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());
			//书的封面暂不设

			
			


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

	//转到书本详情页面
	void goBookIntroduction(int position){
		Book book = booksData.get(position);

		Intent itnt = new Intent(getActivity(), BooksContentActivity.class);
		itnt.putExtra("data", book);             //传书的内容给BooksContentActivity

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();//获取书籍数据
	}

	//下载书
	void reload(){

		Request request = Servelet.requestuildApi("books")
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

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							BookListFragment.this.page = data.getNumber();//放进主线程进行，确保数据部位空
							BookListFragment.this.booksData = data.getContent();

							bookListAdapter.notifyDataSetInvalidated();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(e.getMessage())
						.show();
					}
				});

			}

		});
	}
}