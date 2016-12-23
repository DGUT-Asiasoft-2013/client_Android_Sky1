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

import com.itcast.booksale.R;;

/**
 * 照片选择
 * @author Administrator
 *
 */
public class PictureInputCellFragment extends Fragment {
	final int REQUESTCODE_CAMERA = 1;//鎷嶇収
	final int REQUESTCODE_ALBUM = 2;//鏈湴鍥剧墖
	
	ImageView imageView;//鍥剧墖
	TextView labelText;//鏍囩
	TextView hintText;//鎻愮ず
	
	byte[] pngData;//瀛楃鍨嬫暟缁�
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);

		imageView = (ImageView) view.findViewById(R.id.image);//鑾峰彇ImageView鎺т欢image
		labelText = (TextView) view.findViewById(R.id.label);//鑾峰彇TextView鎺т欢label
		hintText = (TextView) view.findViewById(R.id.hint);//鑾峰彇TextView鎺т欢hint
		
		imageView.setOnClickListener(new OnClickListener() {//璁剧疆鎺т欢imageView鐨勭偣鍑讳簨浠�
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();
				
			}
		});
		
		return view;
	}
	
	protected void onImageViewClicked() {
		String[] items={"拍照","相册"};//娣诲姞items闆嗗悎灞炴��
		
		new AlertDialog.Builder(getActivity())   //鐢熸垚鍒楄〃瀵硅瘽妗�
		.setTitle(hintText.getText())   //鎻愬彇hintText鎺т欢鍐呭鏉ヨ缃爣棰�
		.setItems(items, new DialogInterface.OnClickListener() {   //鍒楄〃瀵硅瘽妗唅tem鐨勭偣鍑讳簨浠�
			
			@Override
			public void onClick(DialogInterface dialog, int which) {  //item鐨勭偣鍑婚�夋嫨
				switch (which){
				case 0:
					takePhoto();//鎷嶇収
					break;
					
				case 1:
					pickFromAlbum();//鐩稿唽
					break;
					
				default:
					break;
				}	
			}
		}).setNegativeButton("确定", null)//璁剧疆涓�涓彇娑堟寜閽�
		  .show();
		
	}

	
	protected void takePhoto() {//瀹氫箟鎷嶇収鐨勬柟娉�
		//鎵撳紑鎷嶇収绋嬪簭
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUESTCODE_CAMERA);//寰楀埌鏂版墦寮�Activity鍏抽棴鍚庤繑鍥炵殑鏁版嵁
	}
	

	protected void pickFromAlbum() {//瀹氫箟鐩稿唽鐨勬柟娉�
		//鎵撳紑鐩稿唽
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUESTCODE_ALBUM);
	}
	
	void saveBitmap(Bitmap bitmap){//淇濆瓨鍥剧墖
		ByteArrayOutputStream baos=new ByteArrayOutputStream();//鍒涘缓涓�涓瓧鑺傛暟缁勮緭鍑烘祦瀵硅薄
		bitmap.compress(CompressFormat.PNG,100, baos);//鍘嬬缉鍥剧墖鈥斺�斿皢鍥剧墖鍘嬬缉涓篜NG鏍煎紡锛�100鏄帇缂╃巼锛堣〃绀轰笉鍘嬬缉锛�
		pngData=baos.toByteArray();//鍒涘缓涓�涓ぇ灏忎笌姝よ緭鍑烘祦鐨勫綋鍓嶅ぇ灏忕殑涓�涓柊鍒嗛厤缂撳啿鍖恒��
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   //涓轰簡寰楀埌浼犲洖鐨勬暟鎹�斺�旀柊鐨凙ctivity鍏抽棴鍚庝細鍚戝墠闈㈢殑Activity浼犲洖鏁版嵁
		if(resultCode == Activity.RESULT_CANCELED) return;//褰撳彇娑堟椂杩斿洖

		if(requestCode == REQUESTCODE_CAMERA){//褰撻�夋嫨鎷嶇収鏃�

			Bitmap bmp = (Bitmap)data.getExtras().get("data");//鑾峰緱鏁版嵁
			imageView.setImageBitmap(bmp);//鍔犺浇鎺ユ敹鐨勫浘鐗囨暟
			
			saveBitmap(bmp);//淇濆瓨鍥剧墖
		}else if(requestCode == REQUESTCODE_ALBUM){//褰撻�夋嫨鐩稿唽
			
			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());//鑾峰緱鐩稿唽鐨勬暟鎹�
				imageView.setImageBitmap(bmp);//鍔犺浇鎺ユ敹鐨勫浘鐗囨暟鎹�
				
				saveBitmap(bmp);//淇濆瓨鍥剧墖
			} catch (Exception e) {
				e.printStackTrace();//鎵撳嵃寮傚父浠ュ強鏄剧ず璋冪敤淇℃伅锛堟湁鍔╀簬璋冭瘯锛�
			}
		}
	}

	public byte[] getPngData(){//瀹氫箟鑾峰緱PNG鏍煎紡鏁版嵁鐨勬柟娉�
		return pngData;
	}
	
	public void setLabelText(String labelText){//璁剧疆鏍囩
		this.labelText.setText(labelText);
	}

	public void setHintText(String hintText){//璁剧疆鎻愮ず
		this.hintText.setText(hintText);
	}
}
