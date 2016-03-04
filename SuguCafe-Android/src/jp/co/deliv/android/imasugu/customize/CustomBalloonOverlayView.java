
package jp.co.deliv.android.imasugu.customize;
import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.sugucafe.R;
import jp.co.deliv.android.imasugu.sugucafe.SuguCafeUtils;

import android.content.Context;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

/**
 * set data for balloon
 * @author 120007HTT
 *
 * @param <Item> overlayItem in google library
 */
public class CustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<CustomOverlayItem> {

	/**
	 * set Views on Balloon
	 */
	private TextView catchCopyView;
	private TextView shopNameView;
	private TextView distanceView;
	private TextView walkTimeView;
	private TextView openHourView;
	private TextView hasCouponView;
	/**
	 * image of location
	 */
	private ProgressBar imageProgress;
	private ImageView imageView;
	
	public CustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	/**
	 * setup the view of Balloon
	 */
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.balloon_data_view, parent);
		
		// setup our fields
		catchCopyView = (TextView)v.findViewById(R.id.catchCopy2);
		shopNameView = (TextView)v.findViewById(R.id.shopName);
		distanceView = (TextView)v.findViewById(R.id.shopDistance);
		walkTimeView = (TextView)v.findViewById(R.id.shopTime);
		openHourView = (TextView)v.findViewById(R.id.shopOpenHour);
		hasCouponView = (TextView)v.findViewById(R.id.shopCoupon);
		imageView = (ImageView)v.findViewById(R.id.shopImage);
		imageProgress = (ProgressBar)v.findViewById(R.id.shopImageProgress);

	}

	/**
	 * set data for balloon
	 */
	@Override
	protected void setBalloonData(CustomOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		
		// �L���b�`�R�s�[�Q���Z�b�g
		if(item.getCategory() == null || "".equals(item.getCategory())) {
			catchCopyView.setVisibility(View.GONE);
		} else {
			catchCopyView.setVisibility(View.VISIBLE);
			catchCopyView.setText(item.getCategory());
		}
		
		// ���X�̖��O���Z�b�g
		shopNameView.setTypeface(Typeface.DEFAULT_BOLD);
		shopNameView.setText(item.getName());
		
		// �������Z�b�g
		distanceView.setText("��" + Math.round(item.getDistance()) + "m");
		
		// �k���ł̈ړ����Ԃ��Z�b�g�i3km/h:50m/min �Ōv�Z�j
		walkTimeView.setText("��" + Math.round(item.getDistance() / 50D) + "��");
		
		// �c�Ǝ��Ԃ��Z�b�g
		openHourView.setText(item.getOpenHour());
		
		// �N�[�|������̏ꍇ�u�N�[�|������v�r���[��\��
		if("1".equalsIgnoreCase(item.getMobileCouponFlg())) {
			hasCouponView.setVisibility(View.VISIBLE);
		} 
		else 
		{
			hasCouponView.setVisibility(View.GONE);
		}
		// URL�摜���_�E�����[�h���Ă���ImageView�ɃZ�b�g����
		imageView.setVisibility(View.GONE);		// ��U�C���[�W���������Ă���

		imageProgress.setVisibility(View.VISIBLE);	// �C���[�W�̑���Ƀv���O���X�o�[��\�����Ă���
								
		// �C���[�W���_�E�����[�h����
		String imageUrl = item.getImage1URL();
		if(!SuguCafeUtils.isValidShopImageUrl(imageUrl)) 
		{
			imageUrl = item.getImage2URL();
		}
		imageView.setTag(imageUrl);
		ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, true);
		imageDLTask.execute(imageUrl);
		
	}
}
