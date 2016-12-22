package com.itcast.booksale.fragment.widgets;

import com.example.booksale.R;
import com.itcast.booksale.SendPrivateMessage;
import com.itcast.booksale.servelet.Servelet;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

public class EditPrivateMessageFragment extends Fragment {

	EditText editPrivateMessage;
	Button btnSendPrivateMessage;
	View view;
	String privateMessgeDetail;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view  == null){
			view =inflater.inflate(R.layout.widget_edit_private_message, container);//添加组件
			
		}
		editPrivateMessage = (EditText) view.findViewById(R.id.edit_private_message_text);
		btnSendPrivateMessage=(Button) view.findViewById(R.id.btn_send_private_message);
		privateMessgeDetail =editPrivateMessage.getText().toString();//获得私信的内容
		
		btnSendPrivateMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendPrivateMessage();
			}
		});
		
		
		
		return view;
				
	}
	
	public void sendPrivateMessage(){
		OkHttpClient client = Servelet.getOkHttpClient();
	//	MultipartBody body = new MultipartBody.Builder().addFormDataPart("", value)
		
		
		
		Intent itnt = new Intent(getActivity(), SendPrivateMessage.class);
		itnt.putExtra("privateMessageDetail", privateMessgeDetail);
		editPrivateMessage.setText("");
	}
	
}
