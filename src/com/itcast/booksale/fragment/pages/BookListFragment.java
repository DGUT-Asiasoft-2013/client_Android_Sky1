package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.BooksContentActivity;
import com.itcast.booksale.R;
import com.itcast.booksale.effects.MyListener;
import com.itcast.booksale.effects.PullToRefreshLayout;
import com.itcast.booksale.effects.PullableListView;
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
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment implements OnClickListener {
	// 首页
	View booksView,headerView;
	List<Book> booksData;
	//	ListView bookListView;
	PullableListView bookListView;
	EditText keyword;// 搜索关键字
	int page = 0;
	String keywords;


	//加载更多
	//	View btn_loadmore;//可以不要了
	//	TextView textLoadMore;
	int loadmoreSelect;//用于区分各个书单列表的加载更多
	String bookTag_text;//类型

	private LinearLayout layout_book_all;
	private LinearLayout all_book_layout;
	private LinearLayout textbook_layout;
	private LinearLayout literature_layout;
	private LinearLayout story_book_layout;
	private LinearLayout art_layout;
	private LinearLayout science_layout;
	private LinearLayout life_layout;
	private LinearLayout computer_layout;
	//--------
	//上拉刷新和下拉加载
	PullToRefreshLayout pullToRefresh;
	//----------


	User saler;


	private Integer[] images;
	private String[] titles;
	private ArrayList<ImageView> imageviews;
	private ArrayList<View> dots;
	private TextView title;
	private ViewPager viewpager;
	private int currentItem = 0; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (booksView == null) {
			booksView = inflater.inflate(R.layout.fragment_page_books_list, null);

			layout_book_all=(LinearLayout) booksView.findViewById(R.id.layout_book_all);
			bookListView = (PullableListView) booksView.findViewById(R.id.content_view);
			headerView = inflater.inflate(R.layout.headerview, null);
			keyword = (EditText) booksView.findViewById(R.id.search_keyword);
			keywords =keyword.getText().toString();
			bookTag_text = getResources().getString(R.string.all);//default默认为全部
			
			//book classification
			all_book_layout = (LinearLayout) headerView.findViewById(R.id.all_book_layout);
			all_book_layout.setOnClickListener(this);
			
			textbook_layout = (LinearLayout) headerView.findViewById(R.id.textbook_layout);
			textbook_layout.setOnClickListener(this);
			
			literature_layout = (LinearLayout) headerView.findViewById(R.id.literature_layouts);
			literature_layout.setOnClickListener(this);
			
			story_book_layout = (LinearLayout) headerView.findViewById(R.id.story_book_layout);
			story_book_layout.setOnClickListener(this);
			
			art_layout = (LinearLayout) headerView.findViewById(R.id.art_layout);
			art_layout.setOnClickListener(this);
			
			science_layout = (LinearLayout) headerView.findViewById(R.id.science_layout);
			science_layout.setOnClickListener(this);
			
			life_layout = (LinearLayout) headerView.findViewById(R.id.life_layout);
			life_layout.setOnClickListener(this);
			
			computer_layout = (LinearLayout) headerView.findViewById(R.id.computer_layout);
			computer_layout.setOnClickListener(this);

			bookListView.addHeaderView(headerView);

			//-----------------------------==========================================
			//设置上拉和下拉的监听
			pullToRefresh =(PullToRefreshLayout) booksView.findViewById(R.id.refresh_view);
			pullToRefresh.setOnRefreshListener(new MyListener(){
				//刷新后的操作
				@Override
				public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
					super.onRefresh(pullToRefreshLayout);
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					getBooksListByAll();
				}
				//下拉加载后的操作
				@Override
				public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
					super.onLoadMore(pullToRefreshLayout);
					switch (loadmoreSelect) {
					case 0:
						loadmoreBooksListByAll();
						break;
					case 1:
						loadmoreBooksListByKeyword();
						break;
					case 2:
						loadmoreBooksListByTag(bookTag_text);
						break;
					default:
						loadmoreBookListByKeywordAndType();
						break;
					}
					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}

			});


			//---------------------------=============================================


			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					goBookIntroduction(position);
				}

			});

			bookListView.setAdapter(bookListAdapter);

			// 搜索功能(在OnResume中实现)
			// 设置原始列表
			getBooksListByAll();// 获取书籍数据

			images = new Integer[] { R.drawable.a, R.drawable.b, R.drawable.c,
					R.drawable.d, R.drawable.e };
			titles = new String[] { 
					getResources().getString(R.string.booksmell),
					getResources().getString(R.string.oldsale),
					getResources().getString(R.string.library), 
					getResources().getString(R.string.smellbook),
					getResources().getString(R.string.lookbook) };
			// 为了更好的使用这些信息，可以创建一个集合
			imageviews = new ArrayList<ImageView>();
			for (int i = 0; i < images.length; i++) {
				ImageView iv = new ImageView(getActivity());
				iv.setBackgroundResource(images[i]);
				imageviews.add(iv);
			}   

			// 将五个点放入到集合中
			dots = new ArrayList<View>();
			dots.add(booksView.findViewById(R.id.dop_0));
			dots.add(booksView.findViewById(R.id.dop_1));
			dots.add(booksView.findViewById(R.id.dop_2));
			dots.add(booksView.findViewById(R.id.dop_3));
			dots.add(booksView.findViewById(R.id.dop_4));
			title = (TextView) booksView.findViewById(R.id.textview);
			// 设置默认的显示内容
			title.setText(titles[0]);
			viewpager = (ViewPager) booksView.findViewById(R.id.viewpager);
			// viewpager中的图片也是需要经过适配进去的
			MyAdapter adapter = new MyAdapter();
			viewpager.setAdapter(adapter);

			//添加监听事件(页面改变事件)
			viewpager.setOnPageChangeListener(new OnPageChangeListener() {

				//记录原先点的位置
				int oldposition = 0;

				//1-->2    1页面出去的时候启动的方法
				@Override
				public void onPageSelected(int position) {
					title.setText(titles[position]);
					dots.get(position).setBackgroundResource(R.drawable.dot_focused);
					dots.get(oldposition).setBackgroundResource(R.drawable.dot_normal); //原先点的改成失去焦点
					oldposition = position;
					currentItem = position;
				}
				//1 -->2  2页面进来后启动的方法 
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}
				//页面滑动时调用的方法 
				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}
		return booksView;
	}
	

	
	public void onClick(View v) {
		keywords = keyword.getText().toString();
		switch (v.getId()) {
		case R.id.all_book_layout:
			bookTag_text = getResources().getString(R.string.all);
			allBookLayoutClick();
			break;
			
		case R.id.textbook_layout:
			bookTag_text = getResources().getString(R.string.textbook);
			TextBooklayoutClick();
			break;
			
		case R.id.literature_layouts:
			bookTag_text = getResources().getString(R.string.literature);
			LiteraturelayoutClick();
			break;
			
		case R.id.story_book_layout:
			bookTag_text = getResources().getString(R.string.childrenbooks);
			StoryBookLayoutClick();
			break;
			
		case R.id.art_layout:
			bookTag_text = getResources().getString(R.string.art);
			ArtLayoutClick();
			break;
			
		case R.id.science_layout:
			bookTag_text = getResources().getString(R.string.science);
			ScienceLayoutClick();
			break;
			
		case R.id.life_layout:
			bookTag_text = getResources().getString(R.string.life);
			LifeLayoutClick();
			break;
			
		case R.id.computer_layout:
			bookTag_text = getResources().getString(R.string.computer);
			ComputerLayoutClick();
			break;

		default:
			break;
		}
	}

	/*
	 * computer_layout's ClickListener
	 */
	public void ComputerLayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.computer));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.computer));
		}
	}

	/*
	 * life_layout's ClickListener
	 */
	public void LifeLayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.life));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.life));
		}
	}

	/*
	 * science_layout's ClickListener
	 */
	public void ScienceLayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.science));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.science));
		}
	}

	/*
	 * art_layout's ClickListener
	 */
	public void ArtLayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.art));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.art));
		}
	}

	/*
	 * story_book_layout's ClickListener
	 */
	public void StoryBookLayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.childrenbooks));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.childrenbooks));
		}
	}

	/*
	 * literature_layouts's ClickListener
	 */
	public void LiteraturelayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.literature));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.literature));
		}
	}

	/*
	 * textbook_layout's ClickListener
	 */
	public void TextBooklayoutClick() {
		if (keywords.length()==0) {
			getBooksListByTag(getResources().getString(R.string.textbook));
			
		}
		else {
			getBookListByKeywordAndType(keywords,getResources().getString(R.string.textbook));
		}
	}

	/*
	 * all_book_layout's ClickListener
	 */
	public void allBookLayoutClick() {
		if (keywords.length()==0) {
			
			getBooksListByAll();
		}
		else if (keywords.length()!=0) {
			getBooksListByKeyword();
		}
	}

	//适配器
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
			BookAvatarView bookAvatar = (BookAvatarView) view.findViewById(R.id.book_avatar);// 封面
			TextView bookCellTitle = (TextView) view.findViewById(R.id.book_title);// 书名
			TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);// 作者
			TextView bookSummary = (TextView) view.findViewById(R.id.text_about_book);// 简介
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
			String list_createDate = DateFormat.format("yyyy-MM-dd hh:mm", booksData.get(position).getCreateDate())
					.toString();

			textDate.setText(list_createDate);
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());
			//截取书名
			if(book.getTitle().length()>12){
				bookCellTitle.setText(book.getTitle().subSequence(0, 12) + "...");
			}else{
				bookCellTitle.setText(book.getTitle());
			}			
			bookAuthor.setText(book.getAuthor());
			//截取简介
			if(book.getSummary().length()>20){
				bookSummary.setText(book.getSummary().subSequence(0, 20) + "......");
			}else{
				bookSummary.setText(book.getSummary());
			}
			
			bookPrice.setText(book.getPrice()+getResources().getString(R.string.yuan));


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
				try {

					final String string = arg1.body().string();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							/*new AlertDialog
							.Builder(getActivity())
							.setTitle("success to add bookbus")
							.setMessage(string)
							.setPositiveButton("ok", null)
							.show();*/
							Toast.makeText(getActivity(), "success to add bookbus", Toast.LENGTH_SHORT).show();

						}
					});
				} catch (Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(), R.string.failtopull, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								new AlertDialog.Builder(getActivity()).setTitle("failed to add bookbus")
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
		Book book = booksData.get(position-1);

		Intent itnt = new Intent(getActivity(), BooksContentActivity.class);
		itnt.putExtra("data", book);  // 传书的内容给BooksContentActivity

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();

		// 搜索按钮
		booksView.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchByKeyword();
			}
		});


	}

	// 生成得到所有书的连接
	void getBooksListByAll() {
		loadmoreSelect = 0;// 0为所有书的加载更多
		Request request = Servelet.requestuildApi("books").get().build();

		reload(request);
	}

	// 生成得到搜索书籍的书单
	void getBooksListByKeyword() {
		loadmoreSelect = 1;// 1为搜索的加载更多
		// 判断类型有无选择
		if (bookTag_text.equals(getResources().getString(R.string.all))) {
			Request request = Servelet.requestuildApi("/book/s/" + keywords).get().build();

			reload(request);
		} else {
			getBookListByKeywordAndType(keywords, bookTag_text);
		}

	}


	//生成得到搜索书籍分类的书单
	void getBooksListByTag(String tag){
		loadmoreSelect = 2;//2为分类的加载更多
		bookTag_text = tag;

		Request request = Servelet.requestuildApi("/books/" + tag + "/class").get().build();
		reload(request);
	}

	// 生成得到搜索书籍并带有分类的书单
	void getBookListByKeywordAndType(String key, String tag) {
		loadmoreSelect = 3;// 3为搜索加分类的加载更多
		keywords = key;
		bookTag_text = tag;
		Request request = Servelet.requestuildApi("/books/" + keywords + "/and/" + bookTag_text + "/class").get()
				.build();

		reload(request);
	}

	// 搜索关键字
	void searchByKeyword() {
		keywords = keyword.getText().toString();
		if (keywords.length() == 0) {
			Toast.makeText(getActivity(), R.string.input, Toast.LENGTH_SHORT).show();
			return;
		} else {

			Editable edittext = keyword.getText();
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromInputMethod(keyword.getWindowToken(), 0);
			getBooksListByKeyword();

		}
	}

	// 创建连接发送请求并传回信息
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
							BookListFragment.this.page = data.getNumber();//进入主线程传数据，确保数据不为空
							BookListFragment.this.booksData = data.getContent();

							bookListView.requestLayout();
							bookListAdapter.notifyDataSetChanged();

						}
					});
				} catch (JsonParseException e) {
					e.printStackTrace();

				} catch (JsonMappingException e) {
					e.printStackTrace();

				}
				catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(), R.string.reloadfail, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity()).setTitle("reload onFailure11").setMessage(e.getMessage())
								.show();
					}
				});

			}

		});
	}

	// 全部书单的加载更多
	void loadmoreBooksListByAll() {
		Request request = Servelet.requestuildApi("/books/" + (page + 1)).get().build();
		loadmore(request);
	}

	// 搜索列表的加载更多
	void loadmoreBooksListByKeyword() {
		// 判断类型有无选择
		if (bookTag_text.equals(R.string.all)) {
			Request request = Servelet.requestuildApi("/book/s/" + keywords + "/" + (page + 1)).get().build();
			loadmore(request);
		} else {
			loadmoreBookListByKeywordAndType();
		}
	}

	// 生成得到搜索书籍分类的加载更多
	void loadmoreBooksListByTag(String tag) {
		Request request = Servelet.requestuildApi("/books/" + tag + "/class/" + (page + 1)).get().build();
		loadmore(request);
	}

	//生成得到搜索书籍并分类的加载更多
	void loadmoreBookListByKeywordAndType(){
		Request request = Servelet.requestuildApi("/books/"+keywords+"/and/"+bookTag_text+"/class"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//加载更多
	void loadmore(Request request){
		//		btn_loadmore.setEnabled(false);
		//		textLoadMore.setText("努力加载中...");

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						//						btn_loadmore.setEnabled(true);
						//						textLoadMore.setText("加载更多");

					}
				});

				try {
					final Page<Book> books = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Book>>() {});
					if(books.getNumber()>page){

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								//数据更改放到前台运行，前台不停在读数据，如果后台更改数据，那么前台读取的数据便错误了
								if(booksData==null){
									booksData=books.getContent();
								}else{
									booksData.addAll(books.getContent());
								}
								page = books.getNumber();
								//--
								bookListView.requestLayout();
								bookListAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//							textLoadMore.setText("没有更多了");
							bookListView.requestLayout();
							bookListAdapter.notifyDataSetChanged();
						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						//						btn_loadmore.setEnabled(true);
						//						textLoadMore.setText("加载更多");
					}
				});
			}
		});
	}


	public class MyAdapter extends PagerAdapter {
		// 代表的是当前传进来的对象，是不是要在我当前页面显示的
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;// 如果当前显示的View跟你传进来的是同一个View,说明就是要显示的 view
		}
		@Override
		// 图片的数量
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		/**
		 * 移除当前一张图片
		 * */
		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(view, position, object);
			view.removeView(imageviews.get(position));
		}

		/**
		 * 添加一张图片
		 * */
		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(imageviews.get(position));
			return imageviews.get(position);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.activity_view_page , menu);
		return true;
	}
	@Override
	public void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		//指定两秒钟切花一张图片
		scheduledExecutorService.scheduleAtFixedRate(new ViewPageTask(), 2, 2, TimeUnit.SECONDS);
	}
	@Override
	public void onStop() {
		super.onStop();
		scheduledExecutorService.shutdown();
	};

	public class ViewPageTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem+1) % images.length;
			handler.sendEmptyMessage(0);
		}

	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			viewpager.setCurrentItem(currentItem);
		}

	};
	private ScheduledExecutorService scheduledExecutorService;


}