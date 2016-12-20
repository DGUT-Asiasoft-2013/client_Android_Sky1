package com.itcast.booksale.fragment.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.booksale.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.booksale.entity.Book;
import com.itcast.booksale.entity.Comment;
import com.itcast.booksale.fragment.pages.Page;
import com.itcast.booksale.servelet.Servelet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 这个Comment_Listfragment是用于展示图书的评论的
 */
public class Comment_Listfragment extends Fragment {

	String bookId;//在书评那里设置id，这里获取id




	int page;
	View view ;
	private ListView comment_list;              //列表
	private List<Comment> list=new ArrayList<Comment>();
	CommentAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.book_comment, container);
			comment_list=(ListView) view.findViewById(R.id.comment_list);
			
			adapter=new CommentAdapter();
			comment_list.setAdapter(adapter);               //设置监听器
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadComment();             //调用下载评论方法
	}
	
	/*
	 * 下面这个方法为下载评论
	 */
	public void loadComment() {
		OkHttpClient client=Servelet.getOkHttpClient();       //获得客户端
		//获得请求
		Request request=Servelet.requestuildApi("book/"+bookId+"/comment")//---获取书的Id
				.get()
				.build();
		
		client.newCall(request)
		.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					//获得page类的对象
					final Page<Comment> pageComment;
					ObjectMapper objectMapper=new ObjectMapper();

					//此为后台进行的，所以不能放在主线程里面进行
					String responseString = arg1.body().string();
					Log.d("loading feed list", responseString);
					
					//把解析下来的东西传入pageComment中
					pageComment=objectMapper.readValue(responseString,
							new TypeReference<Page<Comment>>() {});
					
					getActivity().runOnUiThread(new Runnable() {
						
						

						@Override
						public void run() {
							//把解析下来的页数传给Comment_Listfragment
							page=pageComment.getNumber();
							//把内容传给list
							list=pageComment.getContent();
							//刷新
							adapter.notifyDataSetInvalidated();
						}
					});
				}catch (JsonParseException e) {
					e.printStackTrace();
					
				} catch (JsonMappingException e) {
					e.printStackTrace();
					
				}catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							new AlertDialog.Builder(getActivity())
							.setTitle("失败ing")
							.setMessage(e.toString())
							.show();
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				onFailure(arg0, arg1);
			}
		});
	}
	
	public void onFailure(Call arg0, final Exception arg1)  {
		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(getActivity())
				.setTitle("失败")
				.setMessage(arg1.toString())
				.show();
				
			}
		});
		
	}
	
	
	
	class CommentAdapter extends BaseAdapter
	{
		View commentview;         //创建Commentview

		@Override
		public int getCount() {
			return list==null?0:list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView==null) {
				//如果convertView为空，则为listview设置
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				commentview=inflater.inflate(R.layout.book_comment_content_list, null);
			}
			else {
				commentview=convertView;
			}
			TextView user_comment_name=(TextView) commentview.findViewById(R.id.user_comment_name);
			TextView user_comment_createtime=(TextView) commentview.findViewById(R.id.user_comment_createtime);
			EditText user_comment_content=(EditText) commentview.findViewById(R.id.user_comment_content);
			//获得下载的某个文章的某个评论
			Comment comment=list.get(position);
			//设置用户名
			user_comment_name.setText(comment.getUser().getName());
			//将Date类型的时间转换为string类型
			String date=(String) DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate());
			//设置时间
			user_comment_createtime.setText(date);
			
			//设置内容
			user_comment_content.setText(comment.getContent());
			
			return commentview;
		}
		
	}
	
	//用于获取书籍ID
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
