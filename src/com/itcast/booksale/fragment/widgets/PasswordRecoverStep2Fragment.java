package com.itcast.booksale.fragment.widgets;

import com.itcast.booksale.R;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment {
	View view;
	
	SimpleTextInputCellFragment fragVerify;//楠岃瘉鐮�
	SimpleTextInputCellFragment fragPassword;//瀵嗙爜
	SimpleTextInputCellFragment fragPasswordRepeat;//閲嶅瀵嗙爜
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_password_recover_step2, null);//鍔犺浇甯冨眬
			
			fragVerify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);//鍔犺浇楠岃瘉鐮�
			fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//鍔犺浇瀵嗙爜
			fragPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//鍔犺浇閲嶅瀵嗙爜
			
			view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {  //娣诲姞鎸夐挳鐨勭偣鍑讳簨浠�
				
				@Override
				public void onClick(View v) {
					onSubmitClicked();
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		//鍒濆鍖栨帶浠�
		super.onResume();
		
		fragVerify.setLabelText(getActivity().getString(R.string.pr_yam_l));
		{
			fragVerify.setHintText(getActivity().getString(R.string.pr_yam_h));
		}
		
		fragPassword.setLabelText(getActivity().getString(R.string.pr_password_l));
		{
			fragPassword.setHintText(getActivity().getString(R.string.pr_password_h));
			fragPassword.setIsPassword(true);
		}
		
		fragPasswordRepeat.setLabelText(getActivity().getString(R.string.pr_password_two_l));
		{
			fragPasswordRepeat.setHintText(getActivity().getString(R.string.pr_password_two_h));
			fragPasswordRepeat.setIsPassword(true);
		}
	}
	
	OnSubmitClickedListener onSubmitClickedListener;//瀹氫箟涓�涓唴閮ㄧ被
	
	public static interface OnSubmitClickedListener {//鐐瑰嚮鏃跺垱寤烘帴鍙ｏ紝璋冪敤姝ゆ柟娉�
		void onSubmitClicked();
	}
	
	public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener) {
		this.onSubmitClickedListener = onSubmitClickedListener;
	}
	
	public void onSubmitClicked() {
		if (fragPassword.getText().equals(fragPasswordRepeat.getText())) {
			//鍏堝垽鏂柊瀵嗙爜涓庨噸澶嶆柊瀵嗙爜鏄惁涓�鑷�
			if (onSubmitClickedListener != null) {
				//鑻ョ洃鍚櫒涓嶄负绌烘椂锛�
				onSubmitClickedListener.onSubmitClicked();
			}
		} else {//涓嶄竴鑷存椂鏄剧ず鎻愮ず瀵硅瘽妗�
			new AlertDialog
			.Builder(getActivity())
			.setMessage(R.string.pr_password_error)
			.setNegativeButton(R.string.pr_sure,null).show();
		}
	}	

	
	public String getText() {
		return fragPassword.getText();
	}
}
