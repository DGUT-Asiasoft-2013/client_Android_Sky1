package com.itcast.booksale.fragment.widgets;

import com.example.booksale.R;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment {
	View view;
	
	SimpleTextInputCellFragment fragVerify;//验证码
	SimpleTextInputCellFragment fragPassword;//密码
	SimpleTextInputCellFragment fragPasswordRepeat;//重复密码
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_password_recover_step2, null);//加载布局
			
			fragVerify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);//加载验证码
			fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//加载密码
			fragPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//加载重复密码
			
			view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {  //添加按钮的点击事件
				
				@Override
				public void onClick(View v) {
					onSubmitClicked();
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onResume() {//初始化控件
		super.onResume();
		
		fragVerify.setLabelText("验证码:");
		{
			fragVerify.setHintText("请输入验证码");
		}
		
		fragPassword.setLabelText("新密码:");
		{
			fragPassword.setHintText("请输入新密码");
			fragPassword.setIsPassword(true);
		}
		
		fragPasswordRepeat.setLabelText("重复新密码:");
		{
			fragPasswordRepeat.setHintText("请再次输入新密码");
			fragPasswordRepeat.setIsPassword(true);
		}
	}
	
	OnSubmitClickedListener onSubmitClickedListener;//定义一个内部类
	
	public static interface OnSubmitClickedListener {//点击时创建接口，调用此方法
		void onSubmitClicked();
	}
	
	public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener) {
		this.onSubmitClickedListener = onSubmitClickedListener;
	}
	
	public void onSubmitClicked() {
		if (fragPassword.getText().equals(fragPasswordRepeat.getText())) {//先判断新密码与重复新密码是否一致
			if (onSubmitClickedListener != null) {//若监听器不为空时？
				onSubmitClickedListener.onSubmitClicked();
			}
		} else {//不一致时显示提示对话框
			new AlertDialog.Builder(getActivity()).setMessage("密码不一致").setNegativeButton("确定",null).show();
		}
	}	

	
	public String getText() {
		return fragPassword.getText();
	}
}
