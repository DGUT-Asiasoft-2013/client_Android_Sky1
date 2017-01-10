package com.itcast.booksale;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.PrivateMessage;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrivateMessageListActivity extends Activity {
	List<User> privateMessageData;
	String privateMessageDetail;
	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_message_list);

		ListView chatList = (ListView) findViewById(R.id.chat_list_connecters);
		chatList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);
			}
		});
		chatList.setAdapter(adapter);
	}

	BaseAdapter adapter = new BaseAdapter() {
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_listitem_connecters, null);
			}
			User user = privateMessageData.get(position);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.chatlist_avatar);
			// TextView text_message = (TextView)
			// view.findViewById(R.id.chatlist_text);
			TextView text_who = (TextView) view.findViewById(R.id.chatlist_who);
			// TextView text_time = (TextView)
			// view.findViewById(R.id.chatlist_time);
			
			avatar.load(user);
			text_who.setText(user.getAccount());
			

			return view;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public Object getItem(int position) {

			return privateMessageData.get(position);
		}

		@Override
		public int getCount() {
			if (privateMessageData == null)
				return 0;
			else
				return privateMessageData.size();
		}

	};

	public void onItemClicked(int position) {

		User user = privateMessageData.get(position);

		Intent itnt = new Intent(this, SendPrivateMessageActivity.class);
		itnt.putExtra("sendToReceiver", user);
		startActivity(itnt);
	}

	void save(PrivateMessage msg) {
		File dir = this.getDir("messages", MODE_PRIVATE);

		File userDir = new File(dir, "12");
		userDir.mkdir();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(userDir, "1"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File userMessageFile = userDir.listFiles()[0];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(userMessageFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PrivateMessage readmsg = (PrivateMessage) ois.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reload() {
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = Servelet.requestuildApi("getPrivateMessageList").get().build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

				try {
					String rr = arg1.body().string();
					Log.d("rr", rr);
					final Page<User> pageData = new ObjectMapper().readValue(rr, new TypeReference<Page<User>>() {
					});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if(pageData.getNumberOfElements()>0){

							PrivateMessageListActivity.this.privateMessageData = pageData.getContent();

							adapter.notifyDataSetInvalidated();
							}
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					Log.d("parse data error", e.getMessage());
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(PrivateMessageListActivity.this).setMessage(e.getMessage()).show();
					}
				});
			}
		});
	}

	// 使用handler类来实现定时刷新
	Handler handler = new Handler();
	boolean isVisible = false;// 此标志为发送窗口是否可见

	@Override
	protected void onResume() {
		super.onResume();
		isVisible = true;// 当为onResume时,将isVisible 设为true表示该窗口可见
	reload();
		 //refresh();
		// getCurrentUser();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isVisible = false;// 当Activity状态改为onPause时,窗口不可见
	}

	/*
	 * 2016年12月22日 18:58:21 函数功能:使用handler,将消息定时刷新
	 */
	void refresh() {

		reload();

		if (isVisible)
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			}, 2000);
	}

	public void getCurrentUser() {
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = Servelet.requestuildApi("me").get().build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				ObjectMapper obMapper = new ObjectMapper();
				currentUser = obMapper.readValue(arg1.body().string(), User.class);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {

			}
		});

	}
}
