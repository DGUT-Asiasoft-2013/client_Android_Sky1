package com.itcast.booksale;
import java.io.IOException;

import com.itcast.booksale.R;
import com.itcast.booksale.myself.RechargeActivity;
import com.itcast.booksale.pay.DialogWidget;
import com.itcast.booksale.pay.PayActivity;
import com.itcast.booksale.pay.PayPasswordView;
import com.itcast.booksale.pay.PayPasswordView.OnPayListener;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class SettingPayActivity extends Activity {
	
	private DialogWidget mDialogWidget;
	private TextView payTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_pay);
		
		payTextView=(TextView)findViewById(R.id.payEditText);
		payTextView.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialogWidget=new DialogWidget(SettingPayActivity.this, getDecorViewDialog());
				mDialogWidget.show();
			}
		});
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
				payTextView.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);	
				payTextView.setText(password);
				goSetPayPassword();
			
				/*Toast.makeText(getApplicationContext(), "֧���������óɹ�", Toast.LENGTH_SHORT).show();
				finish();*/
			}
			
			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				mDialogWidget.dismiss();
				mDialogWidget=null;
				Toast.makeText(getApplicationContext(), R.string.setpay_cancel, Toast.LENGTH_SHORT).show();
				
			}
		}).getView();
	}

	protected void goSetPayPassword() {
		// TODO Auto-generated method stub
		String payPassword=payTextView.getText().toString();
		
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("payPassword", payPassword)
				.build();
		
		Request request=Servelet.requestuildApi("set/pay")
				.post(body)
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				final String ar = arg1.body().toString();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							SettingPayActivity.this.onReponse(arg0, ar);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				});
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(SettingPayActivity.this).setTitle(R.string.sp_fail).setMessage(R.string.sp_net_error)
								.setNegativeButton(R.string.sp_qd, null).show();

					}
				});
			}
		});
	}

	protected void onReponse(Call arg0, String ar) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), R.string.sp_succeed, Toast.LENGTH_SHORT).show();
		finish();
	}
}