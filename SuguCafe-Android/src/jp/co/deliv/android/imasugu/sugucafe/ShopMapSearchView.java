package jp.co.deliv.android.imasugu.sugucafe;

import java.util.List;

import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.customize.CustomItemizedOverlay;
import jp.co.deliv.android.imasugu.customize.CustomOverlayItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

/**
 * Display map to search with address
 * 
 * @author 120007HTT
 * 
 */
public class ShopMapSearchView extends MapActivity {

	/**
	 * Location of User
	 */
	private double userLatitude;
	private double userLongitude;
	
	/**
	 * Control overlay show on map
	 */
	private CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;

	/**
	 * check the new update location
	 */
	private int updateTmp;
	private int update;

	/**
	 * check new search location
	 */
	private String searchCondition;
	private String strTmp;

	/**
	 * GeoPoint of user Location
	 */
	private GeoPoint pointAddress;

	/**
	 * is Search condition ok
	 */
	private Boolean isAdressOk;

	/**
	 * advertise
	 */
	private AdView adView = null;
	private AdRequest adRequest = null;

	/*
	 * �ړI�n�i���X���j�̈ʒu���
	 */
	private ProgressDialog progressDialog = null;
	private TapControlledMapView mapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cafemapview);

		// Set value
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		userLatitude = appContext.getUserLatitude();
		userLongitude = appContext.getUserLongitude();
		searchCondition = appContext.getSearchCondition();
		strTmp = searchCondition;
		update = appContext.getUpdateLocation();
		updateTmp = update;

		// set on click on btn clear
		ImageButton btn_clear = (ImageButton) findViewById(R.id.btn_clear);

		/**
		 * Comment Clear text box search
		 */
		btn_clear.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// clear Edit text
				EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
				txt_seach.setText("");
			}
		});

		/**
		 * do Search when click
		 */
		Button btn_seach = (Button) findViewById(R.id.btn_seach);
		btn_seach.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Search();
			}
		});
		// �R���g���[���̎擾
		mapView = (TapControlledMapView) findViewById(R.id.mapView);
		// ���[�U���ݒn�ƖړI�n�̒��S�Ɉړ�
		GeoPoint pos = new GeoPoint((int) (userLatitude * 1E6),
				(int) (userLongitude * 1E6));
		mapView.getController().animateTo(pos);
		// �Q�_���\�������悤�ɃY�[�����x����ݒ肷��
		mapView.getController().setZoom(17);
		mapView.removeAllViewsInLayout();
		// ���[�U�̌��݈ʒu�Ƃ��X�̃I�[�o�[���C�A�C�e����ǉ�����
		MapItem userLocationItem = new MapItem(userLatitude, userLongitude,
				getResources().getDrawable(R.drawable.user));
		List<Overlay> mapOverlays = mapView.getOverlays();
		if (!mapOverlays.contains(userLocationItem)) {
			mapOverlays.add(userLocationItem);
		}
		adView = new AdView(this, AdSize.BANNER, (String) getResources()
				.getText(R.string.admob_publisherid));
		adView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		adLayout.addView(adView);

		// �L�����N�G�X�g
		adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice("59A3C630685B6B1AEBF8AF2A027FBF35");

		// set hint for Edit Text
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		if (appContext.getTypeSearch() == SuguCafeConst.SEARCH_CAFE_ARROUND) {
			txt_seach.setHint(getString(R.string.you_are_here_search));
		} else {
			txt_seach.setHint(getString(R.string.search_with_address));
		}

		/**
		 * Comment handle key enter on soft keyboard
		 */
		txt_seach.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
					Search();
					return true;
				}
				return false;
			}
		});

		/**
		 * comment close soft keyboard when touch on map
		 */
		mapView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
				return false;
			}
		});
		if (adView != null && adRequest != null) {
			adView.loadAd(adRequest);
		}
		if (appContext.getTypeSearch() == SuguCafeConst.SEARCH_CAFE_ARROUND) {
			createProgressDialog(R.string.msg_info_find_store);
			updateShopListViewItem();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ShowData();
	}

	/**
	 * Comment : create progress dialog when get shop info .
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
	 * Comment : request to get shop info around user .
	 */
	private void updateShopListViewItem() {

		// WEB�T�[�r�X���Ăяo���ăV���b�v���X�g���擾
		GetShopListInfoTask task = new GetShopListInfoTask(this, mapView,
				getResources().getDrawable(R.drawable.home), getResources()
						.getDrawable(R.drawable.user));

		// ���[�U�̈ʒu�����Z�b�g
		task.setUserLocation(this.userLatitude, this.userLongitude);

		// �_�C�A���O�̃Z�b�g
		task.setProgressDialog(progressDialog);

		// ���[�U�̈ʒu����̋����ɉ��������X�̏����擾���A���X�g�ɃZ�b�g�����
		task.execute(createGNaviApiUrl(this.userLatitude, this.userLongitude));

	}

	/**
	 * do Search
	 */
	private void SeachAddress() {

		// WEB�T�[�r�X���Ăяo���ăV���b�v���X�g���擾
		GetShopListInfoTask task = new GetShopListInfoTask(this, mapView,
				getResources().getDrawable(R.drawable.home), getResources()
						.getDrawable(R.drawable.user));

		// ���[�U�̈ʒu�����Z�b�g
		task.setUserLocation(pointAddress.getLatitudeE6() / 1E6,
				pointAddress.getLongitudeE6() / 1E6);

		// task.setUserLocation(this.userLatitude, this.userLongitude);
		// �_�C�A���O�̃Z�b�g
		task.setProgressDialog(progressDialog);

		// ���[�U�̈ʒu����̋����ɉ��������X�̏����擾���A���X�g�ɃZ�b�g�����
		task.execute(createGNaviApiUrl(pointAddress.getLatitudeE6() / 1E6,
				pointAddress.getLongitudeE6() / 1E6));

	}

	/**
	 * create Url
	 * 
	 * @param latitude
	 * @param longitude
	 * @return String
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

	/**
	 * Comment : use google to get Latitude and longitude
	 * 
	 * @param address
	 */
	private void getFromLocation(String address) {
		isAdressOk = false;
		Geocoder geoCoder = new Geocoder(this);
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {
				pointAddress = new GeoPoint((int) (addresses.get(0)
						.getLatitude() * 1E6), (int) (addresses.get(0)
						.getLongitude() * 1E6));
				isAdressOk = true;

			} else {
				new AlertDialog.Builder(this)
						.setTitle("")
						.setMessage(getString(R.string.msg_invalid_address))
						.setPositiveButton(getString(android.R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int which) {
									}
								}).create().show();
			}
		} catch (Exception ee) {
			Log.d("Bug", ee.toString());
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.msg_network_err_title))
					.setMessage(getString(R.string.msg_network_err_message))
					.setPositiveButton(getString(R.string.msg_network_err_button_text),
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int which) {
								}
							}).create().show();

		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * do Search
	 */
	private void Search() {
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		ApplicationData appContext = (ApplicationData) getApplicationContext();

		// close soft keyboard
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);

		// check search condition
		if (txt_seach.getText().toString().length() > 0) {
			getFromLocation(txt_seach.getText().toString());
			if (isAdressOk == true) {
				isAdressOk = false;
				appContext.setUserLatitude(pointAddress.getLatitudeE6() / 1E6);
				appContext
						.setUserLongitude(pointAddress.getLongitudeE6() / 1E6);
				searchCondition = txt_seach.getText().toString();
				appContext.setSearchCondition(searchCondition);
				strTmp = searchCondition;

				// excute search
				createProgressDialog(R.string.msg_info_find_store_dest);
				SeachAddress();
			} else {
				// msg if address is not ok
			}

		}
	}
	
	/**
	 * show data
	 */
	private void ShowData() {
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		userLatitude = appContext.getUserLatitude();
		userLongitude = appContext.getUserLongitude();
		searchCondition = appContext.getSearchCondition();
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		txt_seach.setText(searchCondition);
		update = appContext.getUpdateLocation();
		List<ShopInfo> saveResult = appContext.getShopResult();
		if (update != updateTmp || searchCondition != strTmp) {
			updateTmp = update;
			strTmp = searchCondition;
			/**
			 * �o�b�N�O���E���h�̏������ʂ�UI�ɕ\��
			 */
			GeoPoint gp_location = null;
			GeoPoint gp_user = null;
			// clear all overlays and layout on map
			mapView.removeAllViewsInLayout();
			mapView.getOverlays().clear();
			// show user position
			gp_user = new GeoPoint((int) (userLatitude * 1E6),
					(int) (userLongitude * 1E6));
			MapItem userLocationItem = new MapItem(userLatitude, userLongitude,
					getResources().getDrawable(R.drawable.user));
			List<Overlay> mapOverlays = mapView.getOverlays();
			if (!mapOverlays.contains(userLocationItem)) {
				mapOverlays.add(userLocationItem);
			}
			mapView.getController().animateTo(gp_user);

			itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
					getResources().getDrawable(R.drawable.home), mapView, this);
			mapView.setOnSingleTapListener(new OnSingleTapListener() {

				public boolean onSingleTap(MotionEvent e) {
					itemizedOverlay.hideAllBalloons();
					return false;
				}
			});
			if (saveResult.size()>0) {
				for (ShopInfo item : saveResult) {
					gp_location = new GeoPoint(
							(int) (item.getLatitude() * 1E6),
							(int) (item.getLongitude() * 1E6));
					// set data to show
					CustomOverlayItem overlayItem = new CustomOverlayItem(
							gp_location, item.getCategory(), item.getName(),
							item.getDistance(), item.getOpenHour(),
							item.getMobileCouponFlg(), item.getShopImage1Url(),
							item.getShopImage2Url());
					overlayItem.setLatitude(userLatitude);
					overlayItem.setLongitude(userLongitude);
					overlayItem.setId(item.getId());
					overlayItem.setAddress(item.getAddress());
					overlayItem.setAccess(item.getAccess());
					overlayItem.setTel(item.getTel());
					overlayItem.setHoliday(item.getHoliday());
					overlayItem.setPrShort(item.getPrShort());
					overlayItem.setPrLong(item.getPrLong());
					overlayItem.setBudget(item.getBudget());
					overlayItem.setShopUrlMobile(item.getShopUrlMobile());
					overlayItem.setCouponUrlMobile(item.getCouponUrlMobile());
					overlayItem.setShopLatitude(item.getLatitude());
					overlayItem.setShoplongitude(item.getLongitude());
					// add data to control itemize overlay
					itemizedOverlay.addOverlay(overlayItem);
				}
				mapOverlays = mapView.getOverlays();
				// add itemize on mapview
				mapOverlays.add(itemizedOverlay);				
			}
			mapView.invalidate();
			mapView.getController().setZoom(17);
		}
	}

}
