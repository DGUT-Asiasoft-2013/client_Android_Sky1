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
 * 杩欐槸璐墿杞︾殑椤甸潰鏄剧ず
 */
public class Buy_book_bus_fragment extends Fragment {

	View abView; // 璐墿杞︾殑椤甸潰

	View top_view; // 璐墿杞﹂《閮ㄧ殑鎸夐挳甯冨眬
	View bottom_view; // 璐墿杞﹀簳绔殑鎸夐挳甯冨眬
	private ListView lv_shopping_bus; // 璐墿杞︾殑鍒楄〃
	private List<Bookbus> list_shopping_bus = new ArrayList<Bookbus>();

	private CheckBox AllChoose_Btn; // 搴曢儴鎸夐挳鏍忕殑鍏ㄩ�夊渾鍦堟寜閽�
	private TextView count_money_tv; // 搴曢儴鎸夐挳鏍忕殑閽辩殑鎬绘暟
	private Button btn_count_all_bus; // 搴曢儴鎸夐挳鏍忕殑鏈�鍙宠竟 鐨勭粨绠楁寜閽�
	private LinearLayout iLayout_buttom_money;// 搴曢儴鎸夐挳鐨勮喘鐗╄溅鍜屽悎璁＄殑甯冨眬
	private Button delete_book_from_bookbus;// 搴曢儴鍒犻櫎鎸夐挳

	List<Bookbus> bookbusList = new ArrayList<Bookbus>();

	int page;

	private ImageView back_btn; // 椤堕儴鐨勮繑鍥炴寜閽�
	private TextView edit; // 椤堕儴鐨勨�滅紪杈戔��

	private float totalPrice; // 瀹氫箟鎬讳环

	private BookAvatarView bookAvatar; // 鍥句功鐓х墖

	private boolean isDelete; // 鏄惁鍙垹闄ゆā寮�

	/**
	 * 鎵归噺妯″紡涓嬬敤鏉ヨ褰曞綋鍓嶉�変腑鐘舵��
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
			abView = inflater.inflate(R.layout.buy_book_bus, null); // 鍔犺浇璐墿杞﹂〉闈�
			bottom_view = inflater.inflate(R.layout.buy_book_bus_bottom_btn, null); // 鍔犺浇璐墿杞﹀簳閮ㄦ寜閽殑甯冨眬
			top_view = inflater.inflate(R.layout.buy_bus_top_view_normal, null); // 鍔犺浇璐墿杞﹂《閮ㄦ寜閽殑甯冨眬

			initView(); // initialization
			lv_shopping_bus.setAdapter(adapter); // set Listener for
			// lv_shopping_bus
		}
		return abView;
	}

	public void initView() {
		// 鑾峰緱璐墿杞︾殑鐣岄潰鐨刲istview鍒楄〃
		lv_shopping_bus = (ListView) abView.findViewById(R.id.listview_shopping_bus);

		/**
		 * 椤堕儴鐨勫疄鐜�
		 */
		back_btn = (ImageView) top_view.findViewById(R.id.back); // 椤堕儴鐨勮繑鍥炴寜閽�
		edit = (TextView) top_view.findViewById(R.id.subtitle);// 椤堕儴鐨勨�滅紪杈戔��
		//		EditOnClickListener listener = new EditOnClickListener();
		edit.setOnClickListener(listener);

		/**
		 * 搴曢儴鐨勫疄鐜�
		 */
		// 搴曢儴鍏ㄩ�夌殑鍦嗗湀鎸夐挳
		AllChoose_Btn = (CheckBox) bottom_view.findViewById(R.id.isAllChoose_check);
		// 涓哄簳閮ㄥ叏閫夊渾鍦堟寜閽坊鍔犵洃鍚櫒
		AllChoose_Btn.setOnClickListener(cAllchooseOnClickListener);

		// bottom's btn'column 's moneny's total
		count_money_tv = (TextView) bottom_view.findViewById(R.id.count_money);
		// bottom's btn'column 's rightest count btn
		btn_count_all_bus = (Button) bottom_view.findViewById(R.id.count_all_bus);
		// add Listener
		btn_count_all_bus.setOnClickListener(clickListener);
		// bottom's btn and count's layout
		iLayout_buttom_money = (LinearLayout) bottom_view.findViewById(R.id.layout_buttom_money);
		// bottom's delete book from bookbus鈥� btn
		delete_book_from_bookbus = (Button) bottom_view.findViewById(R.id.delete_book_from_bookbus);
		delete_book_from_bookbus.setOnClickListener(deletebookFromBookbusListener);

		// top's btn lan add to listview's bottom锛宼his must before setAdapter()
		lv_shopping_bus.addHeaderView(top_view);
		// bottom's btn lan add to listview's top锛宼his must before setAdapter()
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
	 * delete_book_from_bookbus鈥榮 listener
	 * 
	 * @author Administrator
	 *
	 */
	OnClickListener deletebookFromBookbusListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isDelete) {
				// if it can delete锛実et this id
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

							Toast.makeText(getActivity(), "绉婚櫎璐墿杞﹀け璐�", Toast.LENGTH_SHORT).show();
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
	 * top's鈥渆dit鈥� 's ClickListener
	 */
	OnClickListener listener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			isDelete = !isDelete; // design the model
			if (isDelete) {
				// if it is delete model
				edit.setText("瀹屾垚");
				iLayout_buttom_money.setVisibility(View.GONE); // design layout
				// as INVISIBLE
				delete_book_from_bookbus.setVisibility(abView.VISIBLE); // design
				// delete
				// as
				// VISIBLE
			} else {
				edit.setText("缂栬緫");
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
		count_money_tv.setText("￥:" + 0.00 + "元");
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

					// 姝や负鍚庡彴杩涜鐨勶紝鎵�浠ヤ笉鑳芥斁鍦ㄤ富绾跨▼閲岄潰杩涜
					String responseString = arg1.body().string();
					// 鑾峰緱page绫荤殑瀵硅薄
					final Page<Bookbus> pageBook;

					final ObjectMapper objectMapper = new ObjectMapper();
					Log.d("loading feed list", responseString);
					// 鎶婅В鏋愪笅鏉ョ殑涓滆タ浼犲叆pageComment涓�
					pageBook = objectMapper.readValue(responseString, new TypeReference<Page<Bookbus>>() {
					});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 鎶婅В鏋愪笅鏉ョ殑椤垫暟浼犵粰Comment_Listfragment
							page = pageBook.getNumber();
							// 鎶婂唴瀹逛紶缁檒ist
							list_shopping_bus = pageBook.getContent();

							// 鍒锋柊
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

							new AlertDialog.Builder(getActivity()).setTitle("bookbus澶辫触ing").setMessage(e.toString())
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
	 * 杩欐槸搴曢儴鎸夐挳鏍忕殑鏈�鍙宠竟鐨�"缁撶畻"鎸夐挳鐨勭洃鍚櫒
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 鑾峰緱涔︾殑淇℃伅
			if (btn_count_all_bus.isClickable()) {
				// Log.i("--------------妫�娴�----------",
				// "--------缁撶畻鎸夐挳鐨勬樉绀�1----------");
				// 濡傛灉杩欎釜鎸夐挳琚寜浜�,鍒欏脊鍑轰竴涓彁绀烘锛屾樉绀轰拱鐨勪笢瑗�
				if (count_money_tv != null) {

					for (int i = 0; i < list_shopping_bus.size(); i++) {

						// Circular traversal the list_shopping_bus
						// Log.i("--------------妫�娴�----------",
						// list_shopping_bus.get(i).getId().getBook().getText());
						if (mSelectedState.get(list_shopping_bus.get(i).getId().getBook().getId())) {

							// if the one is seleted
							final Bookbus bookbus = list_shopping_bus.get(i);
							String string = bookbus.getId().getBook().getTitle();
							bookbusList.add(bookbus);

							// get the random ab's character
							Random random = new Random();
							int index = random.nextInt(ab.length);
							final String order_letter = ab[index];

							// Log.i("--------------妫�娴�----------", string);
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									new AlertDialog.Builder(getActivity()).setTitle("鎻愪氦璁㈠崟")
									.setMessage("鎮ㄤ簬" + bookbus.getId().getBook().getEditDate() + "鎯宠璐拱"
											+ bookbus.getId().getBook().getTitle())
									.setPositiveButton("纭畾", new DialogInterface.OnClickListener() {

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
													count_money_tv.setText("￥:" + 0.00 + "元");//After the settlement is complete, the total price of the returned cart will be cleared to zero
													// Log.i("------------妫�娴�----------",
													// "----------------鍟﹀暒鍟﹀暒鍟�-----------");
												}
											}).setNegativeButton("涓嶄簡锛屾垜鍐嶆兂鎯�", null).show();

								}
							});

						}
					}
					
				} else {
					// 鍙栨秷璁㈠崟
					Toast.makeText(getActivity(), "鎮ㄥ凡缁忓彇娑堜簡璁㈠崟", Toast.LENGTH_SHORT).show();
					

				}
			}
		}

	};

	/*
	 * 杩欐槸搴曢儴鍏ㄩ�夊渾鍦堟寜閽殑鐩戝惉鍣�
	 */

	OnClickListener cAllchooseOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (AllChoose_Btn.isChecked()) {
				// 濡傛灉"鍏ㄩ��"鎸夐挳琚�変腑锛屽垯缁х画鎵ц涓嬮潰鐨勬搷浣�
				totalPrice = 0;
				if (list_shopping_bus != null) {
					mSelectedState.clear(); // 娓呭叏閫夌姸鎬�
					if (list_shopping_bus.size() == 0) {
						// 濡傛灉list鐨勫ぇ灏忎负0锛屽垯杩斿洖
						return;

					}
					for (int i = 0; i < list_shopping_bus.size(); i++) {
						// 鑾峰緱姣忚璐墿鍒楄〃涔︾殑id
						int each_id = list_shopping_bus.get(i).getId().getBook().getId();

						mSelectedState.put(each_id, true); // 缁欐瘡涓喘鐗╄璁剧疆鐐逛腑鐘舵��

						//This line has a value after the "+" or "-" or radio button is clicked
						int each_num = mSelectedNum.get(each_id);
						// 鎶婂叏閮ㄤ环閽卞姞璧锋潵
						totalPrice += each_num * list_shopping_bus.get(i).getId().getBook().getPrice();
					}

					// 鍒锋柊鍒楄〃
					adapter.notifyDataSetChanged();
					count_money_tv.setText("￥:" + totalPrice + "元"); // 杈撳叆閽辩殑鎬绘暟

				}

			} else {
				// 鍚﹀垯
				totalPrice = 0; // 閽辩殑鎬绘暟涓�0
				mSelectedState.clear();
				//				mSelectedNum.put(list_shopping_bus.get(i), 1);
				// 鍒锋柊
				adapter.notifyDataSetChanged();
				count_money_tv.setText("￥:" + 0.00 + "元");

			}
		}
	};

	/**
	 * 涓嬮潰涓鸿喘鐗╄溅鐨勫垪琛╨v_shopping_bus鐨勬槸閫傞厤鍣�
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

			TextView shopname = (TextView) abView.findViewById(R.id.shop_name); // 鍟嗗簵鍚嶅瓧
			CheckBox each_item_choose_btn = (CheckBox) abView.findViewById(R.id.each_item_choose); // 鍦嗗湀閫夋嫨鎸夐挳
			bookAvatar = (BookAvatarView) abView.findViewById(R.id.book_image); // 涔︾殑鍥剧墖
			TextView each_bookprice = (TextView) abView.findViewById(R.id.each_item_price); // 鍥句功鐨勪环閽�
			TextView each_item_reduce = (TextView) abView.findViewById(R.id.each_item_reduce); // 鏁伴噺鐨勨��-鈥�
			final TextView each_item_num = (TextView) abView.findViewById(R.id.each_item_num); // number
			TextView each_item_add = (TextView) abView.findViewById(R.id.each_item_add); // number's鈥�+鈥�

			final Bookbus bookbus = list_shopping_bus.get(position); // 鑾峰緱瀵瑰簲鐨勮喘鐗╄溅淇℃伅

			int each_book_id = bookbus.getId().getBook().getId(); // 鑾峰緱瀵瑰簲鐨刬d
			boolean selected = mSelectedState.get(each_book_id, false); // The selected state of its id
			int selectedNum = mSelectedNum.get(each_book_id, 1);// Marks the
			// current
			// number state
			// of its id

			final int count = selectedNum;      //make the selectedNum Assignment for count

			String number = String.valueOf(selectedNum);
			Log.i("--------------妫�娴�----------", number);
			each_item_num.setText(number);
			each_item_choose_btn.setChecked(selected); // 璁剧疆姣忚閫夋嫨鎸夐挳鐨勯�夋嫨鐘舵��

			// 璁剧疆涔﹀浘鐗�
			bookAvatar.load(Servelet.urlstring + bookbus.getId().getBook().getBookavatar());
			shopname.setText(bookbus.getId().getBook().getTitle()); // design
			// bookname
			String price = String.valueOf(bookbus.getId().getBook().getPrice());
			each_bookprice.setText(price); // design price
			// Log.i("--------------妫�娴�----------", "-------+鍙疯繍琛屼腑");

			/**
			 * number's 鈥�+鈥� 's ClickListener
			 */
			each_item_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 鑾峰緱id
					int id = bookbus.getId().getBook().getId();
					boolean selected = mSelectedState.get(id, false);
					// count=0;
					if (!selected) {
						mSelectedNum.put(id, count + 1);// 璁剧疆鍏剁殑id鐨勫綋鍓嶆暟閲忕姸鎬�
						// int selectedNum=mSelectedNum.get(id, count);
						// String number = String.valueOf(selectedNum);
						// Log.i("--------------妫�娴�----------", number);
						// set number
						// String add_number =
						// String.valueOf(bookbus.getId().getBook().getBooknumber()
						// + 1);
						String add_number = String.valueOf(count);
						each_item_num.setText(add_number);

						// because client add 1,so Backstage's booknumber-1
						bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() - 1);
						notifyDataSetChanged(); // notify
						// 閫変腑浜�
						totalPrice += bookbus.getId().getBook().getPrice();
						//						count_money_tv.setText("锟�:" + (totalPrice + bookbus.getId().getBook().getPrice()) + "鍏�"); // 璁剧疆鎬婚挶鏁�

					} else {
						totalPrice = 0; // 閽辩殑鎬绘暟涓�0
						mSelectedState.clear();
						// 鍒锋柊
						adapter.notifyDataSetChanged();
					}
				}
			});

			/**
			 * number's 鈥�-鈥� 's ClickListener
			 */
			each_item_reduce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (bookbus.getId().getBook().getBooknumber() == 1) {
						// if have one book,return
						return;

					}
					int reduce_id = bookbus.getId().getBook().getId(); // 鑾峰緱id
					boolean selected = mSelectedState.get(reduce_id, false);

					if (!selected) {
						mSelectedNum.put(reduce_id, count - 1);// 璁剧疆鍏剁殑id鐨勫綋鍓嶆暟閲忕姸鎬�
						// 璁剧疆鏁伴噺
						// String reduce_number =
						// String.valueOf(bookbus.getId().getBook().getBooknumber()
						// - 1);
						String reduce_number = String.valueOf(count);
						each_item_num.setText(reduce_number);

						// because client reduce 1,so Backstage's booknumber+1
						bookbus.getId().getBook().setBooknumber(bookbus.getId().getBook().getBooknumber() + 1);
						notifyDataSetChanged(); // 鍒锋柊
						// 閫変腑浜�
						totalPrice -= bookbus.getId().getBook().getPrice();
						//						count_money_tv.setText("锟�:" + (totalPrice + bookbus.getId().getBook().getPrice()) + "鍏�"); // 璁剧疆鎬婚挶鏁�

					} else {
						totalPrice = 0; // 閽辩殑鎬绘暟涓�0
						mSelectedState.clear();
						// 鍒锋柊
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
						// Log.d("------------妫�娴�-----------", each_item_id);

						// design selected as !
						boolean selected = !mSelectedState.get(each_item_id, false);

						// each_item_choose_btn.toggle(); // design choose

						if (selected) {
							// if selected is true,array--mSelectedState design
							// as true
							mSelectedState.put(each_item_id, true);
							// 鑾峰緱閫夋嫨鐨勬暟閲�
							int selectednumber = Integer.parseInt(each_item_num.getText().toString());
							mSelectedNum.put(each_item_id, selectednumber);
							totalPrice += selectednumber * list_shopping_bus.get(position).getId().getBook().getPrice();
						} else {
							// delete this item
							mSelectedState.delete(each_item_id);
							// 鑾峰緱閫夋嫨鐨勬暟閲�
							int selectednumber = Integer.parseInt(each_item_num.getText().toString());
							// reduce the totalPrice
							totalPrice -= selectednumber * list_shopping_bus.get(position).getId().getBook().getPrice();
						}

						count_money_tv.setText("￥:" + totalPrice + "元");

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
