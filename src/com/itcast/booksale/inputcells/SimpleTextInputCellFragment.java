package com.itcast.booksale.inputcells;

import com.example.booksale.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

//����һЩ�༭��ĸ�ʽ
public class SimpleTextInputCellFragment extends BaseInputCellFragment{
	TextView label;//��ǩ
	EditText edit;//�༭
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//fragment������ͼ

		View view=inflater.inflate(R.layout.fragment_inputcell_simpletext, container);//�õ�����view
		label=(TextView) view.findViewById(R.id.label);//��ȡTextView�ؼ�label
		edit=(EditText) view.findViewById(R.id.edit);//��ȡEditView�ؼ�edit
		
		return view;
	}

	@Override
	public void setLabelText(String labelText) {//��д������ķ��� 
		label.setText(labelText);
	}

	public String getText() {  //��ȡ�༭��edit���ַ���
		return edit.getText().toString();
	}
	
	@Override
	public void setHintText(String hintText) {
		edit.setHint(hintText);
	}
	
	public void setIsPassword(boolean isPassword){//����Ϊ��������
		if(isPassword){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsEmail(boolean isEmail){//���ñ���Ϊ�����ַ
		if(isEmail){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsNumber(boolean isNumber){//���ñ���Ϊ�����ַ
		if(isNumber){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_NUMBER);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsPhone(boolean isPhone){//���ñ���Ϊ�绰����
		if(isPhone){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_PHONE);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
}
