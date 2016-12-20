package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;

import com.itcast.booksale.fragment.pages.BookListFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {


	
	
	private Button btn_comment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
