package com.itcast.booksale.fragment.widgets;

import com.example.booksale.R;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep1Fragment extends Fragment {
	SimpleTextInputCellFragment fragEmail;//邮箱
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view =inflater.inflate(R.layout.fragment_password_recover_step1, null);//加载布局
			
			fragEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//加载邮箱
			
			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {//添加按钮btn_next的点击监听事件
				
				@Override
				public void onClick(View v) {
					goNext();//忘记密码的下一步					
				}
			});
		}
		return view;
	}

	@Override
	public void onResume() {//初始化控件
		super.onResume();
		
		fragEmail.setLabelText("邮箱");
		{
			fragEmail.setHintText("请输入注册时绑定的邮箱");
		}
	}
	
	protected void goNext() {//定义下一步
		if(onGoNextListener!=null){   //若不为空
			onGoNextListener.onGoNext();
		}
	}
	
	OnGoNextListener onGoNextListener;//定义一个内部类
	
	public static interface OnGoNextListener{//点击时创建接口，调用此方法
		void onGoNext();
	}
	
	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}
	
	public String getText() {//获取输入的邮箱
		return fragEmail.getText().toString();
	}


	
}
