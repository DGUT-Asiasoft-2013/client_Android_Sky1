package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.BooksContentActivity;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.fragment.widgets.Buy_book_bus_fragment;
import com.itcast.booksale.fragment.widgets.MainTabbarFragment;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment {
	//首页
	View booksView;
	List<Book> booksData;
	ListView bookListView;
	EditText keyword;//搜索关键字
	int page = 0;
	String keywords;
	//加载更多
	View btn_loadmore;
	TextView textLoadMore;
	int loadmoreSelect;//用于区分各个书单列表的加载更多

	//图书分类(标签)
	private Spinner bookTagSpinner;
	private List<String> bookTag_list;
	private ArrayAdapter<String> booksTag_adapter;
	String bookTag_text;

	//购物车页面
	Buy_book_bus_fragment bookbus=new Buy_book_bus_fragment();          
	User saler;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(booksView == null){
			booksView = inflater.inflate(R.layout.fragment_page_books_list, null);
			btn_loadmore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btn_loadmore.findViewById(R.id.text);

			bookListView = (ListView) booksView.findViewById(R.id.books_list);
			keyword = (EditText) booksView.findViewById(R.id.search_keyword);

			//图书分类
			bookTagSpinner = (Spinner) booksView.findViewById(R.id.spinner_book_tag_select);
			initData();//初始化



			//加载更多
			bookListView.addFooterView(btn_loadmore);
			btn_loadmore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//判断当前为哪一种列表
					switch (loadmoreSelect) {
					case 0:
						loadmoreBooksListByAll();
						break;
					case 1:
						loadmoreBooksListByKeyword();
						break;
					default:
						loadmoreBooksListByTag(bookTag_text);
						break;
					}


				}
			});

			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					goBookIntroduction(position);//跳转到书籍简介
				}

			});

			bookListView.setAdapter(bookListAdapter);


			//搜索功能(在OnResume中实现)
			//设置原始列表
			getBooksListByAll();//获取书籍数据
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
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());//书的封面



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


		//搜索按钮
		booksView.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchByKeyword();
				
			}
		});

		//分类按钮
		
		bookTagSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//选中下拉框后设置类型
				bookTag_text= adapter.getItem(position);
				if(bookTag_text.equals("全部")){
					getBooksListByAll();
				}else{
					getBooksListByTag(bookTag_text);
				}
				
				 try {
				        Field field =       AdapterView.class.getDeclaredField("mOldSelectedPosition");
				                field.setAccessible(true);  //设置mOldSelectedPosition可访问
				                field.setInt(bookTagSpinner, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
				    } catch (Exception e) {
				                e.printStackTrace();
				    }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
	}

	//生成得到所有书的连接
	void getBooksListByAll(){
		loadmoreSelect = 0;//0为所有书的加载更多
		Request request = Servelet.requestuildApi("books")
				.get()
				.build();
		reload(request);
	}

	//生成得到搜索书籍的书单
	void getBooksListByKeyword(){
		loadmoreSelect = 1;//1为搜索的加载更多	
		Request request = Servelet.requestuildApi("/book/s/"+keywords)
				.get()
				.build();
		reload(request);
	}

	//生成得到搜索书籍分类的书单
	void getBooksListByTag(String tag){
		loadmoreSelect = 2;//2为分类的加载更多
		bookTag_text = tag;
		Request request = Servelet.requestuildApi("/books/"+tag+"/class")
				.get()
				.build();
		reload(request);
	}

	//搜索关键字
	void searchByKeyword(){
		keywords = keyword.getText().toString();
		if(keywords.length() == 0){
			Toast.makeText(getActivity(),
					"请输入关键字", Toast.LENGTH_SHORT).show();
			return;
		}else{
			Editable edittext = keyword.getText();
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromInputMethod(keyword.getWindowToken(), 0);
			getBooksListByKeyword();

		}
	}


	//创建连接发送请求并传回信息
	void reload(Request request){


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

	//全部书单的加载更多
	void loadmoreBooksListByAll(){
		Request request = Servelet.requestuildApi("/books/"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//搜索列表的加载更多
	void loadmoreBooksListByKeyword(){
		Request request = Servelet.requestuildApi("/book/s/"+keywords+"/"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//生成得到搜索书籍分类的加载更多
	void loadmoreBooksListByTag(String tag){
		Request request = Servelet.requestuildApi("/books/"+tag+"/class/"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//加载更多
	void loadmore(Request request){
		btn_loadmore.setEnabled(false);
		textLoadMore.setText("努力加载中...");

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btn_loadmore.setEnabled(true);
						textLoadMore.setText("加载更多");

					}
				});

				try {
					Page<Book> books = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Book>>() {});
					if(books.getNumber()>page){
						if(booksData==null){
							booksData=books.getContent();
						}else{
							booksData.addAll(books.getContent());
						}
						page = books.getNumber();
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								bookListAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(getActivity())
							.setTitle("获取失败")
							.setMessage(e.getLocalizedMessage())
							.setPositiveButton("ok",null)
							.show();
						}
					});
				}


			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btn_loadmore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});
	}

	//图书分类
	public void initData(){
		//添加下拉框数据
		bookTag_list = new ArrayList<String>();
		bookTag_list.add("全部");
		bookTag_list.add("教科书");
		bookTag_list.add("文学");
		bookTag_list.add("童书");
		bookTag_list.add("艺术");
		bookTag_list.add("科技");
		bookTag_list.add("生活");
		bookTag_list.add("计算机");
		//适配器
		booksTag_adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,bookTag_list);
		//设置样式
		booksTag_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		bookTagSpinner.setAdapter(booksTag_adapter);

	}


}