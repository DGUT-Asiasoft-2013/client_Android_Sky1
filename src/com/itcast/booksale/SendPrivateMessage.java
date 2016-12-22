package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.PrivateMessage;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.*;

public class SendPrivateMessage extends Activity {
	List<PrivateMessage> privateMessageData;
	EditText editPrivateMessage;
	String privateMessgeDetail;// 私信信息
	User pivateMessageReceiver;// 私信接收者
	final String chatType[] = { "send", "receive" };// 消息的类型,"send"表示是发送的,"receive"表示是接收的
	ListView chatListView;
	TextView chat_text;
	TextView textConnectToSb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_private_messge);

		Button chatSendButton = (Button) findViewById(R.id.chat_bottom_sendbutton);
		editPrivateMessage = (EditText) findViewById(R.id.chat_bottom_edittext);
		textConnectToSb = (TextView) findViewById(R.id.chat_contact_name);

		chatListView = (ListView) findViewById(R.id.chat_list);
		chatListView.setAdapter(adapter);

		chatSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendPrivateMessage();
			}
		});
	}

	class ViewHolder1 {
		//public ImageView imageView = null;// 头像在右
		public TextView textView = null;
	}

	class ViewHolder2 {
		//public ImageView imageView = null;// 头像在左
		public TextView textView = null;
	}

	BaseAdapter adapter = new BaseAdapter() {

		private final int TYPE1 = 0;
		private final int TYPE2 = 1;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View view, ViewGroup parent) {

			PrivateMessage data = privateMessageData.get(position);
			int type = getItemViewType(position);
			Log.d("rr1", data.getPrivateText());
			ViewHolder1 holder1 = null;
			ViewHolder2 holder2 = null;
			if (view == null) {
				switch (type) {
				case TYPE1:// 如果穿回来的数据中接受者是Intent中的接收者,那么就加载chat_listitem_me,即,是我发送的
					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_listitem_me, null);
					 holder1 = new ViewHolder1();
						holder1.textView = (TextView) view.findViewById(R.id.chatlist_text_me);
						holder1.textView.setText(data.getPrivateText());
					  view.setTag(holder1);
					break;
				case TYPE2:// 如果穿回来的数据中接受者是Intent中的接收者,那么就加载chat_listitem_me,即,是别人发送的
					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_listitem_other, null);
					holder2 = new ViewHolder2();
					holder2.textView = (TextView) view.findViewById(R.id.chatlist_text_other);
					holder2.textView.setText(data.getPrivateText());
					view.setTag(holder2);
					break;
				}
			} else {
				switch (type) {
				case TYPE1:
					holder1 = (ViewHolder1) view.getTag();
					holder1.textView = (TextView) view.findViewById(R.id.chatlist_text_me);
					holder1.textView.setText(data.getPrivateText());
					break;

				case TYPE2:
					holder2 = (ViewHolder2) view.getTag();
					holder2.textView = (TextView) view.findViewById(R.id.chatlist_text_other);
					holder2.textView.setText(data.getPrivateText());
					break;
				}
			}

			return view;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return privateMessageData.get(position);
		}

		@Override
		public int getCount() {
			if (privateMessageData == null)
				return 0;
			else
				return privateMessageData.size();
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		};

		@Override
		public int getItemViewType(int position) {
	
				PrivateMessage data = privateMessageData.get(position);
				Log.d("rr2", data.getPrivateText());
				if (data.getPrivateMessageReceiver().getAccount().equals("hh")) {
					return TYPE1;
				} else {
					return TYPE2;
				}

		
		};

	};
	protected int page;

	// "发送"按钮监听实现接口
	public void sendPrivateMessage() {
		/*
		 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
		 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例 ，并且不能发送空消息。
		 */
		privateMessgeDetail = (editPrivateMessage.getText() + "").toString();// 获得私信的内容
		if (privateMessgeDetail.length() == 0)
			return;
		OkHttpClient client = Servelet.getOkHttpClient();
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("privateText", privateMessgeDetail)
				.addFormDataPart("receiverAccount", "hh").addFormDataPart("chatType", chatType[0]).build();

		Request request = Servelet.requestuildApi("privateMessage").method("POST", body).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				Log.d("privateRS", arg1.body().string());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						/*
						 * 更新数据列表，
						 */						
						reload();
					

					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				Log.d("privateRF", e.getMessage());

			}
		});

		editPrivateMessage.setText("");
	}

	public void reload() {
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = Servelet.requestuildApi("findPrivateMessage" + "/1").get().build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

				try {
					String rr = arg1.body().string();
					Log.d("rr", rr);
					final Page<PrivateMessage> pageData = new ObjectMapper().readValue(rr,
							new TypeReference<Page<PrivateMessage>>() {
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SendPrivateMessage.this.page = pageData.getNumber();
							//反转数据,使得最新的消息在最后
							List<PrivateMessage> lst = new ArrayList<>();
							List<PrivateMessage> source = pageData.getContent();
							for(int i=source.size()-1; i>=0; i--){
								lst.add(source.get(i));
							}
							
							SendPrivateMessage.this.privateMessageData = lst;
							

							adapter.notifyDataSetInvalidated();
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
						new AlertDialog.Builder(SendPrivateMessage.this).setMessage(e.getMessage()).show();
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

}