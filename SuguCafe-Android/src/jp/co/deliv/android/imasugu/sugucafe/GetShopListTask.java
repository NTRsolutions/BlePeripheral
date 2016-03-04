package jp.co.deliv.android.imasugu.sugucafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import jp.co.deliv.android.geocoding.GeoCodingUtils;
import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.http.NetworkErrorAlertDialog;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;
import jp.co.deliv.android.imasugu.webapi.gurunavi.GuruNaviAPIParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

/**
 * WEBAPI�^�C�v
 * 
 * @author DLV4002
 */
enum WEBAPI_TYPE {
	HOTPAPPER, // �z�b�g�y�b�p�[API
	GNAVI, // ����i�rAPI
}

/**
 * �z�b�g�y�b�p�[API���R�[������XML���擾�A�p�[�X�������s���AShopInfo���X�g���쐬����o�b�N�O���E���h�^�X�N
 * 
 * @author DLV4002
 */
public class GetShopListTask extends AsyncTask<String, Void, List<ShopInfo>> {

	/*
	 * �o�b�N�O���E���h�^�X�N�̌Ăяo����Activity
	 */
	private Activity activity = null;

	/*
	 * �o�b�N�O���E���h�^�X�N���s���ɕ\������v���O���X�_�C�A���O
	 */
	private ProgressDialog progressDialog = null;

	/*
	 * ListView�Ɋ֘A�t�����Ă���Adapter�N���X
	 */
	private ShopListAdapter adapter = null;

	/*
	 * ���[�U�̈ʒu���
	 */
	private double userLatitude;
	private double userLongitude;

	/**
	 * ���[�U�̈ʒu�����Z�b�g���Ă����B�����ŃZ�b�g����ʒu���́A���X�Ƃ̋����v�Z�Ɏg�p�����B
	 * 
	 * @param latitude
	 *            �ܓx
	 * @param longitude
	 *            �o�x
	 */
	public void setUserLocation(double latitude, double longitude) {
		this.userLatitude = latitude;
		this.userLongitude = longitude;
	}

	/**
	 * �������ɕ\������v���O���X�_�C�A���O�̐ݒ�
	 * 
	 * @param progressDialog
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	/**
	 * BackgroundTask�N���X�iAsyncTask�N���X�̔h���N���X�j
	 * 
	 * @param activity
	 */
	public GetShopListTask(Activity activity, ShopListAdapter adapter) {
		this.activity = activity;
		this.adapter = adapter;
	}


	/**
	 * �o�b�N�O���E���h�����̑O����
	 */
	@Override
	protected void onPreExecute() {

		/*
		 * �i���󋵂�\���v���O���X�o�[�̐ݒ�
		 */
		if (progressDialog == null) {
			// ProgressDialog�i�v���O���X�o�[�j�̐ݒ�
			progressDialog = new ProgressDialog(activity);

			// �L�����Z���ݒ�
			progressDialog.setIndeterminate(false);

			// �v���O���X�o�[�̃X�^�C�����Z�b�g
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			// �\�����郁�b�Z�[�W���Z�b�g
			Resources res = activity.getResources();
			progressDialog
					.setMessage(res.getText(R.string.msg_info_find_store));

			// �v���O���X�o�[�\��
			progressDialog.show();
		}
	}

	/**
	 * �����œn�����URL�ɃA�N�Z�X���A�擾�ł���XML�p�[�X����TimeLineItemList���쐬���� ���̃��\�b�h���񓯊��Ŏ��s�����B
	 */
	@Override
	protected List<ShopInfo> doInBackground(String... params) {

		String url = params[0]; // URL���擾����

		// �l�b�g���[�N�̐ڑ���Ԃ��m�F����
		if (!HttpClient.isNetworkConnected(activity)) {
			// �ڑ�����Ă��Ȃ��ꍇ�́A�ȍ~�̏������s��Ȃ�
			return null;
		}

		// XML���擾����
		ShopInfoAPIPaser xmlParser = null;
		xmlParser = (ShopInfoAPIPaser) new GuruNaviAPIParser(url);

		// XML����͂���ShopInfo�I�u�W�F�N�g�̃��X�g�Ƀp�[�X����
		List<ShopInfo> result = xmlParser.parse();

		// �G���[���������ăf�[�^���擾�ł��Ȃ������Ƃ���null��ԋp����
		if (result == null) {
			return null;
		}

		/*
		 * �擾�������X���̈ʒu���ƃ��[�U�̈ʒu��񂩂狗�����v�Z����
		 */
		for (ShopInfo shop : result) {
			double shopLat = shop.getLatitude();
			double shopLng = shop.getLongitude();
			double distance = GeoCodingUtils.getDistance(this.userLatitude,
					this.userLongitude, shopLat, shopLng);
			shop.setDistance(distance);
		}

		/*
		 * ���[�U�̈ʒu��񂩂�̋����ɉ����ĕ��ёւ����s��
		 */
		Collections.sort(result, new Comparator<ShopInfo>() {
			/*
			 * ShopInfo�N���X�������̏����ɕ��ёւ���
			 */
			public int compare(ShopInfo shop1, ShopInfo shop2) {
				double d1 = shop1.getDistance();
				double d2 = shop2.getDistance();
				if (d1 == d2) {
					return 0;
				}
				if (d1 < d2) {
					return -1;
				}
				return 1;
			}
		});

		return result;
	}

	/**
	 * �o�b�N�O���E���h�����̌㏈�� ���̏�����UI�X���b�h�ɖ߂��Ă���͂��B
	 */
	@Override
	protected void onPostExecute(List<ShopInfo> result) {

		// �v���O���X�_�C�A���O�I��
		try {
			this.progressDialog.dismiss();
		} catch (Exception e) {
			Log.d("GetShopListTask", "�v���O���X�_�C�A���O�I���ŗ�O�F" + e.getMessage());
			return;
		}

		/**
		 * �o�b�N�O���E���h�̏������ʂ�UI�ɕ\��
		 */
		if (result != null) {
			/*
			 * XML���p�[�X�������ʂ�ShopInfo���X�g���A�_�v�^�ɃZ�b�g����
			 */
			HashSet<String> shopIdSet = new HashSet<String>();
			List<ShopInfo> saveResult = new ArrayList<ShopInfo>();
			ApplicationData appContext = (ApplicationData) activity
					.getApplicationContext(); // application context
			adapter.clear();
			for (ShopInfo item : result) {
				/*
				 * �����X�܂������o�Ă��Ă��܂����߁A�X�܃f�[�^�擾��A����ID�̏d���X�܂͒ǉ�����Ȃ�
				 */

				if (!shopIdSet.contains(item.getId())) {
					shopIdSet.add(item.getId());// �����X�܂̊Ǘ����X�g
					saveResult.add(item); // add saving result
					adapter.add(item);
				}
			}
			appContext.setShopResult(saveResult); // save result
			shopIdSet.clear();
			shopIdSet = null;
		} else {
			NetworkErrorAlertDialog alert = new NetworkErrorAlertDialog(
					this.activity);
			alert.show();
		}
	}

}
