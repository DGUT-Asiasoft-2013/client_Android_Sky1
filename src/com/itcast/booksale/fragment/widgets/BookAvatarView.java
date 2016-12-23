package com.itcast.booksale.fragment.widgets;

import java.io.IOException;

import com.itcast.booksale.entity.User;
import com.itcast.booksale.servelet.Servelet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//用于创建矩形的图片
public class BookAvatarView extends View{
	public BookAvatarView(Context context) {
		super(context);
	}

	public BookAvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BookAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	Paint paint;
	float srcWidth, srcHeight;

	Handler mainThreadHandler = new Handler();;

	public void setBitmap(Bitmap bmp){
		if(bmp==null) {
			paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
			paint.setAntiAlias(true);
		}else{
			paint = new Paint();
			paint.setShader(new BitmapShader(bmp, TileMode.REPEAT, TileMode.REPEAT));
			paint.setAntiAlias(true);

			
			srcWidth = bmp.getWidth();
			srcHeight = bmp.getHeight();	
		}
		invalidate();

	}

	public void load(User user){
		load(Servelet.urlstring + user.getAvatar());
}
	public void load(String url){

		Log.d("bookAvatar", url);
		OkHttpClient client = Servelet.getOkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.method("get",null)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

				try {
					byte[] bytes = arg1.body().bytes();
					final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					mainThreadHandler.post(new Runnable() {

						@Override
						public void run() {
							setBitmap(bmp);

						}
					});

				} catch (Exception e) {
					setBitmap(null);
				}
				
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				mainThreadHandler.post(new Runnable() {
					public void run() {
						setBitmap(null);
					}
				});
			}
		});

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(paint != null){
			canvas.save();
			float dstWidth = getWidth();
			float dstHeight = getHeight();
			float scaleX = srcWidth / dstWidth;
			float scaleY = srcHeight / dstHeight;
			canvas.scale(1/scaleX, 1/scaleY);
			//画矩形，起点坐标，终点坐标
			Rect rect = new Rect(0,0,200,200);
			canvas.drawRect(rect,paint);
			
			canvas.restore();	
		}
	}
	
}
