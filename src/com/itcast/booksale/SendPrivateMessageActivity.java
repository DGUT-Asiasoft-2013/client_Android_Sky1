package com.itcast.booksale;

import java.io.IOException;

import com.itcast.booksale.R;
import com.itcast.booksale.R.id;
import com.itcast.booksale.R.layout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.PrivateMessage;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.widgets.AvatarViewPrivateMessage;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.*;

public class SendPrivateMessageActivity extends Activity {
	List<PrivateMessage> privateMessageData;
	EditText editPrivateMessage;
	String privateMessgeDetail;// 私信信息
	public User privateMessageReceiver;// 私信接收者
	ListView chatListView;// 私信列表
	TextView chat_text;
	TextView textConnectToSb;// 抬头提示框
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_private_messge);
		// 绑定各组件
		Button chatSendButton = (Button) findViewById(R.id.chat_bottom_sendbutton);
		editPrivateMessage = (EditText) findViewById(R.id.chat_bottom_edittext);
		textConnectToSb = (TextView) findViewById(R.id.chat_contact_name);

		chatListView = (ListView) findViewById(R.id.chat_list);
		chatListView.setAdapter(adapter);

		// 一些数据的初始化
	
		privateMessageReceiver = (User) getIntent().getSerializableExtra("sendToReceiver");
		textConnectToSb.setText(getResources().getString(R.string.sendTO)+ privateMessageReceiver.getName() +getResources().getString(R.string.sendPivateMessage));

		//设置刷新线程启动
	
		chatSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendPrivateMessage();
			}
		});
	}

	class ViewHolderMe {
		// public ImageView imageView = null;// 头像在右
		AvatarViewPrivateMessage avatar = null;
		public TextView textView = null;
	}

	class ViewHolderOthers {
		// public ImageView imageView = null;// 头像在左
		AvatarViewPrivateMessage avatar = null;
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
			//Log.d("rr1", data.getPrivateText());
			ViewHolderMe holder1 = null;
			ViewHolderOthers holder2 = null;
			if (view == null) {
				switch (type) {
				case TYPE1:// 如果穿回来的数据中接受者是Intent中的接收者,那么就加载chat_listitem_me,即,是我发送的
					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_listitem_me, null);
					holder1 = new ViewHolderMe();
					holder1.textView = (TextView) view.findViewById(R.id.chatlist_text_me);
					holder1.avatar=(AvatarViewPrivateMessage) view.findViewById(R.id.chatlist_avatar_me);
					holder1.textView.setText(data.getPrivateText());
					holder1.avatar.load(data.getPrivateMessageSender());
					view.setTag(holder1);
					break;
				case TYPE2:// 如果穿回来的数据中接受者是Intent中的接收者,那么就加载chat_listitem_me,即,是别人发送的
					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_listitem_other, null);
					holder2 = new ViewHolderOthers();
					holder2.textView = (TextView) view.findViewById(R.id.chatlist_text_other);
					holder2.avatar = (AvatarViewPrivateMessage) view.findViewById(R.id.chatlist_avatar_other);
					holder2.textView.setText(data.getPrivateText());
					holder2.avatar.load(data.getPrivateMessageSender());
					view.setTag(holder2);
					break;
				}
			} else {
				switch (type) {
				case TYPE1:
					holder1 = (ViewHolderMe) view.getTag();
					holder1.textView = (TextView) view.findViewById(R.id.chatlist_text_me);
					holder1.avatar=(AvatarViewPrivateMessage) view.findViewById(R.id.chatlist_avatar_me);
					holder1.textView.setText(data.getPrivateText());
					holder1.avatar.load(data.getPrivateMessageSender());
					break;

				case TYPE2:
					holder2 = (ViewHolderOthers) view.getTag();
					holder2.textView = (TextView) view.findViewById(R.id.chatlist_text_other);
					holder2.avatar=(AvatarViewPrivateMessage) view.findViewById(R.id.chatlist_avatar_other);
					holder2.textView.setText(data.getPrivateText());
					holder2.avatar.load(data.getPrivateMessageSender());
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
			if (data.getPrivateMessageReceiver().getAccount().equals(privateMessageReceiver.getAccount())) {//如果消息的接收者等于要接收的人,那么这条信息就是我发送的
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
		if (privateMessgeDetail.length() == 0){
			Toast.makeText(this, "消息不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
			
		OkHttpClient client = Servelet.getOkHttpClient();
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("privateText", privateMessgeDetail)
				.addFormDataPart("receiverAccount", privateMessageReceiver.getAccount()).build();

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
				e.printStackTrace();

			}
		});

		editPrivateMessage.setText("");
	}

	
	//重新加载数据接口
	public void reload() {
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = Servelet.requestuildApi("findPrivateMessage/" + privateMessageReceiver.getId()).get().build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

				try {
					String rr = arg1.body().string();
				//	Log.d("rr", rr);
					final Page<PrivateMessage> pageData = new ObjectMapper().readValue(rr,
							new TypeReference<Page<PrivateMessage>>() {
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SendPrivateMessageActivity.this.page = pageData.getNumber();
							// 反转数据,使得最新的消息在最后
							List<PrivateMessage> lst = new ArrayList<PrivateMessage>();
							List<PrivateMessage> source = pageData.getContent();
							for (int i = source.size() - 1; i >= 0; i--) {
								lst.add(source.get(i));
							}

							SendPrivateMessageActivity.this.privateMessageData = lst;

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
						new AlertDialog.Builder(SendPrivateMessageActivity.this).setMessage(e.getMessage()).show();
					}
				});
			}
		});
	}
//使用handler类来实现定时刷新
	Handler handler = new Handler();
	boolean isVisible = false;//此标志为发送窗口是否可见
	
	@Override
	protected void onResume() {
		super.onResume();
		isVisible = true;//当为onResume时,将isVisible 设为true表示该窗口可见4
		refresh();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isVisible = false;//当Activity状态改为onPause时,窗口不可见
	}
	
	/*
	 * 2016年12月22日 18:58:21
	 * 函数功能:使用handler,将消息定时刷新
	 */
	void refresh(){
		
		reload();
		
		if(isVisible)
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		}, 3000);
	}
	
	//隐藏软键盘
	 @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     * 
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     * 
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
	
}
