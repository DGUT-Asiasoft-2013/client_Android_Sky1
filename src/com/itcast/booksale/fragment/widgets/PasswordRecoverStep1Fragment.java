package com.itcast.booksale.fragment.widgets;

import com.itcast.booksale.R;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep1Fragment extends Fragment {
	SimpleTextInputCellFragment fragEmail;//閭
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view =inflater.inflate(R.layout.fragment_password_recover_step1, null);//鍔犺浇甯冨眬
			
			fragEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//鍔犺浇閭
			
			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {//娣诲姞鎸夐挳btn_next鐨勭偣鍑荤洃鍚簨浠�
				
				@Override
				public void onClick(View v) {
					goNext();//蹇樿瀵嗙爜鐨勪笅涓�姝�					
				}
			});
		}
		return view;
	}

	@Override
	public void onResume() {//鍒濆鍖栨帶浠�
		super.onResume();
		
		fragEmail.setLabelText("邮箱:");
		{
			fragEmail.setHintText("请输入绑定的邮箱");
		}
	}
	
	protected void goNext() {//瀹氫箟涓嬩竴姝�
		if(onGoNextListener!=null){   //鑻ヤ笉涓虹┖
			onGoNextListener.onGoNext();
		}
	}
	
	OnGoNextListener onGoNextListener;//瀹氫箟涓�涓唴閮ㄧ被
	
	public static interface OnGoNextListener{//鐐瑰嚮鏃跺垱寤烘帴鍙ｏ紝璋冪敤姝ゆ柟娉�
		void onGoNext();
	}
	
	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}
	
	public String getText() {//鑾峰彇杈撳叆鐨勯偖绠�
		return fragEmail.getText().toString();
	}


	
}
