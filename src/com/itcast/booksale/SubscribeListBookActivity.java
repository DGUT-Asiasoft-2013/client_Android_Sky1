package com.itcast.booksale;

import com.itcast.booksale.R;
import com.itcast.booksale.entity.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SubscribeListBookActivity extends  Activity{
	User saler;
	ListView list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_page_subscribe_book_list);
		saler = (User)getIntent().getSerializableExtra("data"); 
		list = (ListView)findViewById(R.id.list_book);
		
	}
}
