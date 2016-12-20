package com.itcast.booksale.inputcells;

import android.app.Fragment;

//定义一个抽象类BaseInputCellFragment
public abstract class BaseInputCellFragment extends Fragment{
	public abstract void setLabelText(String labelText);//设置标签名
	public abstract void setHintText(String hintText);//设置提示字符
}
