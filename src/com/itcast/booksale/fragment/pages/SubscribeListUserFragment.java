package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SubscribeListUserFragment extends Fragment {

	View view;
	ListView listView;
	View btnLoadMore;
	TextView textLoadMore;

	List<User> data;
	int page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null){
			view = inflater.inflate(R.layout.fragment_page_subscribe_user_list, null);
			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);

			listView = (ListView) view.findViewById(R.id.list_subscribe_user);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});
			
			btnLoadMore.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadmore();
				}
			});
		}

		return view;
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_cell_user_list, null);	
			}else{
				view = convertView;
			}

			TextView textUser = (TextView) view.findViewById(R.id.user);
			AvatarView avatar = (AvatarView)view.findViewById(R.id.user_avatar);
			
			User user = data.get(position);
//			textContent.setText(article.getText());
//			textTitle.setText(article.getTitle());
//			textAuthorName.setText(article.getAuthor().getName());
//			avatar.load(article.getAuthor());
			avatar.load(user);
			textUser.setText(user.getName());
			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public int getCount() {
			return data==null ? 0 : data.size();
		}
	};

	void onItemClicked(int position){
		User user = data.get(position);

		Intent itnt = new Intent(getActivity(), SubscribeListBookFragment.class);
		itnt.putExtra("data", user);

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	void reload(){
		Request request = Servelet.requestuildApi("subscribe")
				.get()
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{					
					final Page<User> data = new ObjectMapper()
							.readValue(arg1.body().string(),
									new TypeReference<Page<User>>(){});
					
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							SubscribeListUserFragment.this.page = data.getNumber();
							SubscribeListUserFragment.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(e.getMessage())
						.show();
					}
				});
			}
		});
	}

	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("正在加载...");
		
		Request request = Servelet.requestuildApi("subscribe/"+(page+1)).get().build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
				
				try{
					final Page<User> feeds = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<User>>() {});
					if(feeds.getNumber()>page){
						
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								if(data==null){
									data = feeds.getContent();
								}else{
									data.addAll(feeds.getContent());
								}
								page = feeds.getNumber();
								
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});
	}
}
