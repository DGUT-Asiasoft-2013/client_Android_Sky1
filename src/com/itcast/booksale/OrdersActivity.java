package com.itcast.booksale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * 订单详情页面
 * @author Administrator
 *
 */
public class OrdersActivity extends Activity {
	//	Bookbus order;//gone
	List<Bookbus> order; //get bookbus's massage what were putted
	OrderLists orderList;
	//ye mian shu xing
	private AvatarView bookUserAvatar; // 图书卖家照片
	private TextView bookUserName; // 卖书卖家姓名
	private TextView btn_order;//提交订单
	private TextView momey_all;//总金额
	private String AllPay;
	private String orderNumber;//订单号

	private BookAvatarView bookAvatar; // 图书照片
	private TextView bookTitle; // 图书标题
	private TextView bookAuthor;//图书作者
	private TextView bookSummary; //图书简介
	private TextView bookPrice;// 售价
	private TextView books_buy_numb;//购买数量
	//----------------修改内容
	LinearLayout add_bookList;//添加购物车中多本书籍
	LinearLayout bookList;//书籍的布局
	LayoutInflater inflater;
	//-------------------------------

	//Pay Type Spinner
	private Spinner payTypeSpinner;
	private List<String> payType_list;
	private ArrayAdapter<String> payType_adapter;
	String payType_text;

	//----
	public static OrdersActivity temp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		temp = this;
		setContentView(R.layout.activity_orders_view);
		//-----------设置当前布局
		inflater = LayoutInflater.from(this);

		//get bookbus
		if(order==null){
			order = (List<Bookbus>) getIntent().getSerializableExtra("bookbus");
		}else{
			order = new ArrayList<Bookbus>();
		}
		
	//	Log.d("bookbus,-----------------========", order.get(0).getId().getBook().getTitle());

		AllPay = getIntent().getStringExtra("AllPay");
		orderNumber = getIntent().getStringExtra("order_number");

		add_bookList = (LinearLayout) findViewById(R.id.lin_books);//购买图书列表
		initorders(); // 初始化父类布局
		


		//遍历List<Bookbus>===============================================
		String size = String.valueOf(order.size());
		Log.d("size==========================", size);
		for(int i=0;i<order.size();i++){
			addBookList();
			setorders(order.get(i));//设置父类信息
			setBooks(order.get(i));//设置书籍信息
		}

		//=========================================================

		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//提交订单
				saveOrdersList(order,AllPay,orderNumber,payType_text);


			}
		});
		//		Log.i("------------检测----------", "----------------lalaa-----------");


	}
	//保存order到数据库
	void saveOrdersList(List<Bookbus> order2,String AllPay,String orderNumber,final String payType_text){
		int book_id = ((Bookbus) order2).getId().getBook().getId();
		//		Log.d("-----AllPay-----", order.getId().getBook().getAuthor());
		MultipartBody orderbody = new MultipartBody.Builder()
				.addFormDataPart("orderId",orderNumber)
				.addFormDataPart("payMoney", AllPay)
				.addFormDataPart("payway", payType_text)
				.addFormDataPart("finish", "已提交")
				.build();

		Request request = Servelet.requestuildApi("books/"+book_id+"/orders")
				.post(orderbody)
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				final String arg = arg1.body().string();
				//把订单格式化

				ObjectMapper objectMapper=new ObjectMapper();
				orderList = objectMapper.readValue(arg, OrderLists.class);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Log.d("ARG-------------------", arg);
						Log.d("payType_text00000000000000000", payType_text);
						new AlertDialog.Builder(OrdersActivity.this)
						.setTitle("连接成功")
						.setMessage("提交订单成功")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(payType_text.equals("在线交易")){
									//支付页面
									goPayActivity();
								}else{
									Toast.makeText(OrdersActivity.this, "私下订单已生成", Toast.LENGTH_SHORT).show();
									goSpedingBillActivity();
								}

							}
						}).show();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(OrdersActivity.this)
						.setTitle("连接失败")
						.setMessage("提交订单失败")
						.setPositiveButton("OK", null).show();
					}
				});

			}
		});

	}

	//支付页面
	void goPayActivity(){
		Intent itnt = new Intent(this,PayMoneyActivity.class);
		itnt.putExtra("ordersId", orderNumber);
		startActivity(itnt);
		finish();
	}
	//私下订单页面
	void goSpedingBillActivity(){
		Intent itnt = new Intent(this,BillDetailActivity.class);
		itnt.putExtra("order",orderList);
		startActivity(itnt);
		finish();
	}

	void initorders() {
		bookUserAvatar = (AvatarView) findViewById(R.id.user_avatar);
		bookUserName = (TextView) findViewById(R.id.user_name);
		momey_all = (TextView) findViewById(R.id.momey_all);
		btn_order = (TextView) findViewById(R.id.btn_order);//提交订单按钮

		payTypeSpinner = (Spinner) findViewById(R.id.spinner_pay_tag);
		//添加下拉框数据
		payType_list = new ArrayList<String>();
		payType_list.add("在线交易");
		payType_list.add("私下交易");


		//适配器
		payType_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,payType_list);
		//设置样式
		payType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		payTypeSpinner.setAdapter(payType_adapter);

		payTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//选中下拉框后设置类型
				payType_text= adapter.getItem(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	//------------
	void initBook(){
		//获取订单
		bookAvatar = (BookAvatarView) bookList.findViewById(R.id.book_avatar);
		bookTitle = (TextView) bookList.findViewById(R.id.book_title); // 图书标题
		bookAuthor = (TextView) bookList.findViewById(R.id.book_author);//图书作者
		bookSummary = (TextView) bookList.findViewById(R.id.text_about_book); //图书简介
		bookPrice = (TextView) bookList.findViewById(R.id.book_price);// 售价
		books_buy_numb = (TextView) bookList.findViewById(R.id.books_numb);//购买数量

	}

	//遍历设置订单信息
	void setorders(Bookbus order){
		//设置
		bookUserAvatar.load(order.getId().getUser());
		bookUserName.setText(order.getId().getUser().getName());
		momey_all.setText(AllPay);

	}

	void setBooks(Bookbus order){
		//设置内容
		bookAvatar.load(Servelet.urlstring + order.getId().getBook().getBookavatar());
		bookTitle.setText(order.getId().getBook().getTitle());

		bookAuthor.setText(order.getId().getBook().getAuthor());
		bookSummary.setText(order.getId().getBook().getSummary());
		bookPrice.setText(String.valueOf(order.getId().getBook().getPrice()));
	}

	//----------------------
	void addBookList(){
		//---------------书籍布局
		bookList = (LinearLayout) inflater.inflate(R.layout.cell_order_list,null).findViewById(R.id.order_list_cell);
		initBook();// 初始化书籍布局
		//添加新的书籍布局
		add_bookList.addView(bookList);

	}
	//-----------
	//监听返回键
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
	//-------------


}
