package com.itcast.booksale.fragment.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.servelet.Servelet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 这是购物车的页面显示
 */
public class Buy_book_bus_fragment extends Fragment {

	View view; // 购物车的页面

	View top_view; // 购物车顶部的按钮布局
	View bottom_view; // 购物车底端的按钮布局
	private ListView lv_shopping_bus; // 购物车的列表
	private List<Bookbus> list_shopping_bus = new ArrayList<Bookbus>();

	private CheckBox AllChoose_Btn; // 底部按钮栏的全选圆圈按钮
	private TextView count_money_tv; // 底部按钮栏的钱的总数

	private Button btn_count_all_bus; // 底部按钮栏的最右边 的结算按钮

	int page;

	private ImageView back_btn; // 顶部的返回按钮
	private TextView edit; // 顶部的“编辑”

	ShoppingBusAdapter adapter = new ShoppingBusAdapter();

	private int totalPrice; // 定义总价

	private BookAvatarView bookAvatar; // 图书照片

	/**
	 * 批量模式下用来记录当前选中状态
	 */
	private SparseArray<Boolean> mSelectedState = new SparseArray<Boolean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.buy_book_bus, null); // 加载购物车页面
			bottom_view = inflater.inflate(R.layout.buy_book_bus_bottom_btn, null); // 加载购物车底部按钮的布局
			top_view = inflater.inflate(R.layout.buy_bus_top_view_normal, null); // 加载购物车顶部按钮的布局

			// 获得购物车的界面的listview列表
			lv_shopping_bus = (ListView) view.findViewById(R.id.listview_shopping_bus);

			/**
			 * 顶部的实现
			 */
			back_btn = (ImageView) top_view.findViewById(R.id.back); // 顶部的返回按钮
			edit = (TextView) top_view.findViewById(R.id.subtitle); // 顶部的“编辑”

			/**
			 * 底部的实现
			 */
			// 底部全选的圆圈按钮
			AllChoose_Btn = (CheckBox) bottom_view.findViewById(R.id.isAllChoose_check);
			// 为底部全选圆圈按钮添加监听器
			AllchooseOnClickListener cAllchooseOnClickListener = new AllchooseOnClickListener();
			AllChoose_Btn.setOnClickListener(cAllchooseOnClickListener);

			// 底部按钮栏的钱的总数
			count_money_tv = (TextView) bottom_view.findViewById(R.id.count_money);
			// 底部按钮栏的最右边 的"结算"按钮
			btn_count_all_bus = (Button) bottom_view.findViewById(R.id.count_all_bus);
			// 添加监听器
			CountAllBusMoneyOnClickListener clickListener = new CountAllBusMoneyOnClickListener();
			btn_count_all_bus.setOnClickListener(clickListener);

			// 把顶部按钮栏加载在listview的底部，此必须在setAdapter()前
			lv_shopping_bus.addHeaderView(top_view);
			// 把底部按钮栏加载在listview的底部，此必须在setAdapter()前
			lv_shopping_bus.addFooterView(bottom_view);

			lv_shopping_bus.setAdapter(adapter); // 为lv_shopping_bus设置适配器
			lv_shopping_bus.setOnItemClickListener(adapter);
		}
		return view;
	}
	
	
	/**
	 * delete way
	 */
	
	public void onDeleted(List<Integer> lt) {
		for (int i = 0; i < list_shopping_bus.size(); i++) {
			//get the book_id
			long id=list_shopping_bus.get(i).getId().getBook().getId();
			for (int j = 0; j < lt.size(); j++) {
				//get the deleteId
				int deleteId = lt.get(j);
				if (id==deleteId) {
					list_shopping_bus.remove(i);
					i--;
					lt.remove(j);
					j--;
					
				}
				
				
			}
		}
		
		adapter.notifyDataSetChanged();
		mSelectedState.clear();
		totalPrice = 0;
		count_money_tv.setText("￥" + 0.00 + "元");
		AllChoose_Btn.setChecked(false);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		loadBookaddToBus();
	}

	// load bookbus
	public void loadBookaddToBus() {
		Request request = Servelet.requestuildApi("bookbus").get().build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {

					// 此为后台进行的，所以不能放在主线程里面进行
					String responseString = arg1.body().string();
					// 获得page类的对象
					final Page<Bookbus> pageBook;

					final ObjectMapper objectMapper = new ObjectMapper();
					Log.d("loading feed list", responseString);
					// 把解析下来的东西传入pageComment中
					pageBook = objectMapper.readValue(responseString, new TypeReference<Page<Bookbus>>() {
					});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 把解析下来的页数传给Comment_Listfragment
							page = pageBook.getNumber();
							// 把内容传给list
							list_shopping_bus = pageBook.getContent();

							// 刷新
							adapter.notifyDataSetInvalidated();
						}
					});
				} catch (JsonParseException e) {
					e.printStackTrace();

				} catch (JsonMappingException e) {
					e.printStackTrace();

				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							new AlertDialog.Builder(getActivity()).setTitle("bookbus失败ing").setMessage(e.toString())
									.show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {

						new AlertDialog.Builder(getActivity()).setTitle("bookbus innect 失败ing")
								.setMessage(arg1.toString()).show();
					}
				});
			}
		});
	}

	/*
	 * 这是底部按钮栏的最右边的"结算"按钮的监听器
	 */
	class CountAllBusMoneyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// 获得书的信息
			if (btn_count_all_bus.isSelected()) {
				// 如果这个按钮被按了,则弹出一个提示框，显示买的东西
				if (count_money_tv != null) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity()).setTitle("结算").setMessage("您购买了,需要多少钱")
									.setPositiveButton("确认付款", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											/**
											 * 付款操作
											 */
										}
									}).show();
						}
					});

				} else {
					// 取消订单

				}
			}
		}

	}

	/*
	 * 这是底部全选圆圈按钮的监听器
	 */

	class AllchooseOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (AllChoose_Btn.isChecked()) {
				// 如果"全选"按钮被选中，则继续执行下面的操作
				totalPrice = 0;
				if (list_shopping_bus != null) {
					mSelectedState.clear(); // 清全选状态
					if (list_shopping_bus.size() == 0) {
						// 如果list的大小为0，则返回
						return;

					}
					for (int i = 0; i < list_shopping_bus.size(); i++) {
						// 获得每行购物列表书的id
						int each_id = list_shopping_bus.get(i).getId().getBook().getId();

						mSelectedState.put(each_id, true); // 给每个购物行设置点中状态
						// 把全部价钱加起来
						totalPrice += list_shopping_bus.get(i).getId().getBook().getBooknumber()
								* list_shopping_bus.get(i).getId().getBook().getPrice();
					}

					// 刷新列表
					adapter.notifyDataSetChanged();
					count_money_tv.setText("￥:" + totalPrice + "元"); // 输入钱的总数

				}

			} else {
				// 否则
				totalPrice = 0; // 钱的总数为0
				mSelectedState.clear();
				// 刷新
				adapter.notifyDataSetChanged();
				count_money_tv.setText("￥:" + 0.00 + "元");

			}
		}
	}
	
	
	
	
	

	/**
	 * 下面为购物车的列表lv_shopping_bus的是适配器
	 */

	class ShoppingBusAdapter extends BaseAdapter implements OnItemClickListener {

		@Override
		public int getCount() {
			return list_shopping_bus == null ? 0 : list_shopping_bus.size();
		}

		@Override
		public Object getItem(int position) {
			return list_shopping_bus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View abView = convertView;
			ViewHolder holder = null; // create holder

			if (abView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				abView = inflater.inflate(R.layout.buy_book_bus_each_content, null);
				holder = new ViewHolder(abView);
				abView.setTag(holder);

			} else {
				holder = (ViewHolder) abView.getTag();

			}
			final Bookbus bookbus = list_shopping_bus.get(position); // 获得对应的购物车信息

			int each_book_id = list_shopping_bus.get(position).getId().getBook().getId(); // 获得对应的id
			boolean selected = mSelectedState.get(each_book_id, false); // 标记其的id的选择状态
			holder.each_item_choose_btn.setChecked(selected); // 设置每行选择按钮的选择状态

			// 设置书图片
			bookAvatar.load(Servelet.urlstring + bookbus.getId().getBook().getBookavatar());
			holder.shopname.setText(bookbus.getId().getBook().getTitle()); // design
																			// bookname
			String price = String.valueOf(bookbus.getId().getBook().getPrice());
			holder.each_bookprice.setText(price); // design price

			/**
			 * number's “+” 's ClickListener
			 */
			holder.each_item_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 获得id
					int id = bookbus.getId().getBook().getId();
					boolean selected = mSelectedState.get(id, false);
					// 设置数量
					// holder.each_item_num.setText(bookbus.getId().getBook().getBooknumber()
					// + 1);
					bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() + 1);
					notifyDataSetChanged(); // 刷新
					if (!selected) {
						// 选中了
						totalPrice += bookbus.getId().getBook().getPrice();
						count_money_tv.setText("￥" + totalPrice + "元"); // 设置总钱数

					} else {
						totalPrice = 0; // 钱的总数为0
						mSelectedState.clear();
						// 刷新
						adapter.notifyDataSetChanged();
					}
				}
			});

			/**
			 * number's “-” 's ClickListener
			 */
			holder.each_item_reduce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (bookbus.getId().getBook().getBooknumber() == 1) {
						// if have one book,return
						return;

					}
					int reduce_id = bookbus.getId().getBook().getId(); // 获得id
					boolean selected = mSelectedState.get(reduce_id, false);
					// 设置数量
					// holder.each_item_num.setText(bookbus.getId().getBook().getBooknumber()
					// - 1);
					bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() - 1);
					notifyDataSetChanged(); // 刷新

					if (!selected) {
						// 选中了
						totalPrice -= bookbus.getId().getBook().getPrice();
						count_money_tv.setText("￥" + totalPrice + "元"); // 设置总钱数

					} else {
						totalPrice = 0; // 钱的总数为0
						mSelectedState.clear();
						// 刷新
						adapter.notifyDataSetChanged();
					}
				}
			});
			return abView;
		}

		/**
		 * this is the view'properties
		 */

		class ViewHolder {
			TextView shopname;
			CheckBox each_item_choose_btn;
			TextView each_bookprice;
			TextView each_item_reduce;
			final TextView each_item_num;
			TextView each_item_add;

			public ViewHolder(View view) {

				shopname = (TextView) view.findViewById(R.id.shop_name); // 商店名字
				each_item_choose_btn = (CheckBox) view.findViewById(R.id.each_item_choose); // 圆圈选择按钮
				bookAvatar = (BookAvatarView) view.findViewById(R.id.book_image); // 书的图片
				each_bookprice = (TextView) view.findViewById(R.id.each_item_price); // 图书的价钱
				each_item_reduce = (TextView) view.findViewById(R.id.each_item_reduce); // 数量的“-”
				each_item_num = (TextView) view.findViewById(R.id.each_item_num); // number
				each_item_add = (TextView) view.findViewById(R.id.each_item_add); // number's“+”

			}
		}

		/*
		 * this is the list'item ItemClick
		 */

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (list_shopping_bus != null) {
				int listSize = list_shopping_bus.size();
				
				//because have the addHeaderView,so each item will position+1,so should reduce 1
				position--;
				final Bookbus bookbus = list_shopping_bus.get(position); // 获得对应的购物车信息
				ViewHolder holder = (ViewHolder) view.getTag();

				// get the item id
				int each_item_id = bookbus.getId().getBook().getId();
				// design selected as !
				boolean selected = !mSelectedState.get(each_item_id, false);

				holder.each_item_choose_btn.toggle(); // design choose

				if (selected) {
					// if selected is true,array--mSelectedState design as true
					mSelectedState.put(each_item_id, true);
					totalPrice += bookbus.getId().getBook().getBooknumber() * bookbus.getId().getBook().getPrice();
				} else {
					// delete this item
					mSelectedState.delete(each_item_id);
					// reduce the totalPrice
					totalPrice -= bookbus.getId().getBook().getBooknumber() * bookbus.getId().getBook().getPrice();
				}

				count_money_tv.setText("￥" + totalPrice + "");

				if (mSelectedState.size() == list_shopping_bus.size()) {
					/**
					 * if Array's SelectedState--mSelectedState's size
					 * ==list_shopping_bus.size(),to design buttom
					 * btn--AllChoose_Btn as true
					 */

					AllChoose_Btn.setChecked(true);

				} else {
					AllChoose_Btn.setChecked(false);
				}
			}
		}

	}
}
