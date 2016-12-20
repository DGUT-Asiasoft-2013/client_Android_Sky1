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
	final int REQUESTCODE_CAMERA = 1;//����
	final int REQUESTCODE_ALBUM = 2;//����ͼƬ
	
	ImageView imageView;//ͼƬ
	TextView labelText;//��ǩ
	TextView hintText;//��ʾ
	
	byte[] pngData;//�ַ�������
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);

		imageView = (ImageView) view.findViewById(R.id.image);//��ȡImageView�ؼ�image
		labelText = (TextView) view.findViewById(R.id.label);//��ȡTextView�ؼ�label
		hintText = (TextView) view.findViewById(R.id.hint);//��ȡTextView�ؼ�hint
		
		imageView.setOnClickListener(new OnClickListener() {//���ÿؼ�imageView�ĵ���¼�
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();
				
			}
		});
		
		return view;
	}
	
	protected void onImageViewClicked() {
		String[] items={"����","���"};//���items��������
		
		new AlertDialog.Builder(getActivity())   //�����б�Ի���
		.setTitle(hintText.getText())   //��ȡhintText�ؼ����������ñ���
		.setItems(items, new DialogInterface.OnClickListener() {   //�б�Ի���item�ĵ���¼�
			
			@Override
			public void onClick(DialogInterface dialog, int which) {  //item�ĵ��ѡ��
				switch (which){
				case 0:
					takePhoto();//����
					break;
					
				case 1:
					pickFromAlbum();//���
					break;
					
				default:
					break;
				}	
			}
		}).setNegativeButton("ȡ��", null)//����һ��ȡ����ť
		  .show();
		
	}

	
	protected void takePhoto() {//�������յķ���
		//�����ճ���
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUESTCODE_CAMERA);//�õ��´�Activity�رպ󷵻ص�����
	}
	

	protected void pickFromAlbum() {//�������ķ���
		//�����
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUESTCODE_ALBUM);
	}
	
	void saveBitmap(Bitmap bitmap){//����ͼƬ
		ByteArrayOutputStream baos=new ByteArrayOutputStream();//����һ���ֽ��������������
		bitmap.compress(CompressFormat.PNG,100, baos);//ѹ��ͼƬ������ͼƬѹ��ΪPNG��ʽ��100��ѹ���ʣ���ʾ��ѹ����
		pngData=baos.toByteArray();//����һ����С���������ĵ�ǰ��С��һ���·��仺������
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   //Ϊ�˵õ����ص����ݡ����µ�Activity�رպ����ǰ���Activity��������
		if(resultCode == Activity.RESULT_CANCELED) return;//��ȡ��ʱ����

		if(requestCode == REQUESTCODE_CAMERA){//��ѡ������ʱ

			Bitmap bmp = (Bitmap)data.getExtras().get("data");//�������
			imageView.setImageBitmap(bmp);//���ؽ��յ�ͼƬ��
			
			saveBitmap(bmp);//����ͼƬ
		}else if(requestCode == REQUESTCODE_ALBUM){//��ѡ�����
			
			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());//�����������
				imageView.setImageBitmap(bmp);//���ؽ��յ�ͼƬ����
				
				saveBitmap(bmp);//����ͼƬ
			} catch (Exception e) {
				e.printStackTrace();//��ӡ�쳣�Լ���ʾ������Ϣ�������ڵ��ԣ�
			}
		}
	}

	public byte[] getPngData(){//������PNG��ʽ���ݵķ���
		return pngData;
	}
	
	public void setLabelText(String labelText){//���ñ�ǩ
		this.labelText.setText(labelText);
	}

	public void setHintText(String hintText){//������ʾ
		this.hintText.setText(hintText);
	}
}
