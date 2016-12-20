package com.itcast.booksale;

import java.io.IOException;

import com.example.booksale.R;
import com.itcast.booksale.inputcells.PictureInputCellFragment;
import com.itcast.booksale.inputcells.SimpleTextInputCellFragment;
import com.itcast.booksale.servelet.Servelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {
SimpleTextInputCellFragment fragInputCellAccount;//�û����˺�
SimpleTextInputCellFragment fragInputCellName;//�û����ǳ�
SimpleTextInputCellFragment fragInputCellPassword;//�û�������
SimpleTextInputCellFragment fragInputCellPasswordRepeat;//�ظ�����
SimpleTextInputCellFragment fragInputCellEmail;//�û��������ַ
SimpleTextInputCellFragment fragInputCellPhone;//�û��ĵ绰����
SimpleTextInputCellFragment fragInputCellQq;//�û���QQ

PictureInputCellFragment fragImage;//ͷ��

ProgressDialog ProgressDialog;//���ȶԻ���


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);//���ز���
		
		fragInputCellAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);//�����˺�account
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);//�����ǳ�name
		fragInputCellPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);//��������password
		fragInputCellPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);//�����ظ�����
		fragInputCellEmail=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);//��������Emai
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_phone);//���ص绰����Phone
		fragInputCellQq=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_qq);//����QQ
		
		fragImage=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);//����ͷ��avatar
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {//���ð�ťbtn_submit�ĵ���¼�
			
			@Override
			public void onClick(View v) {
				Submit();
			}
		});
	}


	@Override
		protected void onResume() {//��ʼ���ؼ�
			// TODO Auto-generated method stub
			super.onResume();
			
			fragInputCellAccount.setLabelText("�˺�:");//�˺�
			{
				fragInputCellAccount.setHintText("�������˺�");
			}
			
			fragInputCellName.setLabelText("�ǳ�:");//�ǳ�
			{
				fragInputCellName.setHintText("�������ǳ�");
			}
			
			fragInputCellPassword.setLabelText("����:");//����
			{
				fragInputCellPassword.setHintText("����������");
			}
			
			fragInputCellPasswordRepeat.setLabelText("�ظ�����:");//�ظ�����
			{
				fragInputCellPasswordRepeat.setHintText("���ٴ���������");
			}
			
			fragInputCellEmail.setLabelText("����:");//����
			{
				fragInputCellEmail.setHintText("����������");
				fragInputCellEmail.setIsEmail(true);
			}
			
			fragInputCellPhone.setLabelText("�绰:");//�绰
			{
				fragInputCellPhone.setHintText("������绰����");
				fragInputCellPhone.setIsPhone(true);
			}
			
			fragInputCellQq.setLabelText("QQ:");//QQ
			{
				fragInputCellQq.setHintText("������QQ����");
				fragInputCellQq.setIsNumber(true);
			}
		}
	
	protected void Submit() {  //�����ύ��ť�ķ���
		String password=fragInputCellPassword.getText();//��ȡ���������
		String passwordRepeat=fragInputCellPasswordRepeat.getText();//��ȡ������ظ�����
		
		if(!password.equals(passwordRepeat)){//�ж�������������Ƿ�һ��������һ���򵯳��Ի���ʾ��
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("������������벻һ��")
			.setNegativeButton("��", null)
			.show();//��ʾ�Ի���
			
			return;
		}
		
		String account=fragInputCellAccount.getText();//��ȡ������˻�
		String name=fragInputCellName.getText();//��ȡ������ǳ�
		String email=fragInputCellEmail.getText();//��ȡ���������
		String phone=fragInputCellPhone.getText();//��ȡ����ĵ绰
		String qq=fragInputCellQq.getText();//��ȡ�����QQ
	
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)//����������
				.addFormDataPart("num", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("password", password)
				.addFormDataPart("phoneNumb", phone)
				.addFormDataPart("qq", qq);
		
		if(fragImage.getPngData()!=null){//��ͷ��Ϊ��ʱ����ȡͷ��
			requestBodyBuilder.addFormDataPart("avatar", "avatar",RequestBody.create(MediaType.parse("image/png")
					, fragImage.getPngData()));
		}
		
		//��������
		Request request=Servelet.requestuildApi("register")
								.method("post", null)
								.post(requestBodyBuilder.build())
								.build();
		
		//������
		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("���Ժ�");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		//�ͻ������ӷ�����
		Servelet.getOkHttpClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {//�������ӷ�����ʱ
				runOnUiThread(new Runnable() {
					String ar=arg1.body().toString();
					@Override
					public void run() {
						progressDialog.dismiss();//��������ʧ						
						
						RegisterActivity.this.onResponse(arg0, ar);		
					}
				});
					
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();// ʹProgressDialog��ʧ

						RegisterActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}


	protected void onFailure(Call arg0, Exception arg1) {//�޷����ӷ�����ʱ
		new AlertDialog.Builder(this).setTitle("����ʧ��").setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("��", null).show();
	}


	protected void onResponse(Call arg0, String ar) {//�ɹ�
		try {
			new AlertDialog.Builder(this).setTitle("����ɹ�").setMessage("��������ɹ�")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);//��ת����½ҳ��
					startActivity(intent);
				}
			})
			.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onFailure(arg0, e);
		}
	}
}
