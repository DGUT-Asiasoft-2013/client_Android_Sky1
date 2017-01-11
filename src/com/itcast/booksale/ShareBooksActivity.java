package com.itcast.booksale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itcast.booksale.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.inputcells.PictureInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加卖书的信息
 * @author Administrator
 *
 */
public class ShareBooksActivity extends Activity{


	//图书信息(8个)
	EditText editBookTitle,editBookAuthor
	,editPrice,editBookPublisher,editISBN
	,editBookSummary,editText,editBookNumber;
	PictureInputCellFragment fragInputBookAvatar;//图书照片
	//图书分类(标签)
	private Spinner bookTagSpinner;
	private List<String> bookTag_list;
	private ArrayAdapter<String> booksTag_adapter;
	String bookTag_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_share_books);

		//初始化定义
		init();


		//确定出售
		findViewById(R.id.btn_share_book).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//将信息存进数据库
				sendContent();
			}
		});
	}

	//初始化
	void init(){
		editBookTitle = (EditText) findViewById(R.id.input_book_title);//图书名称
		editBookAuthor = (EditText) findViewById(R.id.input_book_author);//图书作者
		editPrice = (EditText) findViewById(R.id.input_book_price);//出售价格
		editBookPublisher = (EditText) findViewById(R.id.input_book_publisher);//出版社
		editISBN = (EditText) findViewById(R.id.input_book_isbn);//ISBN
		editBookSummary = (EditText) findViewById(R.id.input_book_summary);//图书摘要
		editText = (EditText) findViewById(R.id.input_user_text);//卖家留言
		//添加图片
		fragInputBookAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_share_picture);		
		editBookNumber=(EditText) findViewById(R.id.input_book_number);
		//图书分类下拉框
		bookTagSpinner = (Spinner) findViewById(R.id.spinner_book_tag);//图书分类(标签)
		//添加下拉框数据
		bookTag_list = new ArrayList<String>();
		bookTag_list.add("教科书");
		bookTag_list.add("文学");
		bookTag_list.add("童书");
		bookTag_list.add("艺术");
		bookTag_list.add("科技");
		bookTag_list.add("生活");
		bookTag_list.add("计算机");
		//适配器
		booksTag_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,bookTag_list);
		//设置样式
		booksTag_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		//加载适配器
		bookTagSpinner.setAdapter(booksTag_adapter);

		bookTagSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//选中下拉框后设置类型
				bookTag_text= adapter.getItem(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	void sendContent(){

		//获取图书信息
		String bookTitle = editBookTitle.getText().toString();
		String bookAuthor = editBookAuthor.getText().toString();
		String bookPrice = editPrice.getText().toString();
		String bookPublisher = editBookPublisher.getText().toString();
		String bookTag = bookTag_text;//改成下拉选择框
		String bookSummary = editBookSummary.getText().toString();
		String text = editText.getText().toString();
		String ISBN = editISBN.getText().toString();
		String booknumber=editBookNumber.getText().toString();

		if(bookTitle.length()==0){
			Toast.makeText(ShareBooksActivity.this, "图书标题不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if(ISBN.length() == 0){
			Toast.makeText(ShareBooksActivity.this, "ISBN不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else if(booknumber.length() == 0){
			booknumber = "1";//默认为1本
		}

		//下面的addFormDataPart("title", bookTitle)左边的title应该跟服务器的一样，记住
		MultipartBody.Builder bookBody = new MultipartBody.Builder()
				.addFormDataPart("title", bookTitle)
				.addFormDataPart("author", bookAuthor)
				.addFormDataPart("price", bookPrice)
				.addFormDataPart("publisher", bookPublisher)
				.addFormDataPart("summary", bookSummary)
				.addFormDataPart("tag", bookTag)
				.addFormDataPart("book_isbn", ISBN) 
				.addFormDataPart("text", text)
				.addFormDataPart("booknumber", booknumber);


		//----------------
		//创建存储图片
		byte[] pngData = fragInputBookAvatar.getPngData();
		if (pngData != null){
			RequestBody fileBody = RequestBody.create(MediaType.parse("image.png"), pngData);
			bookBody.addFormDataPart("bookavatar", "bookavatar.png", fileBody);

		}

		//创建新载体
		RequestBody newBookBody = bookBody.build();
		Request request = Servelet.requestuildApi("sellbooks")
				.post(newBookBody)
				.build();

		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						ShareBooksActivity.this.onSucceed(responseBody);
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						ShareBooksActivity.this.onFailure(arg1);
					}
				});

			}
		});
	}

	void onSucceed(final String text){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(ShareBooksActivity.this)
				.setTitle("连接成功，提交")
				.setMessage(text)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//返回主界面
						goMainView();
					}
				}).show();
			}
		});

	}

	void onFailure(final Exception e){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(ShareBooksActivity.this)
				.setTitle("连接，提交失败")
				.setMessage(e.getMessage())
				.show();
			}
		});

	}
	void goMainView(){



		Intent itnt = new Intent(this,HelloWorldActivity.class);

		startActivity(itnt);

		finish();

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
