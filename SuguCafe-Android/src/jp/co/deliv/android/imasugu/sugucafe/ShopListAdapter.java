package jp.co.deliv.android.imasugu.sugucafe;

import java.util.List;

import jp.co.deliv.android.cache.ImageMemoryCache;
import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.sugucafe.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * �C�}�����V���[�Y�F�����J�t�F �V���b�v���X�g�A�N�e�B�r�e�B��ListView�Ɋ֘A�Â�����A�_�v�^�N���X
 * ShopInfo�I�u�W�F�N�g�̕��������r���[�ɃZ�b�g���A�摜�f�[�^�̓o�b�N�O���E���h�^�X�N�Ƃ��Ď��s���A �摜�f�[�^�擾��A�C���[�W�r���[�ɃZ�b�g����B
 * 
 * @author Atsushi
 */
public class ShopListAdapter extends ArrayAdapter<ShopInfo> {

	private LayoutInflater inflater = null;
	private List<ShopInfo> items = null;

	/**
	 * CafeShopListView �Ɋ֘A�Â��� Adapter�N���X�̃R���X�g���N�^
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ShopListAdapter(Context context, int textViewResourceId,
			List<ShopInfo> objects) {
		super(context, textViewResourceId, objects);

		this.items = objects;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ListView�̍s�f�[�^�\�����ɌĂ΂��B
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if(view==null){
			view = inflater.inflate(R.layout.shoplistview_row, null);
		}
		// �\�����ׂ��f�[�^�̎擾
		ShopInfo item = (ShopInfo) items.get(position);
		if (item != null) {

			// �L���b�`�R�s�[�Q���Z�b�g
			TextView catchCopyView = (TextView) view
					.findViewById(R.id.catchCopy2);
			if (item.getCategory() == null || "".equals(item.getCategory())) {
				catchCopyView.setVisibility(View.GONE);
			} else {
				catchCopyView.setVisibility(View.VISIBLE);
				catchCopyView.setText(item.getCategory());
			}

			// ���X�̖��O���Z�b�g
			TextView shopNameView = (TextView) view.findViewById(R.id.shopName);
			shopNameView.setTypeface(Typeface.DEFAULT_BOLD);
			shopNameView.setText(item.getName());

			// �������Z�b�g
			TextView distanceView = (TextView) view
					.findViewById(R.id.shopDistance);
			distanceView.setText("��" + Math.round(item.getDistance()) + "m");

			// �k���ł̈ړ����Ԃ��Z�b�g�i3km/h:50m/min �Ōv�Z�j
			TextView walkTimeView = (TextView) view.findViewById(R.id.shopTime);
			walkTimeView.setText("��" + Math.round(item.getDistance() / 50D)
					+ "��");

			// �c�Ǝ��Ԃ��Z�b�g
			TextView openHourView = (TextView) view
					.findViewById(R.id.shopOpenHour);
			openHourView.setText(item.getOpenHour());

			// �N�[�|������̏ꍇ�u�N�[�|������v�r���[��\��
			TextView hasCouponView = (TextView) view
					.findViewById(R.id.shopCoupon);
			if ("1".equalsIgnoreCase(item.getMobileCouponFlg())) {
				hasCouponView.setVisibility(View.VISIBLE);
			} else {
				hasCouponView.setVisibility(View.GONE);
			}

			// URL�摜���_�E�����[�h���Ă���ImageView�ɃZ�b�g����
			ImageView imageView = (ImageView) view.findViewById(R.id.shopImage);
			imageView.setVisibility(View.GONE); // ��U�C���[�W���������Ă���
			ProgressBar imageProgress = (ProgressBar) view
					.findViewById(R.id.shopImageProgress);
			imageProgress.setVisibility(View.VISIBLE); // �C���[�W�̑���Ƀv���O���X�o�[��\�����Ă���

			// �C���[�W���_�E�����[�h����
			//ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView,
					//imageProgress, true);
			String imageUrl = item.getShopImage1Url();
			if (!SuguCafeUtils.isValidShopImageUrl(imageUrl)) {
				imageUrl = item.getShopImage2Url();
			}
			Bitmap image = ImageMemoryCache.getImage(imageUrl);
			imageView.setTag(imageUrl);
			if(image == null)
			{
				ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, true);
				imageDLTask.execute(imageUrl);
			}
			else
			{				
				imageView.setImageBitmap(this.Resize(image, SuguCafeConst.LIST_VIEW_IMAGE_WIDTH, SuguCafeConst.LIST_VIEW_IMAGE_HEIGHT));
				imageProgress.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
			}
			//imageDLTask.execute(imageUrl);
		}

		return view;
	}
	
	/**
	 * resize image 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap Resize(Bitmap image, int width, int height)
	{
		/*
         * �摜���w��̑傫���Ƀ��T�C�Y����
         */
    	Bitmap resizeImage = null;
        if(image.getWidth() != width && image.getHeight() != height) {
        	int srcWidth = image.getWidth();
        	int srcHeight = image.getHeight();
        	
        	Matrix scaleMat = new Matrix();
        	float scaleX = (float)width / (float)srcWidth;
        	float scaleY = (float)height / (float)srcHeight;
        	
        	scaleMat.postScale(scaleX, scaleY);
        	
        	// resize
        	resizeImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), scaleMat, true);
        	
        } else {
        	resizeImage = image;
        }
        // �C���[�W��Ԃ�
        return resizeImage;  
	}

}
