package jp.co.deliv.android.http;

import jp.co.deliv.android.imasugu.sugucafe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * �l�b�g���[�N�ڑ����ł��Ȃ��Ƃ��Ƀ��[�U�Ƀl�b�g���[�N�ڑ���Ԃ��m�F����悤�����A
 * �A���[�g�_�C�A���O��\������N���X�B
 * @author DLV4002
 */
public class NetworkErrorAlertDialog {

	private Activity activity;	
	private String title;					// �_�C�A���O�^�C�g��
	private String message;					// �_�C�A���O���b�Z�[�W
	private String positiveButtonText;		// �|�W�e�B�u�iOK�j�{�^��������
	
	public NetworkErrorAlertDialog(Activity activity) {
		this.activity = activity;
		title = activity.getString(R.string.msg_network_err_title);
		message = activity.getString(R.string.msg_network_err_message);
		positiveButtonText = activity.getString(R.string.msg_network_err_button_text);
	}
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public String getPositiveButtonText() {
		return positiveButtonText;
	}

	public void setPositiveButtonText(String positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	/**
	 * �_�C�A���O��\��
	 * @param activity
	 */
	public void show() {
		new AlertDialog.Builder(activity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) { }
		})
		.create()
		.show();
	}
}
