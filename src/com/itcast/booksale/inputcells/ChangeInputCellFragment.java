package com.itcast.booksale.inputcells;

import com.itcast.booksale.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 为更改个人资料的基本类
 * @author Administrator
 *
 */
public class ChangeInputCellFragment extends Fragment {

	TextView title;
	EditText edit;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_change_inputcell, null);
		
		title=(TextView) view.findViewById(R.id.change_title);
		edit=(EditText) view.findViewById(R.id.change_edittext);
		
		return view;
	}
	
	public void setTitleText(String TitleText){
		title.setText(TitleText);
	}
	
	public String getText(){
		return edit.getText().toString();
	}
	
	public void setHintText(String hintText){
		edit.setHint(hintText);
	}
	
}
