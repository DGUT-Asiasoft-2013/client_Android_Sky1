package com.itcast.booksale.myself;

import com.itcast.booksale.R;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePhoneActivity extends Activity{
TextView phonetitle;
EditText phonehint;

@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_phone);
		
		phonehint=(EditText) findViewById(R.id.change_edittext);
		phonetitle=(TextView) findViewById(R.id.change_title);
	}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		phonehint.setHint("请输入电话");
		phonetitle.setText("电话");
	}
}
