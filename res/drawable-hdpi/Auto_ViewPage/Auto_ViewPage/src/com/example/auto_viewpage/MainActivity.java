package com.example.auto_viewpage;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Integer[] images;
	private String[] titles;
	private ArrayList<ImageView> imageviews;
	private ArrayList<View> dots;
	private TextView title;
	private ViewPager viewpager;
	private int currentItem = 0; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		images = new Integer[] { R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };
		titles = new String[] { "书香", "就输大甩卖",
				"身临图书馆", "品味书", "看书看到天亮" };
		// 为了更好的使用这些信息，可以创建一个集合
		imageviews = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setBackgroundResource(images[i]);
			imageviews.add(iv);
		}
		// 将五个点放入到集合中
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dop_0));
		dots.add(findViewById(R.id.dop_1));
		dots.add(findViewById(R.id.dop_2));
		dots.add(findViewById(R.id.dop_3));
		dots.add(findViewById(R.id.dop_4));
		title = (TextView) findViewById(R.id.textview);
		// 设置默认的显示内容
		title.setText(titles[0]);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// viewpager中的图片也是需要经过适配进去的
		MyAdapter adapter = new MyAdapter();
		viewpager.setAdapter(adapter);
		
		//添加监听事件(页面改变事件)
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//记录原先点的位置
			int oldposition = 0;
			
			//1-->2    1页面出去的时候启动的方法
			@Override
			public void onPageSelected(int position) {
				title.setText(titles[position]);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				dots.get(oldposition).setBackgroundResource(R.drawable.dot_normal); //原先点的改成失去焦点
				oldposition = position;
				currentItem = position;
			}
			//1 -->2  2页面进来后启动的方法 
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			//页面滑动时调用的方法 
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	public class MyAdapter extends PagerAdapter {
		// 代表的是当前传进来的对象，是不是要在我当前页面显示的
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;// 如果当前显示的View跟你传进来的是同一个View,说明就是要显示的 view
		}
		@Override
		// 图片的数量
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		/**
		 * 移除当前一张图片
		 * */
		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(view, position, object);
			view.removeView(imageviews.get(position));
		}

		/**
		 * 添加一张图片
		 * */
		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(imageviews.get(position));
			return imageviews.get(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	protected void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		//指定两秒钟切花一张图片
		scheduledExecutorService.scheduleAtFixedRate(new ViewPageTask(), 2, 2, TimeUnit.SECONDS);
	}
	@Override
	protected void onStop() {
		super.onStop();
		scheduledExecutorService.shutdown();
	};
	
	public class ViewPageTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem+1) % images.length;
			handler.sendEmptyMessage(0);
		}
		
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			viewpager.setCurrentItem(currentItem);
		}
		
	};
	private ScheduledExecutorService scheduledExecutorService;

}
