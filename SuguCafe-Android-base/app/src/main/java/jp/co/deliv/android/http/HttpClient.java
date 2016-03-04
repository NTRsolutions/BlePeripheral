package jp.co.deliv.android.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * HTTP�A�N�Z�X�N���C�A���g�N���X�B
 * 
 * @author DLV4002
 */
public class HttpClient {

	/**
	 * �f�t�H���g�^�C���A�E�g�F30�b�i�~���b�j
	 */
	public static final int DEFAULT_TIME_OUT = 30 * 1000;

	/**
	 * �l�b�g���[�N�̐ڑ���Ԃ��`�F�b�N���郆�[�e�B���e�B���\�b�h
	 * 
	 * @param ctx
	 *            �R���e�L�X�g
	 * @return �l�b�g���[�N�i3G�܂���WIFI�j�ɐڑ����Ă���ꍇ��true
	 */
	public static boolean isNetworkConnected(Context ctx) {

		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMan != null) {

			// �l�b�g���[�N����̏�Ԃ��m�F����
			NetworkInfo[] networks = conMan.getAllNetworkInfo();
			if (networks != null) {
				for (NetworkInfo nInfo : networks) {
					if (nInfo.isConnected()) {
						// �l�b�g���[�N�ɐڑ�����Ă�����̂����݂���
						return true;
					}
				}
			}

			/********
			 * ���N���b�V�����|�[�g�ɂ��ȉ��̃R�[�h�͍폜�� // 3G����̏�� State mobile =
			 * conMan.getNetworkInfo
			 * (ConnectivityManager.TYPE_MOBILE).getState(); // WIFI�̏�� State
			 * wifi =
			 * conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			 * 
			 * // �����ꂩ���ڑ�����Ă��邩�H if((mobile != State.CONNECTED) && (wifi !=
			 * State.CONNECTED)) { return false; } // �ǂ��炩�A�܂��͗����ڑ�����Ă��� return
			 * true;
			 ********/
		}
		return false;
	}

	/**
	 * 
	 * �����Ŏw�肳���URL�̃f�[�^���o�C�g�z��Ŏ擾����B WEB�A�N�Z�X�ɂ��XML�f�[�^�̎擾��摜�f�[�^�̎擾�Ȃǂɂ�闘�p��z�肵�Ă���B
	 * 
	 * @param strUrl
	 *            WEB�A�N�Z�X�ɂ��f�[�^�擾��URL
	 * @return �w�肵��URL����擾�����f�[�^�̃o�C�g�z��
	 * 
	 */
	public static byte[] getByteArrayFromURL(String strUrl) {

		byte[] byteArray = new byte[1024];
		byte[] result = null;

		HttpURLConnection con = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		int size = 0;

		try {
			URL url = new URL(strUrl);

			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(HttpClient.DEFAULT_TIME_OUT);
			con.connect();

			in = con.getInputStream();

			out = new ByteArrayOutputStream();
			while ((size = in.read(byteArray)) != -1) {
				out.write(byteArray, 0, size);
			}

			result = out.toByteArray();

		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.disconnect();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * �����Ŏw�肷��URL����C���[�W�摜���擾����
	 * 
	 * @param url
	 *            �C���[�W�摜��URL
	 * @return �r�b�g�}�b�v�C���[�W
	 */
	public static Bitmap getImage(String url) {
		byte[] byteArray = getByteArrayFromURL(url);
		
		// �摜�����݂��Ȃ��ꍇ�߂�
		if (byteArray == null) return null;
		
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		
		o2.inPurgeable = true;
		
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, o2);
	}
}
