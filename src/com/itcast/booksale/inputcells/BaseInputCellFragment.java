package com.itcast.booksale.inputcells;

import android.app.Fragment;

//����һ��������BaseInputCellFragment
public abstract class BaseInputCellFragment extends Fragment{
	public abstract void setLabelText(String labelText);//���ñ�ǩ��
	public abstract void setHintText(String hintText);//������ʾ�ַ�
}
