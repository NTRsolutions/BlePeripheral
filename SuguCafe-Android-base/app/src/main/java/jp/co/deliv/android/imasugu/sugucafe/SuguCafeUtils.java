package jp.co.deliv.android.imasugu.sugucafe;

import android.app.Activity;
import android.content.Intent;

/**
 * �����J�t�F�A�v���P�[�V�������[�e�B���e�B���\�b�h
 * @author DLV4002
 *
 */
public class SuguCafeUtils {

	
	/**
	 * �����J�t�F�A�v���P�[�V�����̃o�[�W��������\�����郆�[�e�B���e�B���\�b�h
	 * @param activity
	 */
	public static void showAboutInformation(Activity activity) {
		Intent intent = new Intent(activity, SuguCafeAboutView.class);
		activity.startActivity(intent);
	}
	/**
	 * �摜Url�����������m�F���܂�
	 * @param url �m�F������Url
	 * @return true: ������ false:������
	 */
	public static boolean isValidShopImageUrl(String url){
		if (url==null || url.equals("")) return false;
		
		if (url.contains("{0}") || url.contains("{1}")) return false;
		
		return true;
	}
}
