package com.itcast.booksale.myself;

import com.itcast.booksale.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeQqActivity extends Activity{

	TextView qqTitle;
	EditText qqhint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_qq);
		
		qqTitle=(TextView) findViewById(R.id.change_title);
		qqhint=(EditText) findViewById(R.id.change_edittext);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		qqhint.setHint("«Î ‰»Î–¬µƒQQ");
		qqTitle.setText("QQ");
	}
}
