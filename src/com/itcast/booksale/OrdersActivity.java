package com.itcast.booksale;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.fragment.widgets.BookAvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 璁㈠崟璇︽儏椤甸潰
 * @author Administrator
 *
 */
public class OrdersActivity extends Activity {
	//	Bookbus order;//gone
	List<Bookbus> order; //get bookbus's massage what were putted
	OrderLists orderList;
	//ye mian shu xing
	private AvatarView bookUserAvatar; // 鍥句功鍗栧鐓х墖
	private TextView bookUserName; // 鍗栦功鍗栧濮撳悕
	private TextView btn_order;//鎻愪氦璁㈠崟
	private TextView momey_all;//鎬婚噾棰�
	private String AllPay;
	private String orderNumber;//璁㈠崟鍙�

	private BookAvatarView bookAvatar; // 鍥句功鐓х墖
	private TextView bookTitle; // 鍥句功鏍囬
	private TextView bookAuthor;//鍥句功浣滆��
	private TextView bookSummary; //鍥句功绠�浠�
	private TextView bookPrice;// 鍞环
	private TextView books_buy_numb;//璐拱鏁伴噺
	//----------------淇敼鍐呭
	LinearLayout add_bookList;//娣诲姞璐墿杞︿腑澶氭湰涔︾睄
	LinearLayout bookList;//涔︾睄鐨勫竷灞�
	LayoutInflater inflater;
	//-------------------------------

	//Pay Type Spinner
	private Spinner payTypeSpinner;
	private List<String> payType_list;
	private ArrayAdapter<String> payType_adapter;
	String payType_text;
	int index;
	int payType_tag;
	String payTag;

	//----
	public static OrdersActivity temp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		temp = this;
		setContentView(R.layout.activity_orders_view);
		//-----------璁剧疆褰撳墠甯冨眬
		inflater = LayoutInflater.from(this);

		//get bookbus
		if(order==null){
			order = (List<Bookbus>) getIntent().getSerializableExtra("bookbus");
		}else{
			order = new ArrayList<Bookbus>();
		}

		AllPay = getIntent().getStringExtra("AllPay");
		orderNumber = getIntent().getStringExtra("order_number");

		add_bookList = (LinearLayout) findViewById(R.id.lin_books);//璐拱鍥句功鍒楄〃
		initorders(); // 鍒濆鍖栫埗绫诲竷灞�



		//閬嶅巻List<Bookbus>===============================================
		String size = String.valueOf(order.size());
		Log.d("size==========================", size);
		for(int i=0;i<order.size();i++){
			addBookList();
			setorders(order.get(i));//璁剧疆鐖剁被淇℃伅
			setBooks(order.get(i));//璁剧疆涔︾睄淇℃伅
		}

		//=========================================================

		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//鎻愪氦璁㈠崟
				saveOrdersList(order,AllPay,orderNumber,payType_text);


			}
		});


	}

	//淇濆瓨order鍒版暟鎹簱
	void saveOrdersList(List<Bookbus> order2,String AllPay,String orderNumber,final String payType_text){
		for(int i=0;i<order2.size();i++){
			int book_id =  order2.get(i).getId().getBook().getId();
			saveBook(book_id,payTag);
		}

		new AlertDialog.Builder(OrdersActivity.this)
		.setTitle("杩炴帴鎴愬姛")
		.setMessage("鎻愪氦璁㈠崟鎴愬姛")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(payType_tag == 0){
					goPayActivity();
					//鏀粯椤甸潰
				}else if(payType_tag == 1){
					Toast.makeText(OrdersActivity.this, "绉佷笅璁㈠崟宸茬敓鎴�", Toast.LENGTH_SHORT).show();
					goSpedingBillActivity();
				}else{
					return;
				}

			}
		}).show();




	}

	//鏀粯椤甸潰
	void goPayActivity(){
		Intent itnt = new Intent(this,PayMoneyActivity.class);
		itnt.putExtra("order",(Serializable)order);//淇敼鎶妉ist<>浼犺繃鍘�
		itnt.putExtra("ordersId", orderNumber);
		itnt.putExtra("AllPay", AllPay);
		itnt.putExtra("payType",payType_text);
		startActivity(itnt);
		finish();
	}

	//绉佷笅璁㈠崟椤甸潰
	void goSpedingBillActivity(){
		Intent itnt = new Intent(this,BillDetailActivity.class);
		itnt.putExtra("order",(Serializable)order);//淇敼鎶妉ist<>浼犺繃鍘�
		itnt.putExtra("AllPay", AllPay);
		itnt.putExtra("payType",payType_text);
		startActivity(itnt);
		finish();
	}

	void initorders() {
		bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);
		bookUserName = (TextView) findViewById(R.id.user_name);
		momey_all = (TextView) findViewById(R.id.momey_all);
		btn_order = (TextView) findViewById(R.id.btn_order);//鎻愪氦璁㈠崟鎸夐挳

		payTypeSpinner = (Spinner) findViewById(R.id.spinner_pay_tag);
		//娣诲姞涓嬫媺妗嗘暟鎹�
		payType_list = new ArrayList<String>();
		payType_list.add("在线交易");
		payType_list.add("私下交易");


		//閫傞厤鍣�
		payType_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,payType_list);
		//璁剧疆鏍峰紡
		payType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//鍔犺浇閫傞厤鍣�
		payTypeSpinner.setAdapter(payType_adapter);

		payTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//閫変腑涓嬫媺妗嗗悗璁剧疆绫诲瀷
				payType_text= adapter.getItem(position);
				payType_tag = position;//0涓哄湪绾夸氦鏄擄紝1涓虹涓嬩氦鏄�
				payTag = String.valueOf(payType_tag);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				payType_tag = 0;//榛樿涓�0
			}
		});
	}

	//------------
	void initBook(){
		//鑾峰彇璁㈠崟
		bookAvatar = (BookAvatarView) bookList.findViewById(R.id.book_avatar);
		bookTitle = (TextView) bookList.findViewById(R.id.book_title); // 鍥句功鏍囬
		bookAuthor = (TextView) bookList.findViewById(R.id.book_author);//鍥句功浣滆��
		bookSummary = (TextView) bookList.findViewById(R.id.text_about_book); //鍥句功绠�浠�
		bookPrice = (TextView) bookList.findViewById(R.id.book_price);// 鍞环
		books_buy_numb = (TextView) bookList.findViewById(R.id.books_numb);//璐拱鏁伴噺

	}

	//閬嶅巻璁剧疆璁㈠崟淇℃伅
	void setorders(Bookbus order){
		//璁剧疆
		bookUserAvatar.load(order.getId().getUser());
		bookUserName.setText(order.getId().getUser().getName());
		momey_all.setText(AllPay);

	}

	void setBooks(Bookbus order){
		//璁剧疆鍐呭
		bookAvatar.load(Servelet.urlstring + order.getId().getBook().getBookavatar());
		bookTitle.setText(order.getId().getBook().getTitle());

		bookAuthor.setText(order.getId().getBook().getAuthor());
		bookSummary.setText(order.getId().getBook().getSummary());
		bookPrice.setText(String.valueOf(order.getId().getBook().getPrice()));
	}

	void saveBook(int book_id,String payTag){
		//		Log.d("-----AllPay-----", order.getId().getBook().getAuthor());
		MultipartBody orderbody = new MultipartBody.Builder()
				.addFormDataPart("orderId",orderNumber)
				.addFormDataPart("payMoney", AllPay)
				.addFormDataPart("payway", payTag)//0鎴�1
				.addFormDataPart("finish", "0")//鏈粯娆�
				.build();

		Request request = Servelet.requestuildApi("books/"+book_id+"/orders")
				.post(orderbody)
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				//				final String arg = arg1.body().string();
				//鎶婅鍗曟牸寮忓寲



				//				runOnUiThread(new Runnable() {
				//
				//					@Override
				//					public void run() {
				//						ObjectMapper objectMapper=new ObjectMapper();
				//						try {
				//							orderList = objectMapper.readValue(arg, OrderLists.class);
				//						} catch (JsonParseException e) {
				//							// TODO Auto-generated catch block
				//							e.printStackTrace();
				//						} catch (JsonMappingException e) {
				//							// TODO Auto-generated catch block
				//							e.printStackTrace();
				//						} catch (IOException e) {
				//							// TODO Auto-generated catch block
				//							e.printStackTrace();
				//						}
				//
				//						Log.d("ARG-------------------", arg);
				//						Log.d("payType_text00000000000000000", payType_text);
				//						
				//					}
				//				});


			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
				new AlertDialog.Builder(OrdersActivity.this)
				.setTitle("鎻愪氦澶辫触")
				.setMessage("鎻愪氦璁㈠崟澶辫触")
				.setNegativeButton("ok", null)
				.show();
			}
		});
		
		
		
	}
			

//		});
//	}

	//----------------------
	void addBookList(){
		//---------------涔︾睄甯冨眬
		bookList = (LinearLayout) inflater.inflate(R.layout.cell_order_list,null).findViewById(R.id.order_list_cell);
		initBook();// 鍒濆鍖栦功绫嶅竷灞�
		//娣诲姞鏂扮殑涔︾睄甯冨眬
		add_bookList.addView(bookList);

	}
	//-----------
	//鐩戝惉杩斿洖閿�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			//do something...
			order.clear();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
