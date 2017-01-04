package com.itcast.booksale.pay;

import com.itcast.booksale.R;
import com.itcast.booksale.pay.PayPasswordView.OnPayListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends Activity {

	private DialogWidget mDialogWidget;
	private TextView payTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		payTextView=(TextView)findViewById(R.id.payEditText);
		payTextView.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialogWidget=new DialogWidget(PayActivity.this, getDecorViewDialog());
				mDialogWidget.show();
			}
		});
	}
	
	protected View getDecorViewDialog() {
		// TODO Auto-generated method stub
		return PayPasswordView.getInstance("45.99",this,new OnPayListener() {
			
			@Override
			public void onSurePay(String password) {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				payTextView.setText(password);
				Toast.makeText(getApplicationContext(), "交易成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				Toast.makeText(getApplicationContext(), "交易已取消", Toast.LENGTH_SHORT).show();
				
			}
		}).getView();
	}
}