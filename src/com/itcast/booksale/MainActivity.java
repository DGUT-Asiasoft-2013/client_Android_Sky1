package com.itcast.booksale;

import com.example.booksale.R;
import com.itcast.booksale.fragment.pages.BookListFragment;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	BookListFragment bookListFragment = new BookListFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, bookListFragment) 
		.commit();
		
	}

	
}
