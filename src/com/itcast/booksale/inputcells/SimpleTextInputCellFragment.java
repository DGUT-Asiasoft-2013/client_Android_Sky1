package com.itcast.booksale.inputcells;

import com.itcast.booksale.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

//定义一些编辑框的格式
public class SimpleTextInputCellFragment extends BaseInputCellFragment{
	TextView label;//标签
	EditText edit;//编辑
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//fragment加载视图

		View view=inflater.inflate(R.layout.fragment_inputcell_simpletext, container);//得到加载view
		label=(TextView) view.findViewById(R.id.label);//获取TextView控件label
		edit=(EditText) view.findViewById(R.id.edit);//获取EditView控件edit
		
		return view;
	}
	
	
	@Override
	public void setLabelText(String labelText) {//重写抽象类的方法 
		label.setText(labelText);
	}

	public String getText() {  //获取编辑框edit的字符串
		return edit.getText().toString();
	}
	
	@Override
	public void setHintText(String hintText) {
		edit.setHint(hintText);
	}
	
	public void setIsPassword(boolean isPassword){//必须为设置密码
		if(isPassword){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsEmail(boolean isEmail){//设置必须为邮箱地址
		if(isEmail){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsNumber(boolean isNumber){//设置必须为邮箱地址
		if(isNumber){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_NUMBER);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsPhone(boolean isPhone){//设置必须为电话号码
		if(isPhone){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_PHONE);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
}
