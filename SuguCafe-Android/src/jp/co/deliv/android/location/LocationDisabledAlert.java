package jp.co.deliv.android.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * GPS��Network��ʂ��Ĉʒu�����擾�ł��Ȃ��Ƃ��ɃA���[�g�_�C�A���O��\������N���X�B
 * �ʒu���̐ݒ��ʂ�\�����邱�Ƃ��ł���悤�ɂȂ��Ă���B
 * @author DLV4002
 *
 */
public class LocationDisabledAlert {

	private Activity activity;	
	private String title;					// �_�C�A���O�^�C�g��
	private String message;					// �_�C�A���O���b�Z�[�W
	private String positiveButtonText;		// �|�W�e�B�u�iOK�j�{�^��������
	private String negativeButtonText;		// �l�K�e�B�u�i�L�����Z���j�{�^��������
	
	public LocationDisabledAlert(Activity activity) {
		this.activity = activity;
		title = "�ʒu��񂪖����B";
		message = "���݁A���g���̒[���ɂ����Ĉʒu��񂪖����ɂȂ��Ă���A�A�v���P�[�V�����ňʒu�������o�ł��܂���B"
				+ "���̂悤�ɐݒ���s���ʒu��񂪌��o�ł���悤�ɐݒ肵�Ă��������B:\n\n"
				+ "�� �ʒu���̐ݒ�ŁuGPS�@�\���g�p�v�Ɓu���C�����X�l�b�g���[�N���g���v���I���ɂ���\n";
		positiveButtonText = "�ݒ�";
		negativeButtonText = "�X�L�b�v";
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

	public String getNegativeButtonText() {
		return negativeButtonText;
	}

	public void setNegativeButtonText(String negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
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
			public void onClick(final DialogInterface dialog, final int which) {
				try {
					activity.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
				} catch (ActivityNotFoundException e) {}	// ��������
			}
		})
		.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {}
		})
		.create()
		.show();		
	}
}
