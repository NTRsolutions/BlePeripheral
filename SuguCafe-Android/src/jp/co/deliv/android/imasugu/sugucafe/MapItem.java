package jp.co.deliv.android.imasugu.sugucafe;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Google�}�b�v��ɕ\������I�[�o�[���C�A�C�e��
 * @author Atsushi
 *
 */
public class MapItem extends Overlay {

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		
		return super.onTap(arg0, arg1);
	}

	private int latitudeE6;
	private int longitudeE6;
	private Drawable marker;
	
	public MapItem(double latitude, double longitude, Drawable marker) {
		this.latitudeE6 = (int)(Math.round(latitude * 1E6));
		this.longitudeE6 = (int)(Math.round(longitude * 1E6));
		this.marker = marker;
	}

	/**
	 * Map��ɕ\������A�C�e���̕`��
	 * @see com.google.android.maps.Overlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		//�ܓx�o�x�����ʏ�̈ʒu���擾����
		GeoPoint point = new GeoPoint(this.latitudeE6, this.longitudeE6);
		Point pt = mapView.getProjection().toPixels(point, null);
		
		// �A�C�R���̕`��
		int w = marker.getIntrinsicWidth();
		int h = marker.getIntrinsicHeight();
		marker.setBounds(pt.x - w/2, pt.y - h/2, pt.x + w/2, pt.y + h/2);
		marker.draw(canvas);
	}
}
