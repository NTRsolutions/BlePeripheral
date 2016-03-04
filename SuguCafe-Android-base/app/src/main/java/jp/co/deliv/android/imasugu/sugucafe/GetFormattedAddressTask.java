package jp.co.deliv.android.imasugu.sugucafe;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �ܓx�o�x����Z���ϊ����s���o�b�N�O���E���h�^�X�N�B
 * �R���X�g���N�^�ɂďZ����\������TextView���Z�b�g���Ă����ƁA�Z���ϊ����TextView�ɑ΂���
 * �ϊ������Z�����Z�b�g����悤�ɂȂ��Ă���B
 * @author DLV4002
 */
public class GetFormattedAddressTask extends AsyncTask<Double, Void, String> {

	private Activity activity = null;
	private TextView fmtAddressView = null;
	
	public GetFormattedAddressTask(Activity activity, TextView formatedAddressView) {
		this.activity = activity;
		this.fmtAddressView = formatedAddressView;
	}
	
	@Override
	protected String doInBackground(Double... params) {
		Double latitude = params[0];
		Double longitude = params[1];
				
		Geocoder coder = new Geocoder(activity.getApplicationContext(), Locale.JAPAN);
		List<Address> addresses = null;
		try {
			addresses = coder.getFromLocation(latitude, longitude, 10);
		} catch (IOException e) {
			Toast.makeText(activity, "�ʒu���̎擾�Ɏ��s���܂����B�l�b�g���[�N�̐ڑ��󋵂��m�F���Ă��������B", Toast.LENGTH_LONG);
			return new String("-----");
			
		}
		String addressLine;
		if(addresses.size() > 0) {
			Address address = addresses.get(0);
			addressLine = address.getAddressLine(1);
			Log.d("GetFormattedAddressTask", addressLine);
		} else {
			addressLine = "-----";
		}
		
		// �ܓx�o�x���Z���ɕϊ����A�擾�����������ԋp����
		return addressLine;
	}

	@Override
	protected void onPostExecute(String result) {
		
		String formattedAddress = result;
		
		/*
		 * TextView�ɃZ�b�g����
		 */
		if(fmtAddressView != null) {
			fmtAddressView.setText(formattedAddress + " �t��");
		}
	}
}
