package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.BooksContentActivity;
import com.itcast.booksale.R;
import com.itcast.booksale.ViewPageActivity.MyAdapter;
import com.itcast.booksale.ViewPageActivity.ViewPageTask;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment {
	// 棣栭〉
	View booksView,headerView;
	List<Book> booksData;
	ListView bookListView;
	EditText keyword;// 鎼滅储鍏抽敭瀛�
	int page = 0;
	String keywords;


	//鍔犺浇鏇村
	View btn_loadmore;
	TextView textLoadMore;
	int loadmoreSelect;//鐢ㄤ簬鍖哄垎鍚勪釜涔﹀崟鍒楄〃鐨勫姞杞芥洿澶�

	//鍥句功鍒嗙被(鏍囩)
	private Spinner bookTagSpinner;
	private List<String> bookTag_list;
	private ArrayAdapter<String> booksTag_adapter;
	String bookTag_text;




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
			btn_loadmore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btn_loadmore.findViewById(R.id.text);


			
			bookListView = (ListView) booksView.findViewById(R.id.books_list);
			headerView = inflater.inflate(R.layout.headerview, null);
			keyword = (EditText) headerView.findViewById(R.id.search_keyword);

			//鍥句功鍒嗙被
			bookTagSpinner = (Spinner) headerView.findViewById(R.id.spinner_book_tag_select);
			initData();//鍒濆鍖�



			//鍔犺浇鏇村
			bookListView.addFooterView(btn_loadmore);
			bookListView.addHeaderView(headerView);
			btn_loadmore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//鍒ゆ柇褰撳墠涓哄摢涓�绉嶅垪琛�
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


				}
			});

			bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					goBookIntroduction(position);// 璺宠浆鍒颁功绫嶇畝浠�
				}

			});

			bookListView.setAdapter(bookListAdapter);


			// 鎼滅储鍔熻兘(鍦∣nResume涓疄鐜�)
			// 璁剧疆鍘熷鍒楄〃
			getBooksListByAll();// 鑾峰彇涔︾睄鏁版嵁
			
			images = new Integer[] { R.drawable.a, R.drawable.b, R.drawable.c,
					R.drawable.d, R.drawable.e };
			titles = new String[] { "书香", "就输大甩卖",
					"身临图书馆", "品味书", "看书看到天亮" };
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

	// -------
	// 鍒涘缓閫傞厤鍣� BaseAdapter listAdapter
	// getView(閲嶈),getItemID,getItem,getCount
	// Suppresslint("InflateParams")
	// 鍦╣etView閲岄潰,鍙栧嚭瑙嗗浘 layoutInflater *= *.from(parent.getContext())
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

			// 璁剧疆鏁版嵁锛屽苟鑾峰彇
			
			TextView textDate = (TextView) view.findViewById(R.id.edit_date);// 缂栧啓鏃ユ湡
			BookAvatarView bookAvatar = (BookAvatarView) view.findViewById(R.id.book_avatar);// 灏侀潰
			TextView bookCellTitle = (TextView) view.findViewById(R.id.book_title);// 涔﹀悕
			TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);// 浣滆��
			TextView bookSummary = (TextView) view.findViewById(R.id.text_about_book);//绠�浠�
			TextView bookPrice = (TextView) view.findViewById(R.id.book_price);// 鍞环
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
			bookAvatar.load(Servelet.urlstring + book.getBookavatar());//涔︾殑灏侀潰
			bookCellTitle.setText(book.getTitle());
			bookAuthor.setText(book.getAuthor());
			bookSummary.setText(book.getSummary());
			bookPrice.setText(book.getPrice()+" 鍏�");
						

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
	 * 涓婁紶璐墿杞︿俊鎭�
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
							
							new AlertDialog
							.Builder(getActivity())
							.setTitle("success to add bookbus")
							.setMessage(string)
							.setPositiveButton("ok", null)
							.show();
							
						}
					});
				} catch (Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {

							Toast.makeText(getActivity(), "涓婁紶璐墿杞﹀け璐�", Toast.LENGTH_SHORT).show();
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

	// 杞埌涔︽湰璇︽儏椤甸潰
	void goBookIntroduction(int position) {
		Book book = booksData.get(position);

		Intent itnt = new Intent(getActivity(), BooksContentActivity.class);
		itnt.putExtra("data", book); // 浼犱功鐨勫唴瀹圭粰BooksContentActivity

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();

		//鎼滅储鎸夐挳
		headerView.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchByKeyword();

			}
		});

		//鍒嗙被鎸夐挳

		bookTagSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//閫変腑涓嬫媺妗嗗悗璁剧疆绫诲瀷
				bookTag_text= adapter.getItem(position);
				keywords = keyword.getText().toString();
				textLoadMore.setText("鍔犺浇鏇村");
				if(bookTag_text.equals("鍏ㄩ儴") && keywords.length()==0){
					getBooksListByAll();
				}else if(keywords.length()==0){
					getBooksListByTag(bookTag_text);
				}else if(bookTag_text.equals("鍏ㄩ儴") && keywords.length()!=0){
					getBooksListByKeyword();
				}else {
					getBookListByKeywordAndType(keywords,bookTag_text);
				}

				try {
					Field field =       AdapterView.class.getDeclaredField("mOldSelectedPosition");
					field.setAccessible(true);  //璁剧疆mOldSelectedPosition鍙闂�
					field.setInt(bookTagSpinner, AdapterView.INVALID_POSITION); //璁剧疆mOldSelectedPosition鐨勫��
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}


	//鐢熸垚寰楀埌鎵�鏈変功鐨勮繛鎺�
	void getBooksListByAll(){
		loadmoreSelect = 0;//0涓烘墍鏈変功鐨勫姞杞芥洿澶�
		Request request = Servelet.requestuildApi("books")
				.get()
				.build();

		reload(request);
	}


	//鐢熸垚寰楀埌鎼滅储涔︾睄鐨勪功鍗�
	void getBooksListByKeyword(){
		loadmoreSelect = 1;//1涓烘悳绱㈢殑鍔犺浇鏇村
		//鍒ゆ柇绫诲瀷鏈夋棤閫夋嫨		
		if(bookTag_text.equals("鍏ㄩ儴")){
			Request request = Servelet.requestuildApi("/book/s/"+keywords)
					.get()
					.build();

			reload(request);
		}else{
			getBookListByKeywordAndType(keywords,bookTag_text);
		}

	}


	//鐢熸垚寰楀埌鎼滅储涔︾睄鍒嗙被鐨勪功鍗�
	void getBooksListByTag(String tag){
		loadmoreSelect = 2;//2涓哄垎绫荤殑鍔犺浇鏇村
		bookTag_text = tag;

		Request request = Servelet.requestuildApi("/books/"+tag+"/class")
				.get()
				.build();
		reload(request);
	}

	//鐢熸垚寰楀埌鎼滅储涔︾睄骞跺甫鏈夊垎绫荤殑涔﹀崟
	void getBookListByKeywordAndType(String key,String tag){
		loadmoreSelect = 3;//3涓烘悳绱㈠姞鍒嗙被鐨勫姞杞芥洿澶�
		keywords = key;
		bookTag_text = tag;
		Request request = Servelet.requestuildApi("/books/"+keywords+"/and/"+bookTag_text+"/class")
				.get()
				.build();

		reload(request);
	}

	//鎼滅储鍏抽敭瀛�
	void searchByKeyword(){
		keywords = keyword.getText().toString();
		if(keywords.length() == 0){
			Toast.makeText(getActivity(),
					"璇疯緭鍏ュ叧閿瓧", Toast.LENGTH_SHORT).show();
			return;
		}else{

			Editable edittext = keyword.getText();
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromInputMethod(keyword.getWindowToken(), 0);
			getBooksListByKeyword();

		}
	}


	//鍒涘缓杩炴帴鍙戦�佽姹傚苟浼犲洖淇℃伅
	void reload(Request request){


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
							BookListFragment.this.page = data.getNumber();// 鏀捐繘涓荤嚎绋嬭繘琛岋紝纭繚鏁版嵁閮ㄤ綅绌�
							BookListFragment.this.booksData = data.getContent();

							bookListAdapter.notifyDataSetInvalidated();

						}
					});
				}catch (JsonParseException e) {
					e.printStackTrace();

				} catch (JsonMappingException e) {
					e.printStackTrace();

				}
				catch (final Exception e) {
					/*getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setTitle("reload閿欒")
							.setMessage(e.getMessage()).show();
						}
					});*/
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							Toast.makeText(getActivity(), "reload閿欒", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setTitle("reload onFailure")
						.setMessage(e.getMessage()).show();
					}
				});

			}

		});
	}

	//鍏ㄩ儴涔﹀崟鐨勫姞杞芥洿澶�
	void loadmoreBooksListByAll(){
		Request request = Servelet.requestuildApi("/books/"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//鎼滅储鍒楄〃鐨勫姞杞芥洿澶�
	void loadmoreBooksListByKeyword(){
		//鍒ゆ柇绫诲瀷鏈夋棤閫夋嫨
		if(bookTag_text.equals("鍏ㄩ儴")){
			Request request = Servelet.requestuildApi("/book/s/"+keywords+"/"+(page+1))
					.get()
					.build();
			loadmore(request);
		}else{
			loadmoreBookListByKeywordAndType();
		}
	}

	//鐢熸垚寰楀埌鎼滅储涔︾睄鍒嗙被鐨勫姞杞芥洿澶�
	void loadmoreBooksListByTag(String tag){
		Request request = Servelet.requestuildApi("/books/"+tag+"/class/"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//鐢熸垚寰楀埌鎼滅储涔︾睄骞跺垎绫荤殑鍔犺浇鏇村
	void loadmoreBookListByKeywordAndType(){
		Request request = Servelet.requestuildApi("/books/"+keywords+"/and/"+bookTag_text+"/class"+(page+1))
				.get()
				.build();
		loadmore(request);
	}

	//鍔犺浇鏇村
	void loadmore(Request request){
		btn_loadmore.setEnabled(false);
		textLoadMore.setText("鍔姏鍔犺浇涓�...");

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btn_loadmore.setEnabled(true);
						textLoadMore.setText("鍔犺浇鏇村");

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
							textLoadMore.setText("娌℃湁鏇村浜�");
							bookListAdapter.notifyDataSetChanged();
//							new AlertDialog.Builder(getActivity())
//							.setTitle("鑾峰彇澶辫触")
//							.setMessage(e.getLocalizedMessage())
//							.setPositiveButton("ok",null)
//							.show();
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
						textLoadMore.setText("鍔犺浇鏇村");
					}
				});
			}
		});
	}

	//鍥句功鍒嗙被
	public void initData(){
		//娣诲姞涓嬫媺妗嗘暟鎹�
		bookTag_list = new ArrayList<String>();
		bookTag_list.add("鍏ㄩ儴");
		bookTag_list.add("鏁欑涔�");
		bookTag_list.add("鏂囧");
		bookTag_list.add("绔ヤ功");
		bookTag_list.add("鑹烘湳");
		bookTag_list.add("绉戞妧");
		bookTag_list.add("鐢熸椿");
		bookTag_list.add("璁＄畻鏈�");
		//閫傞厤鍣�
		booksTag_adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,bookTag_list);
		//璁剧疆鏍峰紡
		booksTag_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//鍔犺浇閫傞厤鍣�
		bookTagSpinner.setAdapter(booksTag_adapter);

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