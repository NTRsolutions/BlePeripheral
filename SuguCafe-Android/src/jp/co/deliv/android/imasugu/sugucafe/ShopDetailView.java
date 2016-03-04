package jp.co.deliv.android.imasugu.sugucafe;

import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.ShopInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ���X�̏ڍ׏���\������A�N�e�B�r�e�B
 * �u�����ɍs���v�{�^���ŁA�n�}��ʂɑJ�ڂ���B
 * @author Atsushi
 */
public class ShopDetailView extends Activity {

	/*
	 * ���[�U�ʒu���
	 */
	private double userLatitude;
	private double userLongitude;
	
	/*
	 * ���X�̏ڍ׏��
	 */
	private ShopInfo shopInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopdetailview);
		
		/*
		 * Intent�Œl���擾
		 */
		Intent intent = getIntent();
		
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		// ���[�U�̈ʒu�����擾
		userLatitude =appContext.getUserLatitude();
		userLongitude = appContext.getUserLongitude();

		// �I���������X�̏����擾
		shopInfo = new ShopInfo();
		shopInfo.setId(intent.getStringExtra(SuguCafeConst.SHOP_ID));
		shopInfo.setName(intent.getStringExtra(SuguCafeConst.SHOP_NAME));
		shopInfo.setAddress(intent.getStringExtra(SuguCafeConst.SHOP_ADDRESS));
		shopInfo.setAccess(intent.getStringExtra(SuguCafeConst.SHOP_ACCESS));
		shopInfo.setTel(intent.getStringExtra(SuguCafeConst.SHOP_TEL));
		shopInfo.setOpenHour(intent.getStringExtra(SuguCafeConst.SHOP_OPENHOUR));
		shopInfo.setHoliday(intent.getStringExtra(SuguCafeConst.SHOP_HOLIDAY));
		shopInfo.setCategory(intent.getStringExtra(SuguCafeConst.SHOP_CATEGORY));
		shopInfo.setPrShort(intent.getStringExtra(SuguCafeConst.SHOP_PR_SHORT));
		shopInfo.setPrLong(intent.getStringExtra(SuguCafeConst.SHOP_PR_LONG));
		shopInfo.setBudget(intent.getStringExtra(SuguCafeConst.SHOP_BUDGET));
		shopInfo.setShopImage1Url(intent.getStringExtra(SuguCafeConst.SHOP_IMAGE_1));
		shopInfo.setShopImage2Url(intent.getStringExtra(SuguCafeConst.SHOP_IMAGE_2));
		shopInfo.setShopUrlMobile(intent.getStringExtra(SuguCafeConst.SHOP_MOBILE_URL));
		shopInfo.setMobileCouponFlg(intent.getStringExtra(SuguCafeConst.SHOP_MOBILE_COUPON_FLG));
		shopInfo.setCouponUrlMobile(intent.getStringExtra(SuguCafeConst.SHOP_COUPON_URL));
		shopInfo.setLatitude(intent.getDoubleExtra(SuguCafeConst.SHOP_LATITUDE, 0D));
		shopInfo.setLongitude(intent.getDoubleExtra(SuguCafeConst.SHOP_LONGITUDE, 0D));
		shopInfo.setDistance(intent.getDoubleExtra(SuguCafeConst.SHOP_DISTANCE, 0D));
				
		/*
		 * ���X����\��
		 */
		((TextView)findViewById(R.id.shopName)).setText(shopInfo.getName());
		((TextView)findViewById(R.id.shopAddress)).setText(shopInfo.getAddress());
		((TextView)findViewById(R.id.shopAccess)).setText(shopInfo.getAccess());
		((TextView)findViewById(R.id.shopOpenHour)).setText(shopInfo.getOpenHour());
		((TextView)findViewById(R.id.shopCategory)).setText(shopInfo.getCategory());
		((TextView)findViewById(R.id.shopCatchCopy1)).setText(shopInfo.getPrLong());
		((TextView)findViewById(R.id.shopAverage)).setText(shopInfo.getBudget());
		((TextView)findViewById(R.id.shopTel)).setText(shopInfo.getTel());
		((TextView)findViewById(R.id.shopHoliday)).setText(shopInfo.getHoliday());
		
		// PR_LONG�i�ݒ肪�Ȃ��ꍇ�͔�\���j
		TextView shopPrLong = (TextView)findViewById(R.id.shopCatchCopy2);
		String strPrLong = shopInfo.getPrLong();
		if(strPrLong == null || "".equals(strPrLong)) {
			shopPrLong.setVisibility(View.GONE);
		} else {
			shopPrLong.setVisibility(View.VISIBLE);
			shopPrLong.setText(strPrLong);
		}

		// PR_SHORT�i�ݒ肪�Ȃ��ꍇ�͔�\���j
		TextView shopPrShort = (TextView)findViewById(R.id.shopCatchCopy2);
		String strText = shopInfo.getPrShort();
		if(strText == null || "".equals(strText)) {
			shopPrShort.setVisibility(View.GONE);
		} else {
			shopPrShort.setVisibility(View.VISIBLE);
			shopPrShort.setText(strText);
		}

		// �傫���摜��\������
		ProgressBar imageProgress = (ProgressBar)findViewById(R.id.shopImageProgress);
		ImageView imageView = (ImageView)findViewById(R.id.shopImage);
		String imageUrl = shopInfo.getShopImage1Url();
		imageView.setTag(imageUrl);
		ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, false);		
		if(!SuguCafeUtils.isValidShopImageUrl(imageUrl)) {
			imageUrl = shopInfo.getShopImage2Url();
		}
		imageDLTask.execute(imageUrl);
		
		/*
		 * �u�����ɍs���v�{�^���������ƒn�}�r���[��\������
		 */
		Button goNowButton = (Button)findViewById(R.id.btnGoNow);
		goNowButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				/*/
				Intent intent = new Intent(ShopDetailView.this, CafeMapView.class);
				intent.putExtra("USER_LATITUDE", userLatitude);
				intent.putExtra("USER_LONGITUDE", userLongitude);
				intent.putExtra("DESTINATION_LATITUDE", shopInfo.getLatitude());
				intent.putExtra("DESTINATION_LONGITUDE", shopInfo.getLongitude());
				startActivity(intent);
				*/

				//����������Ȃ����AUI�𐧌�ł��Ȃ��̂����܂������B�B�B
				// Google Map �Ōo�H����
				///*
				String sAddr = userLatitude + "," + userLongitude;
				String dAddr = shopInfo.getLatitude() + "," + shopInfo.getLongitude();
				String url = "http://maps.google.com/maps?saddr=" + sAddr + "&daddr=" + dAddr + "&dirflg=w";
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
				intent.setData(Uri.parse(url));
				startActivity(intent);
				// */
			}
		});
		
		/**
		 * �d�b���|����
		 */
		Button btnTelNow = (Button)findViewById(R.id.btnTelephone);
		if(shopInfo.getTel() != null && !"".equals(shopInfo.getTel())) {
			btnTelNow.setEnabled(true);
			btnTelNow.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
					String phoneNo = shopInfo.getTel();
					phoneNo = phoneNo.replace("-", "");
					Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));  
					startActivity(i);					
				}
			});
		} else {
			btnTelNow.setEnabled(false);
		}
		
		/*
		 * �u�N�[�|���\���v�{�^���������ƃN�[�|����\���i�z�b�g�y�b�p�[�₮��i�r���肾�ȁB�B�B�H�׃��O�̂Ƃ��͂ǂ����悤���B�B�B�j
		 */
		Button btnShowCoupon = (Button)findViewById(R.id.btnShowCoupon);
		if("1".equalsIgnoreCase(shopInfo.getMobileCouponFlg())) {
			btnShowCoupon.setVisibility(View.VISIBLE);
			btnShowCoupon.setEnabled(true);
			btnShowCoupon.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(shopInfo.getShopUrlMobile()));  
					startActivity(i);
				}
			});
		} else {
			btnShowCoupon.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// ������擾�̂��߃��\�[�X���擾
		Resources res = getResources();
		
		// �o�[�W������񃁃j���[��ǉ�
    	menu.add(Menu.NONE, SuguCafeConst.MENU_ID_ABOUT, Menu.NONE, res.getText(R.string.menu_about))
    	.setIcon(android.R.drawable.ic_menu_info_details);
    	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean ret = true;
		switch(item.getItemId()) {
		
		// �o�[�W�������I����
		case SuguCafeConst.MENU_ID_ABOUT:
			SuguCafeUtils.showAboutInformation(this);
			break;
			
		// �f�t�H���g����
		default:
			ret = super.onOptionsItemSelected(item); 
		}
		return ret;
	}

}
