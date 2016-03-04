package jp.co.deliv.android.imasugu.sugucafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

import jp.co.deliv.android.geocoding.GeoCodingUtils;
import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.http.NetworkErrorAlertDialog;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.customize.CustomItemizedOverlay;
import jp.co.deliv.android.imasugu.customize.CustomOverlayItem;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;
import jp.co.deliv.android.imasugu.webapi.gurunavi.GuruNaviAPIParser;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;

/**
 * get and show data on map view
 * 
 * @author 120007HTT
 * 
 */
public class GetShopListInfoTask extends
		AsyncTask<String, Void, List<ShopInfo>> {
	/*
	 * �o�b�N�O���E���h�^�X�N�̌Ăяo����Activity
	 */
	private MapActivity activity = null;

	/**
	 * object to control Balloon
	 */
	private CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;

	/**
	 * picture of user and location
	 */
	private Drawable drwUser;
	private Drawable drwLocation = null;
	/*
	 * �o�b�N�O���E���h�^�X�N���s���ɕ\������v���O���X�_�C�A���O
	 */
	private ProgressDialog progressDialog = null;
	/*
	 * ListView�Ɋ֘A�t�����Ă���Adapter�N���X
	 */
	private TapControlledMapView mapview = null;

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
	public GetShopListInfoTask(MapActivity activity,
			TapControlledMapView mapview, Drawable drwLocation, Drawable user) {
		this.activity = activity;
		this.mapview = mapview;
		this.drwLocation = drwLocation;
		this.drwUser = user;
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
				GeoPoint gp_location = null;
				GeoPoint gp_user = null;
				// clear all overlays and layout on map
				mapview.removeAllViewsInLayout();
				mapview.getOverlays().clear();

				// show user position
				gp_user = new GeoPoint((int) (userLatitude * 1E6),
						(int) (userLongitude * 1E6));
				MapItem userLocationItem = new MapItem(userLatitude, userLongitude,
						this.drwUser);
				List<Overlay> mapOverlays = mapview.getOverlays();
				if (!mapOverlays.contains(userLocationItem)) {
					mapOverlays.add(userLocationItem);
				}
				mapview.getController().animateTo(gp_user);

				itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
						this.drwLocation, mapview, activity);
				mapview.setOnSingleTapListener(new OnSingleTapListener() {

					public boolean onSingleTap(MotionEvent e) {
						itemizedOverlay.hideAllBalloons();
						return false;
					}
				});
				
				ApplicationData appContext = (ApplicationData) activity.getApplicationContext(); //application context
				List<ShopInfo> saveResult = new ArrayList<ShopInfo>(); // for saving result data of shop
				
				if (result != null) {
					/*
					 * XML���p�[�X�������ʂ�ShopInfo���X�g���A�_�v�^�ɃZ�b�g����
					 */
					HashSet<String> shopIdSet = new HashSet<String>();
					if (result.size() > 0) {

						gp_location = null;

						for (ShopInfo item : result) {
							if (!shopIdSet.contains(item.getId())) {
								saveResult.add(item); // add to save result
								shopIdSet.add(item.getId());// �����X�܂̊Ǘ����X�g
								gp_location = new GeoPoint(
										(int) (item.getLatitude() * 1E6),
										(int) (item.getLongitude() * 1E6));
								// set data to show
								CustomOverlayItem overlayItem = new CustomOverlayItem(
										gp_location, item.getCategory(),
										item.getName(), item.getDistance(),
										item.getOpenHour(), item.getMobileCouponFlg(),
										item.getShopImage1Url(),
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
								overlayItem.setCouponUrlMobile(item
										.getCouponUrlMobile());
								overlayItem.setShopLatitude(item.getLatitude());
								overlayItem.setShoplongitude(item.getLongitude());
								// add data to control itemize overlay
								itemizedOverlay.addOverlay(overlayItem);
							}
						}
						shopIdSet.clear();
						shopIdSet = null;
						mapOverlays = mapview.getOverlays();

						// add itemize on mapview
						mapOverlays.add(itemizedOverlay);
					}
					appContext.setShopResult(saveResult); // save result
					mapview.invalidate();
					mapview.getController().setZoom(17);
				} else {
					NetworkErrorAlertDialog alert = new NetworkErrorAlertDialog(
							this.activity);
					alert.show();
				}
	}
}
