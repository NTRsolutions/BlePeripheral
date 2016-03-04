package jp.co.deliv.android.imasugu.sugucafe;

import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;
import jp.co.deliv.android.location.ProviderNotFoundException;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

/**
 * Tab control
 * 
 * @author 120007HTT
 * 
 */
public class SuguCafeTabMenuView extends TabActivity {

	/**
	 * handle create menu option
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ������擾�̂��߂̃��\�[�X���擾

		// ���X���X�V���j���[��ǉ�
		TabHost tabHost = getTabHost();
		int index = tabHost.getCurrentTab();

		menu.add(Menu.NONE, SuguCafeConst.MENU_ID_SHOPLIST_UPDATE, Menu.NONE,
				getText(R.string.tab_name_update_location));
		// if tab is map view , it will show menu to link to list . if tab is
		// list view , it will show menu to link to map view
		if (index == 1) {
			menu.add(Menu.NONE, SuguCafeConst.MENU_ID_Move_To_Map, Menu.NONE,
					getText(R.string.tab_name_map));
		} else {
			menu.add(Menu.NONE, SuguCafeConst.MENU_ID_Move_To_List, Menu.NONE,
					getText(R.string.tab_name_list));
		}

		// �o�[�W������񃁃j���[��ǉ�
		menu.add(Menu.NONE, SuguCafeConst.MENU_ID_ABOUT, Menu.NONE,
				getText(R.string.tab_name_info));

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * handle click on item of menu option
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = true;
		TabHost tabHost = getTabHost();
		switch (item.getItemId()) {

		// ���X���X�V���j���[�I����
		case SuguCafeConst.MENU_ID_SHOPLIST_UPDATE:
			// update GPS
			createProgressDialog(R.string.msg_waiting);
			deviceLocation = new DeviceLocation(
					(LocationManager) getSystemService(LOCATION_SERVICE),
					locationListener);
			deviceLocation.enableGps(true);
			deviceLocation.setTimeout(SuguCafeConst.GPS_DETECT_TIME_OUT);
			try {
				deviceLocation.start();
			} catch (ProviderNotFoundException e) {
				e.printStackTrace();
			}
			ret = true;
			break;

		// �o�[�W�������I����
		case SuguCafeConst.MENU_ID_ABOUT:
			SuguCafeUtils.showAboutInformation(this);
			break;

		case SuguCafeConst.MENU_ID_Move_To_List:
			tabHost.setCurrentTab(1);
			break;
		case SuguCafeConst.MENU_ID_Move_To_Map:
			tabHost.setCurrentTab(0);
			// �f�t�H���g����
		default:
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	/**
	 * device to get location
	 */
	private DeviceLocation deviceLocation = null;
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
			appContext.setSearchCondition("");

			// set update change
			appContext.UpdateLocation();
			progressDialog.dismiss();
			createProgressDialog(R.string.msg_info_find_store);
			updateShopListItem(location.getLatitude(), location.getLongitude());
			
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
							SuguCafeTabMenuView.this);
					alert.show();
					progressDialog.dismiss();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(
						SuguCafeTabMenuView.this);
				alert.show();
				progressDialog.dismiss();
			}
		}
	};

	/*
	 * �o�b�N�O���E���h�^�X�N���s���ɕ\������v���O���X�_�C�A���O
	 */
	private ProgressDialog progressDialog = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_menu_view);
		TabHost tabHost = getTabHost();

		// Search with input tab
		Intent intentSeach;
		intentSeach = new Intent(this, ShopMapSearchView.class);
		TabSpec tabSeach = tabHost
				.newTabSpec("NoSeach")
				.setContent(intentSeach)
				.setIndicator(
						prepareTabView(getString(R.string.tab_name_map), 0,
								SuguCafeConst.TAB_PERFORM_TEXT_ONLY));
		// List tab
		Intent intentList = new Intent(this, ShopListSearchView.class);
		TabSpec tabList = tabHost
				.newTabSpec("List")
				.setContent(intentList)
				.setIndicator(
						prepareTabView(getString(R.string.tab_name_list), 0,
								SuguCafeConst.TAB_PERFORM_TEXT_ONLY));
		// add all tabs
		tabHost.addTab(tabSeach);
		tabHost.addTab(tabList);

		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

	/**
	 * Comment : modify each perform of tab in tabHost
	 * 
	 * @param text
	 * @param resId
	 * @param check
	 * @return view
	 */
	private View prepareTabView(String text, int resId, int check) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabs, null);
		ImageView iv_Tab = (ImageView) view.findViewById(R.id.img_tab);
		TextView tv_TabName = (TextView) view.findViewById(R.id.txt_tab_name);
		// perform image only
		if (check == SuguCafeConst.TAB_PERFORM_IMAGE_ONLY) {
			iv_Tab.setImageResource(resId);
			tv_TabName.setVisibility(View.GONE);
		}
		// perform text only
		if (check == SuguCafeConst.TAB_PERFORM_TEXT_ONLY) {
			tv_TabName.setText(text);
			iv_Tab.setVisibility(View.GONE);
		}
		// perform image and text
		if (check == SuguCafeConst.TAB_PERFORM_TEXT_AND_IMAGE) {
			tv_TabName.setText(text);
			iv_Tab.setImageResource(resId);
		}

		return view;
	}

	/**
	 * Create Progress Dialog and start GPS to get location
	 */
	private void createProgressDialog(int idMessage) {

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
		progressDialog.setMessage(res.getText(idMessage));

		// �v���O���X�o�[�\��
		progressDialog.show();
		
	}
	/**
	 * Get shop list
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void updateShopListItem(double latitude, double longitude) {

		// WEB�T�[�r�X���Ăяo���ăV���b�v���X�g���擾
		GetShopListInTab task = new GetShopListInTab(this);
		// �_�C�A���O�̃Z�b�g
		task.setProgressDialog(progressDialog);
		// ���[�U�̈ʒu����̋����ɉ��������X�̏����擾���A���X�g�ɃZ�b�g�����
		task.setUserLocation(latitude, longitude);
		task.execute(createGNaviApiUrl(latitude, longitude));
	}

	/**
	 * �ܓx�o�x�������Ƃ��Ă���i�r�ɃA�N�Z�X���邽�߂�URL��g�ݗ��Ă�
	 * 
	 * @param latitude
	 *            �@�ܓx
	 * @param longitude
	 *            �@�y�x
	 * @return�@����i�rWEB�T�[�r�X�̃��X�g����API�A�N�Z�XURL
	 */
	private String createGNaviApiUrl(double latitude, double longitude) {
		String tmp = "latitude=" + latitude + "&longitude=" + longitude;
		String url;
		url = SuguCafeConst.GNAVI_REST_API_URL + "?"
				+ SuguCafeConst.GNAVI_API_KEY + "&" + tmp + "&"
				+ SuguCafeConst.GNAVI_API_OPTION;
		Log.d("SHOP_LIST_VIEW", url);
		return url;
	}
}
