package com.itcast.booksale.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.itcast.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.SubscribeListBookActivity;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.Subscribe;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubscribeListUserFragment extends Fragment {
	User user;
	View view;
	ListView listView;
	View btnLoadMore;
	TextView textLoadMore;
	String text = "卖家有更新是否提醒我";
	List<Subscribe> data;
	Boolean b;
	int page = 0;
	TextView gx;

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
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
					getActivity().runOnUiThread(new Runnable() {


						public void run() {
							new AlertDialog.Builder(getActivity())
							.setTitle(text)
							.setPositiveButton("否", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									b = false;
									gx.setText("不提醒更新");
									User saler=data.get(position).getId().getSaler();
									changeMode(saler);
								}
							})
							.setNegativeButton("是", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
										b = true;
										gx.setText("提醒更新");
									User saler=data.get(position).getId().getSaler();
									changeMode(saler);
								}
							})
							.show();
						}
					});
					return true;
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
	void changeMode(User saler){
		OkHttpClient client= Servelet.getOkHttpClient();
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("user_id", user.getId().toString())
				.addFormDataPart("saler_id", saler.getId().toString())
				.addFormDataPart("b", b.toString())
				.build();
		Request request =Servelet.requestuildApi("/subscribe/b")
				.post(body)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub

			}

		});
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
			gx = (TextView)view.findViewById(R.id.gx);

			User saler = data.get(position).getId().getSaler();
			avatar.load(saler);
			textUser.setText("卖家："+saler.getName());
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
		User saler = data.get(position).getId().getSaler();

		Intent itnt = new Intent(getActivity(), SubscribeListBookActivity.class);
		itnt.putExtra("data", saler);

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		OkHttpClient client= Servelet.getOkHttpClient();
		Request request =Servelet.requestuildApi("me")
				.method("get", null)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try{
					user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							reload();
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
			public void onFailure(final Call arg0, final IOException e) {

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

	void reload(){
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("user_id", user.getId().toString())
				.build();
		Request request = Servelet.requestuildApi(user.getId()+"/subscribe")
				.post(body)
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{					
					data =  new ObjectMapper().readValue(arg1.body().string(),new TypeReference<List<Subscribe>>(){});

					getActivity().runOnUiThread(new Runnable() {
						public void run() {
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
									//									data = feeds.getContent();
								}else{
									//									data.addAll(feeds.getContent());
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
