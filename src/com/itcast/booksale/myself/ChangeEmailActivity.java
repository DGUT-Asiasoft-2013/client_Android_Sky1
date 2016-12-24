package com.itcast.booksale.myself;

import com.itcast.booksale.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeEmailActivity extends Activity{

	TextView emailTitle;
	EditText emailhint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_email);
		
		emailhint=(EditText) findViewById(R.id.change_edittext);
		emailTitle=(TextView) findViewById(R.id.change_title);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		emailhint.setHint("«Î ‰»Î–¬µƒ” œ‰");
		emailTitle.setText("” œ‰");
	}
}
