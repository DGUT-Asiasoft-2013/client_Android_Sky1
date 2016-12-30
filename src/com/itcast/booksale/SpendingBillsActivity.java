package com.itcast.booksale;

import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.*;

public class SpendingBillsActivity extends Activity {

	List<OrderLists> data;
	View headView;
	final String[] state = { "待付款", "交易关闭", "交易完成" };
	View btnLoadMore;
	TextView textLoadMore;
	int page = 0;

	// TextView textMonth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spending_bills);
		ListView billsList = (ListView) findViewById(R.id.list_bills_list);
		btnLoadMore = LayoutInflater.from(this).inflate(R.layout.widget_load_more_button, null);

		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
		billsList.addFooterView(btnLoadMore);
		/*
		 * headView =
		 * LayoutInflater.from(SpendingBillsActivity.this).inflate(R.layout.
		 * widget_billslist_month, null); textMonth = (TextView)
		 * findViewById(R.id.text_bills_date_month);// FootBood的月份提示
		 * billsList.addHeaderView(headView);
		 */
		billsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				goToOrderList(position);// 跳转到订单详情
			}
		});

		billsList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				deleteBillList(position);// 长按弹出删除指定订单列表功能
				return false;
			}
		});

		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		billsList.setAdapter(adapter);

	}

	// 订单详情列表的适配器
	BaseAdapter adapter = new BaseAdapter() {
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spending_bills_listitem, null);
			}
			OrderLists listData = data.get(position);

			TextView textDate = (TextView) convertView.findViewById(R.id.text_bills_date);// 订单的日期,具体到月日,如12-28
			AvatarView avatar = (AvatarView) convertView.findViewById(R.id.avatar_bills_saler);// 订单的销售者
			TextView textMonery = (TextView) convertView.findViewById(R.id.text_bills_monery);// 订单的金额
			TextView textDetail = (TextView) convertView.findViewById(R.id.text_bills_detail);// 订单的简略详情
			TextView textState = (TextView) convertView.findViewById(R.id.text_bills_sate);// 订单的状态

			// 为各个Item设定数值
			textDate.setText(DateFormat.format("MM-dd", listData.getCreateDate()));

			avatar.load(listData.getBook().getUser());

			textMonery.setText(String.valueOf(listData.getPayMoney()));

			// 判断订单详情长度是否大于某值,大于则截取
			if (listData.getBook().getSummary().length() > 12) {
				textDetail.setText(listData.getBook().getSummary().subSequence(0, 12) + "...");
			} else {
				textDetail.setText(listData.getBook().getSummary());
			}

			// 判断不同的订单状态

			switch (getBillsState(listData.getFinish())) {
			case 1:
				textState.setText("待付款");
				textState.setTextColor(0xD2691E);
				break;
			case 2:
				textState.setText("交易关闭");
				textState.setTextColor(0xD9D9D9);
				break;
			case 0:
				textState.setText("");
				break;
			default:
				textState.setText(listData.getFinish());
				break;

			}

			/*
			 * SimpleDateFormat dateFormater = new SimpleDateFormat("MM月");
			 * textMonth.setText(dateFormater.format(listData.getCreateDate()));
			 */
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public int getCount() {
			if (data == null) {
				return 0;
			} else
				return data.size();
		}

	};

	// onResume
	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}

	void reload() {
		Request request = Servelet.requestuildApi("orders").get().build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				String dataRS = arg1.body().string();
				Log.d("dataRS", dataRS);
				try {
					final Page<OrderLists> pageData = new ObjectMapper().readValue(dataRS,
							new TypeReference<Page<OrderLists>>() {
							});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SpendingBillsActivity.this.data = pageData.getContent();
							SpendingBillsActivity.this.page = pageData.getNumber();

							adapter.notifyDataSetChanged();
						}
					});
				} catch (Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(SpendingBillsActivity.this, "请求账单列表失败了", Toast.LENGTH_SHORT).show();
						}
					});
					e.printStackTrace();

				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SpendingBillsActivity.this, "请求账单列表失败了", Toast.LENGTH_SHORT).show();
					}
				});
			}

		});

	}

	public int getBillsState(String state) {
		if (state.equals(this.state[0])) {
			return 1;

		} else if (state.equals(this.state[1])) {
			return 2;
		} else if (state.equals(this.state[2])) {
			return 0;
		} else
			return 3;
	}

	public void goToOrderList(int position) {
		OrderLists orderData = data.get(position);
		Intent itnt  = new Intent(this , BillDetailActivity.class);
		itnt.putExtra("order", orderData);
		startActivity(itnt);

	}

	public void deleteBillList(int postition) {

	}

	void loadMore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中…");
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = Servelet.requestuildApi("orders/" + (page + 1)).get().build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final String responseString = arg1.body().string();
					final Page<OrderLists> feeds = new ObjectMapper().readValue(responseString,
							new TypeReference<Page<OrderLists>>() {
							});
					if (feeds.getNumberOfElements() > 0 && feeds.getNumber() > page) {

						runOnUiThread(new Runnable() {
							public void run() {
								btnLoadMore.setEnabled(true);
								textLoadMore.setText("加载更多");
								if (data == null) {
									data = feeds.getContent();
								} else {
									data.addAll(feeds.getContent());
								}
								page = feeds.getNumber();
								adapter.notifyDataSetChanged();
							}
						});
					} else {

						runOnUiThread(new Runnable() {
							public void run() {
								btnLoadMore.setEnabled(false);
								textLoadMore.setText("没有更多了");

							}
						});
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btnLoadMore.setEnabled(false);
						textLoadMore.setText("没有更多了");
					}
				});
			}
		});

	}
}
