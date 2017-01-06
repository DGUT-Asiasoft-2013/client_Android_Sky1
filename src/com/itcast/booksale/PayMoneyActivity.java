package com.itcast.booksale;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	private AvatarView userAvatar; //鐓х墖
	private TextView userName; //濮撳悕
	private TextView orderNumber;//璁㈠崟鍙�
	private TextView momeyPay;//鎬婚噾棰�
	private TextView userMomey;//浣欓
	private TextView btn_pay;//浠樻

	private DialogWidget mDialogWidget;
	
	String balanceMoney;//浣欓
	String ordersId;

	public OrderLists orderlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_view);

		ordersId = getIntent().getStringExtra("ordersId");
		//鍒濆鍖�
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
			.setTitle("浣欓涓嶈冻")
			.setMessage("鎮ㄧ殑浣欓涓嶈冻锛岃灏藉揩鍏呭��")
			.setNegativeButton("濂�", null)
			.show();

		}else{
			balanceMoney = String.valueOf(pay_money);
			new AlertDialog.Builder(this)
			.setTitle("纭浠樻")
			.setMessage("纭畾浠樻鍚楋紵")
			.setNegativeButton("纭畾", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDialogWidget=new DialogWidget(PayMoneyActivity.this, getDecorViewDialog());
					mDialogWidget.show();
					
					//linDataBase(balanceMoney);
				}
			})
			.setPositiveButton("鍙栨秷", null)
			.show();
		}
	}
	
	protected View getDecorViewDialog() {
		// TODO Auto-generated method stub
		return PayPasswordView.getInstance("45.99",this,new OnPayListener() {
			
			@Override
			public void onSurePay(String password) {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				//payTextView.setText(password);
				Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
				linDataBase(balanceMoney);
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

		Request request=Servelet.requestuildApi("/me/recharge/use")
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
						Toast.makeText(PayMoneyActivity.this, "浠樻鎴愬姛锛屾偍鐨勫疂璐濆皢浼氶鍒版偍鎵嬩腑", Toast.LENGTH_SHORT).show();
						goSpedingBillActivity();
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
		//鑾峰彇
		userAvatar = (AvatarView) findViewById(R.id.c_user_avatar);
		userName = (TextView) findViewById(R.id.c_user_name);
		orderNumber = (TextView) findViewById(R.id.c_orders_numb);
		momeyPay = (TextView) findViewById(R.id.c_user_pay);
		userMomey = (TextView) findViewById(R.id.c_user_money);
		btn_pay = (TextView) findViewById(R.id.btn_pay_money);
	}


	void getOrdersMassage(String ordersId){
		//閫氳繃璁㈠崟鍙锋悳鍒颁俊鎭�
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
		//鑾峰彇鏈嶅姟鍣ㄦ暟鎹�
		getOrdersMassage(ordersId);
		//寮傛锛屼紶鍥炴椂闂存瘮绋嬪簭杩愯鏃堕棿鏅氾紝鎵�浠ュ緱涓嶅埌鏁版嵁
	}

	/*void gobackHelloword(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
		finish();
		OrdersActivity.temp.finish();
	}*/
	//绉佷笅璁㈠崟椤甸潰
	void goSpedingBillActivity(){
			Intent itnt = new Intent(this,BillDetailActivity.class);
			itnt.putExtra("order",orderlist);
			startActivity(itnt);
			finish();
		}

}
