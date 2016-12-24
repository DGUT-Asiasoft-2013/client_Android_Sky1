package com.itcast.booksale.myself;

import com.itcast.booksale.R;
import com.itcast.booksale.inputcells.ChangeInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ∏ƒ±‰Í«≥∆
 * 
 * @author Administrator
 *
 */
public class ChangeNameActivity extends Activity {
	//ChangeInputCellFragment fragNameTitle,fragNameHint;
	TextView nametitle;
	EditText namehint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_name);
		
		
	/*	fragNameTitle=(ChangeInputCellFragment) getFragmentManager().findFragmentById(R.id.change_title);
		fragNameHint=(ChangeInputCellFragment) getFragmentManager().findFragmentById(R.id.change_edittext);*/
		
		nametitle=(TextView) findViewById(R.id.change_title);
		namehint=(EditText) findViewById(R.id.change_edittext);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*fragNameHint.setHintText(" ‰»ÎÍ«≥∆");
		
		fragNameTitle.setTitleText("Í«≥∆");*/
		
		nametitle.setText("Í«≥∆");
		namehint.setHint("«Î ‰»Î–¬Í«≥∆");
	}
}
