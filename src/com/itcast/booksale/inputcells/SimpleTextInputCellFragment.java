package com.itcast.booksale.inputcells;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itcast.booksale.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

//瀹氫箟涓�浜涚紪杈戞鐨勬牸寮�
public class SimpleTextInputCellFragment extends BaseInputCellFragment{
	TextView label;//鏍囩
	EditText edit;//缂栬緫
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//fragment鍔犺浇瑙嗗浘

		View view=inflater.inflate(R.layout.fragment_inputcell_simpletext, container);//寰楀埌鍔犺浇view
		label=(TextView) view.findViewById(R.id.label);//鑾峰彇TextView鎺т欢label
		edit=(EditText) view.findViewById(R.id.edit);//鑾峰彇EditView鎺т欢edit
		
		return view;
	}
	
	
	@Override
	public void setLabelText(String labelText) {//閲嶅啓鎶借薄绫荤殑鏂规硶 
		label.setText(labelText);
	}

	public String getText() {  //鑾峰彇缂栬緫妗唀dit鐨勫瓧绗︿覆
		return edit.getText().toString();
	}
	
	@Override
	public void setHintText(String hintText) {
		edit.setHint(hintText);
	}
	
	public void setIsPassword(boolean isPassword){//蹇呴』涓鸿缃瘑鐮�
		if(isPassword){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	 public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	 public static boolean isEmail(String email) {
	        return Pattern.matches(REGEX_EMAIL, email);
	    }
	 
	public void setIsEmail(boolean isEmail){//璁剧疆蹇呴』涓洪偖绠卞湴鍧�
		if(isEmail){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public void setIsNumber(boolean isNumber){//璁剧疆蹇呴』涓洪偖绠卞湴鍧�
		if(isNumber){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_NUMBER);	
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	public static boolean isMobileNO(String mobiles){//判断是否为真的手机号码
		Pattern p=Pattern.compile("^((13[0-9])|(14[5|7])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m=p.matcher(mobiles);
		return m.matches();
	}
	
	public void setIsPhone(boolean isPhone){//璁剧疆蹇呴』涓虹數璇濆彿鐮�
		if(isPhone){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_PHONE);
			edit.setFadingEdgeLength(11);
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}		
	}
	
	 public static final String REGEX_QQ = "^[1-9][0-9]{3,11}";
	 public static boolean isQQ(String QQ) {
	        return Pattern.matches(REGEX_QQ, QQ);
	    }
}
