package com.itcast.booksale;

import com.example.booksale.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {


	
	
	private Button btn_comment;        //评论按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity_comment);

		btn_comment=(Button) findViewById(R.id.btn_books_review);
		btn_comment.setOnClickListener(new CommmentOnClickListener());
		

	}

	class CommmentOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			Intent intent=new Intent(MainActivity.this, CommentActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		
	}

	
}
