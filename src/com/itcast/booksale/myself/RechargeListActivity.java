package com.itcast.booksale.myself;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.Money;
import com.itcast.booksale.entity.OrderLists;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.entity.Subscribe;
import com.itcast.booksale.entity.User;
import com.itcast.booksale.fragment.pages.SubscribeListUserFragment;
import com.itcast.booksale.servelet.Servelet;
import com.lidroid.xutils.view.ResLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class RechargeListActivity extends Activity{

	ListView rechargeListView;
	
	List<Money> recharge;
	
	//private List<Money> list=new ArrayList<Money>();
	
	 User user;
	 
	 int page=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge_list);
		
		rechargeListView=(ListView) findViewById(R.id.recharge_list);
		
		rechargeListView.setAdapter(adapter);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Request request=Servelet.requestuildApi("me")
				.method("get", null)
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
			try{
				user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
				
				RechargeListActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						reload();
					}
				});
			}catch (final Exception e) {
				// TODO: handle exception
				RechargeListActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(RechargeListActivity.this)
						.setMessage(e.getMessage())
						.show();
					}
				});
			}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				// TODO Auto-generated method stub
				RechargeListActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(RechargeListActivity.this)
						.setMessage(e.getMessage())
						.show();
					}
				});
			}
		});
		
		//reload();
	}
	

	void reload(){
		
		/*Toast.makeText(RechargeListActivity.this, user.getAccount(), Toast.LENGTH_SHORT).show();
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("user_id", user.getAccount())
				.build();*/
		//Log.d("=====����======", user.getAccount());

		Request request=Servelet.requestuildApi(user.getAccount()+"/money/list")
				//Request request=Servelet.requestuildApi(user.getId()+"/money/list")
				.get()
				.build();
		
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				String ar = arg1.body().string();
				//Log.d("=====����======", ar);
				try{					
					final Page<Money> data =  new ObjectMapper().readValue(ar,
							new TypeReference<Page<Money>>(){});

					RechargeListActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							RechargeListActivity.this.recharge = data.getContent();
							RechargeListActivity.this.page = data.getNumber();
							
							adapter.notifyDataSetChanged();
						}
					});
				}catch(final Exception e){
					RechargeListActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(RechargeListActivity.this)
							.setMessage(e.getMessage())
							.show();
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				// TODO Auto-generated method stub
				RechargeListActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(RechargeListActivity.this)
						.setMessage(e.getMessage())
						.show();
					}
				});
			}
		});
				
	}
	
	BaseAdapter adapter=new BaseAdapter() {
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.activity_list_recharge, null);
			} 
			
			Money money = recharge.get(position);

			TextView textDate = (TextView) convertView.findViewById(R.id.recharge_createtime);
			TextView rechargeMoney=(TextView) convertView.findViewById(R.id.recharge_money);
			
			String sum=String.valueOf(money.getRecharge());
			rechargeMoney.setText("+"+sum);

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", money.getCreateDate()).toString();
			textDate.setText(dateStr);

			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return recharge.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(recharge==null){
				return 0;
			}else{
				return recharge.size();
			}
		}
	};
	
	
}
