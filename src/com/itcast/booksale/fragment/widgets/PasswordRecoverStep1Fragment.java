package com.itcast.booksale.fragment.widgets;

import com.example.booksale.R;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep1Fragment extends Fragment {
	SimpleTextInputCellFragment fragEmail;//����
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view =inflater.inflate(R.layout.fragment_password_recover_step1, null);//���ز���
			
			fragEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//��������
			
			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {//��Ӱ�ťbtn_next�ĵ�������¼�
				
				@Override
				public void onClick(View v) {
					goNext();//�����������һ��					
				}
			});
		}
		return view;
	}

	@Override
	public void onResume() {//��ʼ���ؼ�
		super.onResume();
		
		fragEmail.setLabelText("����");
		{
			fragEmail.setHintText("������ע��ʱ�󶨵�����");
		}
	}
	
	protected void goNext() {//������һ��
		if(onGoNextListener!=null){  //����Ϊ��
			onGoNextListener.onGoNext();
		}
	}
	
	OnGoNextListener onGoNextListener;//����һ���ڲ���
	
	public static interface OnGoNextListener{//���ʱ�����ӿڣ����ô˷���
		void onGoNext();
	}
	
	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}
	
	public String getText() {//��ȡ���������
		return fragEmail.getText().toString();
	}


	
}
