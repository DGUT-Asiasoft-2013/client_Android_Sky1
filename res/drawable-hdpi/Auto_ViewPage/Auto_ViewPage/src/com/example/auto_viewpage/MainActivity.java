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
		titles = new String[] { "����", "�����˦��",
				"����ͼ���", "Ʒζ��", "���鿴������" };
		// Ϊ�˸��õ�ʹ����Щ��Ϣ�����Դ���һ������
		imageviews = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setBackgroundResource(images[i]);
			imageviews.add(iv);
		}
		// ���������뵽������
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dop_0));
		dots.add(findViewById(R.id.dop_1));
		dots.add(findViewById(R.id.dop_2));
		dots.add(findViewById(R.id.dop_3));
		dots.add(findViewById(R.id.dop_4));
		title = (TextView) findViewById(R.id.textview);
		// ����Ĭ�ϵ���ʾ����
		title.setText(titles[0]);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// viewpager�е�ͼƬҲ����Ҫ���������ȥ��
		MyAdapter adapter = new MyAdapter();
		viewpager.setAdapter(adapter);
		
		//��Ӽ����¼�(ҳ��ı��¼�)
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//��¼ԭ�ȵ��λ��
			int oldposition = 0;
			
			//1-->2    1ҳ���ȥ��ʱ�������ķ���
			@Override
			public void onPageSelected(int position) {
				title.setText(titles[position]);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				dots.get(oldposition).setBackgroundResource(R.drawable.dot_normal); //ԭ�ȵ�ĸĳ�ʧȥ����
				oldposition = position;
				currentItem = position;
			}
			//1 -->2  2ҳ������������ķ��� 
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			//ҳ�滬��ʱ���õķ��� 
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	public class MyAdapter extends PagerAdapter {
		// ������ǵ�ǰ�������Ķ����ǲ���Ҫ���ҵ�ǰҳ����ʾ��
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;// �����ǰ��ʾ��View���㴫��������ͬһ��View,˵������Ҫ��ʾ�� view
		}
		@Override
		// ͼƬ������
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		/**
		 * �Ƴ���ǰһ��ͼƬ
		 * */
		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(view, position, object);
			view.removeView(imageviews.get(position));
		}

		/**
		 * ���һ��ͼƬ
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
		//ָ���������л�һ��ͼƬ
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
