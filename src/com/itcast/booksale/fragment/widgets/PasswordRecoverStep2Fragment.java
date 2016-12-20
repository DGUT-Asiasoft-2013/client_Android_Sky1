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
	
	SimpleTextInputCellFragment fragVerify;//��֤��
	SimpleTextInputCellFragment fragPassword;//����
	SimpleTextInputCellFragment fragPasswordRepeat;//�ظ�����
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_password_recover_step2, null);//���ز���
			
			fragVerify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);//������֤��
			fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//��������
			fragPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//�����ظ�����
			
			view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {  //��Ӱ�ť�ĵ���¼�
				
				@Override
				public void onClick(View v) {
					onSubmitClicked();
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onResume() {//��ʼ���ؼ�
		super.onResume();
		
		fragVerify.setLabelText("��֤��:");
		{
			fragVerify.setHintText("��������֤��");
		}
		
		fragPassword.setLabelText("������:");
		{
			fragPassword.setHintText("������������");
			fragPassword.setIsPassword(true);
		}
		
		fragPasswordRepeat.setLabelText("�ظ�������:");
		{
			fragPasswordRepeat.setHintText("���ٴ�����������");
			fragPasswordRepeat.setIsPassword(true);
		}
	}
	
	OnSubmitClickedListener onSubmitClickedListener;//����һ���ڲ���
	
	public static interface OnSubmitClickedListener {//���ʱ�����ӿڣ����ô˷���
		void onSubmitClicked();
	}
	
	public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener) {
		this.onSubmitClickedListener = onSubmitClickedListener;
	}
	
	public void onSubmitClicked() {
		if (fragPassword.getText().equals(fragPasswordRepeat.getText())) {//���ж����������ظ��������Ƿ�һ��
			if (onSubmitClickedListener != null) {//����������Ϊ��ʱ��
				onSubmitClickedListener.onSubmitClicked();
			}
		} else {//��һ��ʱ��ʾ��ʾ�Ի���
			new AlertDialog.Builder(getActivity()).setMessage("���벻һ��").setNegativeButton("ȷ��",null).show();
		}
	}	

	
	public String getText() {
		return fragPassword.getText();
	}
}
