package com.itcast.booksale.inputcells;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import com.example.booksale.R;;

public class PictureInputCellFragment extends Fragment {
	final int REQUESTCODE_CAMERA = 1;//拍照
	final int REQUESTCODE_ALBUM = 2;//本地图片
	
	ImageView imageView;//图片
	TextView labelText;//标签
	TextView hintText;//提示
	
	byte[] pngData;//字符型数组
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);

		imageView = (ImageView) view.findViewById(R.id.image);//获取ImageView控件image
		labelText = (TextView) view.findViewById(R.id.label);//获取TextView控件label
		hintText = (TextView) view.findViewById(R.id.hint);//获取TextView控件hint
		
		imageView.setOnClickListener(new OnClickListener() {//设置控件imageView的点击事件
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();
				
			}
		});
		
		return view;
	}
	
	protected void onImageViewClicked() {
		String[] items={"拍照","相册"};//添加items集合属性
		
		new AlertDialog.Builder(getActivity())   //生成列表对话框
		.setTitle(hintText.getText())   //提取hintText控件内容来设置标题
		.setItems(items, new DialogInterface.OnClickListener() {   //列表对话框item的点击事件
			
			@Override
			public void onClick(DialogInterface dialog, int which) {  //item的点击选择
				switch (which){
				case 0:
					takePhoto();//拍照
					break;
					
				case 1:
					pickFromAlbum();//相册
					break;
					
				default:
					break;
				}	
			}
		}).setNegativeButton("取消", null)//设置一个取消按钮
		  .show();
		
	}

	
	protected void takePhoto() {//定义拍照的方法
		//打开拍照程序
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUESTCODE_CAMERA);//得到新打开Activity关闭后返回的数据
	}
	

	protected void pickFromAlbum() {//定义相册的方法
		//打开相册
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUESTCODE_ALBUM);
	}
	
	void saveBitmap(Bitmap bitmap){//保存图片
		ByteArrayOutputStream baos=new ByteArrayOutputStream();//创建一个字节数组输出流对象
		bitmap.compress(CompressFormat.PNG,100, baos);//压缩图片——将图片压缩为PNG格式；100是压缩率（表示不压缩）
		pngData=baos.toByteArray();//创建一个大小与此输出流的当前大小的一个新分配缓冲区。
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   //为了得到传回的数据——新的Activity关闭后会向前面的Activity传回数据
		if(resultCode == Activity.RESULT_CANCELED) return;//当取消时返回

		if(requestCode == REQUESTCODE_CAMERA){//当选择拍照时

			Bitmap bmp = (Bitmap)data.getExtras().get("data");//获得数据
			imageView.setImageBitmap(bmp);//加载接收的图片数
			
			saveBitmap(bmp);//保存图片
		}else if(requestCode == REQUESTCODE_ALBUM){//当选择相册
			
			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());//获得相册的数据
				imageView.setImageBitmap(bmp);//加载接收的图片数据
				
				saveBitmap(bmp);//保存图片
			} catch (Exception e) {
				e.printStackTrace();//打印异常以及显示调用信息（有助于调试）
			}
		}
	}

	public byte[] getPngData(){//定义获得PNG格式数据的方法
		return pngData;
	}
	
	public void setLabelText(String labelText){//设置标签
		this.labelText.setText(labelText);
	}

	public void setHintText(String hintText){//设置提示
		this.hintText.setText(hintText);
	}
}
