package com.itcast.booksale.fragment.widgets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.OrdersActivity;
import com.itcast.booksale.R;
import com.itcast.booksale.entity.Bookbus;
import com.itcast.booksale.entity.Page;
import com.itcast.booksale.servelet.Servelet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 这是购物车的页面显示
 */
public class Buy_book_bus_fragment extends Fragment {

	View abView; // 购物车的页面

	View top_view; // 购物车顶部的按钮布局
	View bottom_view; // 购物车底端的按钮布局
	private ListView lv_shopping_bus; // 购物车的列表
	private List<Bookbus> list_shopping_bus = new ArrayList<Bookbus>();

	private CheckBox AllChoose_Btn; //底部按钮栏的全选圆圈按钮
	private TextView count_money_tv; // 底部按钮栏的钱的总数
	private Button btn_count_all_bus; //  底部按钮栏的最右边 的结算按钮
	private LinearLayout iLayout_buttom_money;// 底部按钮的购物车和合计的布局
	private Button delete_book_from_bookbus;// 底部删除按钮

	List<Bookbus> bookbusList = new ArrayList<Bookbus>();

	int page;

	private ImageView back_btn; // 顶部的返回按钮
	private TextView edit; // 顶部的“编辑”

	private float totalPrice; // 定义总价

	private BookAvatarView bookAvatar; // 图书照片

	private boolean isDelete; //  是否可删除模式

	/**
	 *  批量模式下用来记录当前选中状态
	 */
	private SparseArray<Boolean> mSelectedState = new SparseArray<Boolean>();

	/**
	 * Batch mode is used to record the current The number of books in the
	 * shopping cart
	 */
	private SparseArray<Integer> mSelectedNum = new SparseArray<Integer>();

	String[] ab = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private boolean isRemoveBookFromBus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (abView == null) {
			abView = inflater.inflate(R.layout.buy_book_bus, null); // 加载购物车页面
			bottom_view = inflater.inflate(R.layout.buy_book_bus_bottom_btn, null); //加载购物车底部按钮的布局
			top_view = inflater.inflate(R.layout.buy_bus_top_view_normal, null); // 加载购物车顶部按钮的布局

			initView(); // initialization
			lv_shopping_bus.setAdapter(adapter); // set Listener for
			// lv_shopping_bus
		}
		return abView;
	}

	public void initView() {
		//获得购物车的界面的listview列表
		lv_shopping_bus = (ListView) abView.findViewById(R.id.listview_shopping_bus);

		/**
		 *  顶部的实现
		 */
		back_btn = (ImageView) top_view.findViewById(R.id.back); //  顶部的返回按钮
		edit = (TextView) top_view.findViewById(R.id.subtitle);// 顶部的“编辑”
		//		EditOnClickListener listener = new EditOnClickListener();
		edit.setOnClickListener(listener);

		/**
		 * 底部的实现
		 */
		// 底部全选的圆圈按钮
		AllChoose_Btn = (CheckBox) bottom_view.findViewById(R.id.isAllChoose_check);
		// 为底部全选圆圈按钮添加监听器
		AllChoose_Btn.setOnClickListener(cAllchooseOnClickListener);

		// bottom's btn'column 's moneny's total
		count_money_tv = (TextView) bottom_view.findViewById(R.id.count_money);
		// bottom's btn'column 's rightest count btn
		btn_count_all_bus = (Button) bottom_view.findViewById(R.id.count_all_bus);
		// add Listener
		btn_count_all_bus.setOnClickListener(clickListener);
		// bottom's btn and count's layout
		iLayout_buttom_money = (LinearLayout) bottom_view.findViewById(R.id.layout_buttom_money);
		// bottom's delete book from bookbus'btn
		delete_book_from_bookbus = (Button) bottom_view.findViewById(R.id.delete_book_from_bookbus);
		delete_book_from_bookbus.setOnClickListener(deletebookFromBookbusListener);

		// top's btn lan add to listview's bottom,his must before setAdapter()
		lv_shopping_bus.addHeaderView(top_view);
		// bottom's btn lan add to listview's top,his must before setAdapter()
		lv_shopping_bus.addFooterView(bottom_view);

	}

	// get the seleted id
	public List<Integer> getSeletedId() {
		ArrayList<Integer> seletedId = new ArrayList<Integer>();
		for (int i = 0; i < mSelectedState.size(); i++) {
			// Circular traversal the mSelectedState,sure which is it at
			if (mSelectedState.valueAt(i)) {
				// if it at position--i,seletedId add this id
				seletedId.add(mSelectedState.keyAt(i));

			}

		}
		return seletedId;
	}

	/**
	 * delete_book_from_bookbus' listener
	 * 
	 * @author Administrator
	 *
	 */
	OnClickListener deletebookFromBookbusListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isDelete) {
				// if it can delete,get this id
				List<Integer> ids = getSeletedId();
				onDeleted(ids); // delete id

			} else {
				Toast.makeText(getActivity(), "there is nothing to delete", Toast.LENGTH_SHORT).show();
			}
		}

	};

	/*
	 * delete book from bookbus
	 */
	public void RemoveFromBookBus(int bookid) {
		// client
		OkHttpClient client = Servelet.getOkHttpClient();
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("isRemoveBookFromBus", String.valueOf(!isRemoveBookFromBus)).build();
		Request request = Servelet.requestuildApi("book/" + bookid + "/removefrombookbus").post(body).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {

					final String string = arg1.body().string();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							new AlertDialog.Builder(getActivity()).setTitle("success to remove from bookbus")
							.setMessage(string).setPositiveButton("ok", null).show();

						}
					});
				} catch (Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(), getResources().getString(R.string.yichufrombus), Toast.LENGTH_SHORT).show();
						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								new AlertDialog.Builder(getActivity()).setTitle("failed to remove bookbus")
								.setMessage(arg1.toString()).setPositiveButton("ok", null).show();

							}
						});
					}
				});

			}
		});

	}

	/**
	 * top's"edit" 's ClickListener
	 */
	OnClickListener listener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			isDelete = !isDelete; // design the model
			if (isDelete) {
				// if it is delete model
				edit.setText(getResources().getString(R.string.finish));
				iLayout_buttom_money.setVisibility(View.GONE); // design layout
				// as INVISIBLE
				delete_book_from_bookbus.setVisibility(abView.VISIBLE); // design
				// delete
				// as
				// VISIBLE
			} else {
				edit.setText(getResources().getString(R.string.edit));
				iLayout_buttom_money.setVisibility(View.VISIBLE); // design
				// layout as
				// INVISIBLE
				delete_book_from_bookbus.setVisibility(abView.GONE); // design
				// delete
				// as
				// VISIBLE
			}

		}

	};

	/**
	 * delete method
	 */

	public void onDeleted(List<Integer> lt) {
		for (int i = 0; i < list_shopping_bus.size(); i++) {
			// get the book_id
			int id = list_shopping_bus.get(i).getId().getBook().getId();
			for (int j = 0; j < lt.size(); j++) {
				// get the deleteId
				int deleteId = lt.get(j);
				if (id == deleteId) {
					RemoveFromBookBus(id); // remove the book from bookbus
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
		count_money_tv.setText( getResources().getString(R.string.rmb) + 0.00 +  getResources().getString(R.string.yuan));
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

					// 此为后台进行的，所以不能放在主线程里面进行
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

							new AlertDialog.Builder(getActivity()).setTitle("bookbus innect 失败ing").setMessage(e.toString())
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

						new AlertDialog.Builder(getActivity()).setTitle("bookbus innect 澶辫触ing")
						.setMessage(arg1.toString()).show();
					}
				});
			}
		});
	}

	int[] books_id;

	/*
	 * 这是底部按钮栏的最右边的"结算"按钮的监听器
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得书的信息
			if (btn_count_all_bus.isClickable()) {
				// Log.i("--------------检测----------",
				// "--------结算按钮的显示1----------");
				// 如果这个按钮被按了,则弹出一个提示框，显示买的东西
				if (count_money_tv != null) {

					for (int i = 0; i < list_shopping_bus.size(); i++) {

						// Circular traversal the list_shopping_bus
						// Log.i("--------------检测----------",
						// list_shopping_bus.get(i).getId().getBook().getText());
						if (mSelectedState.get(list_shopping_bus.get(i).getId().getBook().getId(),false)) {

							// if the one is seleted
							final Bookbus bookbus = list_shopping_bus.get(i);
							String string = bookbus.getId().getBook().getTitle();
							bookbusList.add(bookbus);

							// get the random ab's character
							Random random = new Random();
							int index = random.nextInt(ab.length);
							final String order_letter = ab[index];

							// Log.i("--------------检测----------", string);
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.tijiao ))
									.setMessage(getResources().getString(R.string.you ) + bookbus.getId().getBook().getEditDate() + getResources().getString(R.string.wantbuy )
											+ bookbus.getId().getBook().getTitle())
									.setPositiveButton(getResources().getString(R.string.sure ), new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent intent = new Intent(getActivity(), OrdersActivity.class);
											// translate the bookbus to
											// the OrdersActivity
											intent.putExtra("bookbus", (Serializable)bookbusList);

											// get the allpay money
											String AllPay = count_money_tv.getText().toString();
											// translate the AllPay to
											// the OrdersActivity
											intent.putExtra("AllPay", AllPay);

											// get the order_number
											String order_number = order_letter
													+ bookbus.getId().getBook().getIsbn()
													+ bookbus.getId().getBook().getId();

													// translate the
													// order_number to the
													// OrdersActivity
													intent.putExtra("order_number", order_number);
													startActivity(intent);
													bookbusList.clear();
													dialog.dismiss();
//													AllChoose_Btn.setClickable(false);
//													AllChoose_Btn.setBackgroundResource(R.drawable.normal_choose_all);
													count_money_tv.setText(getResources().getString(R.string.rmb) + 0.00 + getResources().getString(R.string.yuan));//After the settlement is complete, the total price of the returned cart will be cleared to zero
													// Log.i("------------检测----------",
													// "----------------啦啦啦啦啦-----------");
												}
											}).setNegativeButton(getResources().getString(R.string.no), null).show();

								}
							});

						}
					}
					
				} else {
					// 取消订单
					Toast.makeText(getActivity(), getResources().getString(R.string.cancle), Toast.LENGTH_SHORT).show();
					

				}
			}
		}

	};

	/*
	 * 这是底部全选圆圈按钮的监听器
	 */

	OnClickListener cAllchooseOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (AllChoose_Btn.isChecked()) {
				// 如果"全选"按钮被选中，则继续执行下面的操作
				totalPrice = 0;
				if (list_shopping_bus != null) {
					mSelectedState.clear(); // 清全选状态
					if (list_shopping_bus.size() == 0) {
						//  如果list的大小为0，则返回
						return;

					}
					for (int i = 0; i < list_shopping_bus.size(); i++) {
						// 获得每行购物列表书的id
						int each_id = list_shopping_bus.get(i).getId().getBook().getId();

						mSelectedState.put(each_id, true); //  给每个购物行设置点中状态

						//This line has a value after the "+" or "-" or radio button is clicked
						int each_num = mSelectedNum.get(each_id);
						// 把全部价钱加起来
						totalPrice += each_num * list_shopping_bus.get(i).getId().getBook().getPrice();
					}

					// 刷新列表
					adapter.notifyDataSetChanged();
					count_money_tv.setText(getResources().getString(R.string.rmb) + totalPrice + getResources().getString(R.string.yuan)); // 输入钱的总数

				}

			} else {
				// 否则
				totalPrice = 0; // 钱的总数为0
				mSelectedState.clear();
				//				mSelectedNum.put(list_shopping_bus.get(i), 1);
				// 刷新
				adapter.notifyDataSetChanged();
				count_money_tv.setText(getResources().getString(R.string.rmb) + 0.00 + getResources().getString(R.string.yuan));

			}
		}
	};

	/**
	 * 下面为购物车的列表lv_shopping_bus的是适配器
	 */

	BaseAdapter adapter = new BaseAdapter() {

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
			View abView = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				abView = inflater.inflate(R.layout.buy_book_bus_each_content, null);

			} else {
				abView = convertView;
			}

			TextView shopname = (TextView) abView.findViewById(R.id.shop_name); //  商店名字
			CheckBox each_item_choose_btn = (CheckBox) abView.findViewById(R.id.each_item_choose); // 圆圈选择按钮
			bookAvatar = (BookAvatarView) abView.findViewById(R.id.book_image); //书的图片
			TextView each_bookprice = (TextView) abView.findViewById(R.id.each_item_price); //  图书的价钱
			TextView each_item_reduce = (TextView) abView.findViewById(R.id.each_item_reduce); // 数量的“-”
			final TextView each_item_num = (TextView) abView.findViewById(R.id.each_item_num); // number
			TextView each_item_add = (TextView) abView.findViewById(R.id.each_item_add); // number's“+”

			final Bookbus bookbus = list_shopping_bus.get(position); // 获得对应的购物车信息

			int each_book_id = bookbus.getId().getBook().getId(); //获得对应的id
			boolean selected = mSelectedState.get(each_book_id, false); // The selected state of its id
			int selectedNum = mSelectedNum.get(each_book_id, 1);// Marks the
			// current
			// number state
			// of its id

			final int count = selectedNum;      //make the selectedNum Assignment for count

			String number = String.valueOf(selectedNum);
			Log.i("--------------检测-----------", number);
			each_item_num.setText(number);
			each_item_choose_btn.setChecked(selected); // 设置每行选择按钮的选择状态

			// 设置书图片
			bookAvatar.load(Servelet.urlstring + bookbus.getId().getBook().getBookavatar());
			shopname.setText(bookbus.getId().getBook().getTitle()); // design
			// bookname
			String price = String.valueOf(bookbus.getId().getBook().getPrice());
			each_bookprice.setText(price); // design price
			// Log.i("--------------检测-----------", "-------+号运行中");

			/**
			 * number's "+" 's ClickListener
			 */
			each_item_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 获得id
					int id = bookbus.getId().getBook().getId();
					boolean selected = mSelectedState.get(id, false);
					// count=0;
					if (!selected) {
						mSelectedNum.put(id, count + 1);// 设置其的id的当前数量状态
						// int selectedNum=mSelectedNum.get(id, count);
						// String number = String.valueOf(selectedNum);
						// Log.i("--------------检测----------", number);
						// set number
						// String add_number =
						// String.valueOf(bookbus.getId().getBook().getBooknumber()
						// + 1);
						String add_number = String.valueOf(count);
						each_item_num.setText(add_number);

						// because client add 1,so Backstage's booknumber-1
						bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() - 1);
						notifyDataSetChanged(); // notify
						// 选中了
						totalPrice += bookbus.getId().getBook().getPrice();
						//						count_money_tv.setText("￥:" + (totalPrice + bookbus.getId().getBook().getPrice()) + "元"); // 设置总钱数
						mSelectedState.clear();
					} else {
						totalPrice = 0; // 钱的总数为0
						mSelectedState.clear();
						// 刷新
						adapter.notifyDataSetChanged();
					}
				}
			});

			/**
			 * number's "-" 's ClickListener
			 */
			each_item_reduce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (bookbus.getId().getBook().getBooknumber() == 1) {
						// if have one book,return
						return;

					}
					int reduce_id = bookbus.getId().getBook().getId(); // 获得id
					boolean selected = mSelectedState.get(reduce_id, false);

					if (!selected) {
						mSelectedNum.put(reduce_id, count - 1);// 设置其的id的当前数量状态
						// 设置数量
						// String reduce_number =
						// String.valueOf(bookbus.getId().getBook().getBooknumber()
						// - 1);
						String reduce_number = String.valueOf(count);
						each_item_num.setText(reduce_number);

						// because client reduce 1,so Backstage's booknumber+1
						bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() + 1);
						notifyDataSetChanged(); // 刷新
						// 选中了
						totalPrice -= bookbus.getId().getBook().getPrice();
						//						count_money_tv.setText("￥:" + (totalPrice + bookbus.getId().getBook().getPrice()) + "鍏�"); // 设置总钱数
						mSelectedState.clear();
					} else {
						totalPrice = 0; // 钱的总数为0
						mSelectedState.clear();
						// 刷新
						adapter.notifyDataSetChanged();
					}
				}
			});

			each_item_choose_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (list_shopping_bus != null) {
						int listSize = list_shopping_bus.size();

						// because have the addHeaderView,so each item will
						// position+1,so should reduce 1
						// position--;

						// get the item id
						int each_item_id = list_shopping_bus.get(position).getId().getBook().getId();
						// Log.d("------------检测-----------", each_item_id);

						// design selected as !
						boolean selected = !mSelectedState.get(each_item_id, false);

						// each_item_choose_btn.toggle(); // design choose

						if (selected) {
							// if selected is true,array--mSelectedState design
							// as true
							mSelectedState.put(each_item_id, true);
							// 获得选择的数量
							int selectednumber = Integer.parseInt(each_item_num.getText().toString());
							mSelectedNum.put(each_item_id, selectednumber);
							totalPrice += selectednumber * list_shopping_bus.get(position).getId().getBook().getPrice();
						} else {
							// delete this item
							mSelectedState.delete(each_item_id);
							// 获得选择的数量
							int selectednumber = Integer.parseInt(each_item_num.getText().toString());
							// reduce the totalPrice
							totalPrice -= selectednumber * list_shopping_bus.get(position).getId().getBook().getPrice();
						}

						count_money_tv.setText( getResources().getString(R.string.rmb) + totalPrice +  getResources().getString(R.string.yuan));

						if (mSelectedState.size() == listSize) {
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
			});
			return abView;
		}
	};
}
