package com.itcast.booksale;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class PayMoneyActivity extends Activity{
	private AvatarView userAvatar; //照片
	private TextView userName; //姓名
	private TextView orderNumber;//订单号
	private TextView momeyPay;//总金额
	private TextView userMomey;//余额
	private TextView btn_pay;//付款

	String balanceMoney;//余额
	String ordersId;

	public OrderLists orderlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_view);

		ordersId = getIntent().getStringExtra("ordersId");
		//初始化
		initPayList();


		btn_pay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				payMoney();

			}
		});
	}

	void payMoney(){
		float user_money = Float.valueOf(userMomey.getText().toString());
		String temp = momeyPay.getText().toString();
		float pay_money = Float.valueOf(temp.substring(1,temp.length()-1));
		Log.d("------money------------",temp.substring(1,temp.length()-1) );
		if(user_money < pay_money){
			new AlertDialog.Builder(this)
			.setTitle("余额不足")
			.setMessage("您的余额不足，请尽快充值")
			.setNegativeButton("好", null)
			.show();

		}else{
			balanceMoney = String.valueOf(pay_money);
			new AlertDialog.Builder(this)
			.setTitle("确认付款")
			.setMessage("确定付款吗？")
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					linDataBase(balanceMoney);					
				}
			})
			.setPositiveButton("取消", null)
			.show();
		}
	}

	void linDataBase(String balanceMoney){
		MultipartBody Money = new MultipartBody.Builder()
				.addFormDataPart("useMoney",balanceMoney).build();

		Request request=Servelet.requestuildApi("/me/recharge/use")
				.post(Money)
				.build();
		//"/me/recharge/use"
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				gobackHelloword();
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	void initPayList(){
		//获取
		userAvatar = (AvatarView) findViewById(R.id.c_user_avatar);
		userName = (TextView) findViewById(R.id.c_user_name);
		orderNumber = (TextView) findViewById(R.id.c_orders_numb);
		momeyPay = (TextView) findViewById(R.id.c_user_pay);
		userMomey = (TextView) findViewById(R.id.c_user_money);
		btn_pay = (TextView) findViewById(R.id.btn_pay_money);
	}


	void getOrdersMassage(String ordersId){
		//通过订单号搜到信息
		Request request=Servelet.requestuildApi("/orders/get/"+ordersId)
				.get()
				.build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				String body =arg1.body().string();
				try{
					ObjectMapper objectMapper=new ObjectMapper();
					OrderLists orderli = objectMapper.readValue(body, OrderLists.class);
					Log.d("orders------------", orderli.getUser().getName());
					setOrder(orderli);
				}catch(final Exception e){

				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {


			}
		});
	}

	void setOrder(OrderLists orderlist){
		this.orderlist = orderlist;
		Log.d("chuan", orderlist.getFinish());
		//--------
		userAvatar.load(orderlist.getUser());
		userName.setText(orderlist.getUser().getName());
		orderNumber.setText(orderlist.getOrderId());
		momeyPay.setText(orderlist.getPayMoney());
		userMomey.setText(String.valueOf(orderlist.getUser().getSumMoney()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		//获取服务器数据
		getOrdersMassage(ordersId);
		//异步，传回时间比程序运行时间晚，所以得不到数据
	}

	void gobackHelloword(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
		finish();
		OrdersActivity.temp.finish();
	}

}
