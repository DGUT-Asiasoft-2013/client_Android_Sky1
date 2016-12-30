package com.itcast.booksale;

import com.itcast.booksale.fragment.widgets.AvatarView;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.*;

public class SpendingBillsActivity extends Activity {

	List<OrderLists> data;
	View headView;
	final String[] state = { "待付款", "交易关闭", "交易完成" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spending_bills);
		ListView billsList = (ListView) findViewById(R.id.list_bills_list);
	headView = LayoutInflater.from(SpendingBillsActivity.this).inflate(R.layout.widget_billslist_month, null);
		billsList.addHeaderView(headView);
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
		billsList.setAdapter(adapter);

	}

	// 订单详情列表的适配器
	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getParent()).inflate(R.layout.spending_bills_listitem, null);
			}
			OrderLists listData = data.get(position);

			TextView textDate = (TextView) convertView.findViewById(R.id.text_bills_date);// 订单的日期,具体到月日,如12-28
			AvatarView avatar = (AvatarView) convertView.findViewById(R.id.avatar_bills_saler);// 订单的销售者
			TextView textMonery = (TextView) convertView.findViewById(R.id.text_bills_monery);// 订单的金额
			TextView textDetail = (TextView) convertView.findViewById(R.id.text_bills_detail);// 订单的简略详情
		//	TextView textState = (TextView) convertView.findViewById(R.id.text_bills_date);// 订单的状态
			TextView textMonth = (TextView) convertView.findViewById(R.id.text_bills_date_month);// FootBood的月份提示

			// 为各个Item设定数值
			textDate.setText(DateFormat.format("MM-dd", listData.getCreateDate()));
			avatar.load(listData.getUser());
			textMonery.setText(String.valueOf(listData.getPayMoney()));

			// 判断订单详情长度是否大于某值,大于则截取
			if (listData.getBook().getSummary().length() > 12) {
				textDetail.setText(listData.getBook().getSummary().subSequence(0, 12) + "...");
			} else {
				textDetail.setText(listData.getBook().getSummary());
			}
/*
			// 判断不同的订单状态
			switch (listData.getFinish()) {
			case state[0]:
				textState.setText("待付款");
				textState.setTextColor(0xD2691E);
				break;
			case state[1]:
				textState.setText("交易关闭");
				textState.setTextColor(0xD9D9D9);
				break;
			default:
				textState.setText("");
				break;

			}*/
			textMonth.setText(DateFormat.format("MM月 ", listData.getCreateDate()));

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

	private void reload() {
		Request request = Servelet.requestuildApi("orders").build();
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				final Page<OrderLists> pageData = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<OrderLists>>() {
				});
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						SpendingBillsActivity.this.data = pageData.getContent();
						adapter.notifyDataSetChanged();
					}
				});
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

	public void goToOrderList(int position) {
		

	}

	public void deleteBillList(int postition) {

		
	}
}
