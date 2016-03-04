package jp.co.deliv.android.imasugu.sugucafe;

import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;
import jp.co.deliv.android.location.ProviderNotFoundException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * ���̃A�N�e�B�r�e�B�́A�X�v���b�V���摜��\�����ăl�b�g���[�N����ʒu�����擾���Ă����A ShopListView�A�N�e�B�r�e�B�ɐ�����ڂ��B
 * 
 * @author DLV4002
 */
public class SuguCafeTopView extends Activity {

	/**
	 * ���[�U�̈ʒu���i�ܓx�E�o�x�j
	 */
	private DeviceLocation deviceLocation = null;
	private ProgressDialog progressDialog = null;
	private LocationListenerWithTimeout locationListener = new LocationListenerWithTimeout() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		/**
		 * �ʒu��񂪕ω������ۂɌĂяo�����
		 */
		public void onLocationChanged(Location location) {

			// �ʒu���̎擾����������
			deviceLocation.stop();

			// �擾�����ʒu��񂩂�ܓx�o�x���擾
			ApplicationData appContext = (ApplicationData) getApplicationContext();
			appContext.setUserLatitude(location.getLatitude());
			appContext.setUserLongitude(location.getLongitude());
			Intent intent = new Intent(SuguCafeTopView.this,
					SuguCafeTabMenuView.class);
			appContext.setSearchCondition("");
			appContext.setUpdateLocation(0);
			startActivity(intent);
			progressDialog.dismiss();

			/*
			 * �ʒu��񂪍X�V���ꂽ���߁A�X�܃��X�g���X�V����
			 */
		}

		/**
		 * �ʒu���擾�Ń^�C���A�E�g�����������Ƃ��̏���
		 */
		public void onTimeout(DeviceLocation location, Location latestLocation) {
			if (location.gpsEnabled()) {
				location.gpsDisabled();
				try {
					location.start();
				} catch (Exception e) {
					LocationDisabledAlert alert = new LocationDisabledAlert(
							SuguCafeTopView.this);
					progressDialog.dismiss();
					alert.show();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(
						SuguCafeTopView.this);
				progressDialog.dismiss();
				alert.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topview);

		// Manifest�t�@�C���ɋL�q����Ă���o�[�W���������擾����
		Resources res = getResources();

		String packageName = getPackageName();
		PackageInfo packageInfo = null;
		String version = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_META_DATA);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			packageInfo = null;
			version = "-----";
		}

		// �A�v���P�[�V�������A�o�[�W��������\��
		TextView applicationName = (TextView) findViewById(R.id.application_name);
		applicationName.setText(res.getString(R.string.app_name) + " "
				+ version);
	}

	@Override
	protected void onPause() {
		// ���P�[�V�����T�[�r�X���X�g�b�v����
		if (deviceLocation != null)
			deviceLocation.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * move to map with user location 
	 * @param v
	 */
	public void btnSeachAround_onClick(View v) {
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		appContext.setTypeSearch(SuguCafeConst.SEARCH_CAFE_ARROUND);
		createProgressDialog();
	}

	/**
	 * Comment : move to map without user location
	 * @param v
	 */
	public void btnNoSeach_OnClick(View v) {
		// createProgressDialog();
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		// �ړI�n�ŃJ�t�F��
		appContext.setTypeSearch(SuguCafeConst.SEARCH_CAFE_WITH_ADDRESS);
		appContext.setSearchCondition("");
		appContext.setUpdateLocation(0);
		Intent intent = new Intent(SuguCafeTopView.this,
				SuguCafeTabMenuView.class);
		startActivity(intent);

	}

	/**
	 * Comment : create Progress dialog and start GPS
	 */
	private void createProgressDialog() {

		if (progressDialog != null) {
			progressDialog = null;
		}

		// ProgressDialog�i�v���O���X�o�[�j�̐ݒ�
		progressDialog = new ProgressDialog(this);

		// �L�����Z���ݒ�
		progressDialog.setIndeterminate(false);

		// �v���O���X�o�[�̃X�^�C�����Z�b�g
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// �\�����郁�b�Z�[�W���Z�b�g
		Resources res = getResources();
		progressDialog.setMessage(res.getText(R.string.msg_waiting));

		// �v���O���X�o�[�\��
		progressDialog.show();
		deviceLocation = new DeviceLocation(
				(LocationManager) getSystemService(LOCATION_SERVICE),
				locationListener);
		// deviceLocation.enableGps(true);
		deviceLocation.setTimeout(SuguCafeConst.GPS_DETECT_TIME_OUT);
		try {
			deviceLocation.start();
		} catch (ProviderNotFoundException e) {
			Log.e("DeviceLoc", e.getMessage());
		}
	}
}
