package com.itcast.booksale;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.pay.DialogWidget;
import com.itcast.booksale.pay.PayActivity;
import com.itcast.booksale.pay.PayPasswordView;
import com.itcast.booksale.pay.PayPasswordView.OnPayListener;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class PayMoneyActivity extends Activity{
	private AvatarView userAvatar; 
	private TextView userName; 
	private TextView orderNumber;
	private TextView momeyPay;
	private TextView userMomey;
	private TextView btn_pay;

	private DialogWidget mDialogWidget;

	String balanceMoney;
	String ordersId;
	String AllPay;
	String payType;
	List<Bookbus> order;
	//------------------- 

	public OrderLists orderlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_view);

		ordersId = getIntent().getStringExtra("ordersId");
		AllPay = getIntent().getStringExtra("AllPay");
		payType = getIntent().getStringExtra("payType");

		initPayList();
		
		if(order==null){
			order = (List<Bookbus>) getIntent().getSerializableExtra("order");
		}else{
			order = new ArrayList<Bookbus>();
		}
		
		setOrder();

		btn_pay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				payMoney();
				changeState();//切换状态
//				goSpedingBillActivity();
			}
		});
	}

	void payMoney(){
		float user_money = Float.valueOf(userMomey.getText().toString());
		String temp = AllPay;
		Log.d("mommmfjjjfjf22222222222222222222", temp);
//		Log.d("allpay--e-e-e-e---e--e-", temp);
		float pay_money = Float.valueOf(temp.substring(2,temp.length()-1));
//		Log.d("------money------------",temp.substring(2,temp.length()-1) );
		if(user_money < pay_money){
			new AlertDialog.Builder(this)
			.setTitle("余额不足")
			.setMessage("您的余额不足,请尽快充值")
			.setNegativeButton("好", null)
			.show();

		}else{
			balanceMoney = String.valueOf(pay_money);
			Log.d("mommmfjjjfjf111111111111111111111111", balanceMoney);
			new AlertDialog.Builder(this)
			.setTitle("确定支付")
			.setMessage("确定要支付吗?")
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDialogWidget=new DialogWidget(PayMoneyActivity.this, getDecorViewDialog());
					mDialogWidget.show();

//					linDataBase(balanceMoney);
				}
			})
			.setPositiveButton("取消", null)
			.show();
		}
	}

	protected View getDecorViewDialog() {
		// TODO Auto-generated method stub
		//return PayPasswordView.getInstance("45.99",this,new OnPayListener() {

		return PayPasswordView.getInstance(this,new OnPayListener() {

			@Override
			public void onSurePay(String password) {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				//payTextView.setText(password);
				Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
				Log.d("mommmfjjjfjf", balanceMoney);
				linDataBase(balanceMoney);
				
				goSpedingBillActivity();
			}

			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				Toast.makeText(getApplicationContext(), "取消支付", Toast.LENGTH_SHORT).show();

			}
		}).getView();
	}

	void linDataBase(String balanceMoney){
		
		MultipartBody Money = new MultipartBody.Builder()
				.addFormDataPart("useMoney",balanceMoney).build();

		Request request=Servelet.requestuildApi("me/recharge/use")
				.post(Money)
				.build();
		//"/me/recharge/use"
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				//				gobackHelloword();
				PayMoneyActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						
						Toast.makeText(PayMoneyActivity.this, "您的宝贝将以火箭速度向您飞来", Toast.LENGTH_SHORT).show();
						
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	void initPayList(){

		userAvatar = (AvatarView) findViewById(R.id.c_user_avatar);
		userName = (TextView) findViewById(R.id.c_user_name);
		orderNumber = (TextView) findViewById(R.id.c_orders_numb);
		momeyPay = (TextView) findViewById(R.id.c_user_pay);
		userMomey = (TextView) findViewById(R.id.c_user_money);
		btn_pay = (TextView) findViewById(R.id.btn_pay_money);
	}

	void changeState(){
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("orderId", ordersId)
				.addFormDataPart("finish","1")//已付款
				.build();

		Request request=Servelet.requestuildApi("order/changeState")
				.post(body)
				.build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				PayMoneyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
//						Toast.makeText(PayMoneyActivity.this, "pioiuuifdf", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	void setOrder(){
		//--------
		userAvatar.load(order.get(0).getId().getUser());
		userName.setText(order.get(0).getId().getUser().getName());
		orderNumber.setText(ordersId);
		momeyPay.setText(AllPay);
		userMomey.setText(String.valueOf(order.get(0).getId().getUser().getSumMoney()));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/*void gobackHelloword(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
		finish();
		OrdersActivity.temp.finish();
	}*/

	void goSpedingBillActivity(){
		payType = "已付款";
		Intent itnt = new Intent(this,BillDetailActivity.class);
		itnt.putExtra("order",(Serializable)order);//修改把list<>传过去
		itnt.putExtra("AllPay", AllPay);
		itnt.putExtra("payType",payType);
		startActivity(itnt);
		finish();
	}

}
