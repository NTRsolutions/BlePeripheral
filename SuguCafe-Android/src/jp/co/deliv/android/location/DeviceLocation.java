package jp.co.deliv.android.location;

import java.util.Timer;
import java.util.TimerTask;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

/**
 * �f�o�C�X�̈ʒu���̎擾���T�|�[�g����N���X�B���̃N���X�ł́A�ʏ��AndroidSDK�ł̓T�|�[�g����Ă��Ȃ��ʒu���擾�ɂ�����
 * �^�C���A�E�g�̏������s�����Ƃ��ł���悤�ɂȂ��Ă���B�^�C���A�E�g�������s�����߂ɂ́A LocationListenerWithTimeout�C���^�[�t�F�[�X��
 * ��������K�v������B
 * @author DLV4002
 */
public class DeviceLocation {

	public static final int LOCATION_TIMEOUT = 30000;		// �f�t�H���g�^�C���A�E�g�b��30�b
	
	private static final String TAG = "DeviceLocation";
	
	private LocationManager locationManager = null;
	private LocationListenerWithTimeout locationListener = null;
	private Location latestLocation = null;
	
	/**
	 * �ʒu���擾�v���o�C�_�������I��GPS���g���悤�Ɏw������t���O
	 */
	private boolean useGPS = true;
	
	/**
	 * UI�X���b�h�ɑ΂�������POST���邽�߂̃n���h��
	 */
	private Handler timeoutHandler = null;
	
	/**
	 * �^�C���A�E�g�������n���h�����O���邩�ǂ����̃t���O
	 */
	private boolean handleTimeout = true;
	private Timer timer = null;
	private int timeOut = LOCATION_TIMEOUT;		// �^�C���A�E�g�b�i�~���b�j
	
	/**
	 * �f�o�C�X�̈ʒu���擾�����̏�����
	 * @param locationManager
	 * @param locationListener
	 */
	public DeviceLocation(LocationManager locationManager, LocationListenerWithTimeout locationListener) {
		this.locationManager = locationManager;
		this.locationListener = locationListener;
	}
	
	/**
	 * �^�C���A�E�g�����t��LocationListener���Z�b�g����
	 * @param locationListener
	 */
	public void setLocationListener(LocationListenerWithTimeout locationListener) {
		this.locationListener = locationListener;
	}
	
	/**
	 * �^�C���A�E�g���ԁi�~���b�j���Z�b�g����B0���Z�b�g����ƃ^�C���A�E�g�͔������Ȃ��Ȃ�B
	 * @param timeout	�^�C���A�E�g���ԁi�~���b�j
	 */
	public void setTimeout(int timeout) {
		this.timeOut = timeout;
		if(timeout == 0) {
			handleTimeout = false;
		}
	}
	
	/**
	 * �ʒu���̎擾��GPS�̎g�p��ݒ肷��
	 * @param enableGps	false �ɐݒ肷���GPS�͎g�p���Ȃ��B
	 */
	public void enableGps(boolean enableGps) {
		this.useGPS = enableGps;
	}

	/**
	 * GPS�̎g�p�𖳌��ɂ���
	 */
	public void gpsDisabled() {
		enableGps(false);
	}
	
	/**
	 * GPS�̎g�p�L�����m�F����
	 * @return true �̏ꍇ�AGPS�g�p�Bfalse �̏ꍇGPS���g�p���Ȃ��B
	 */
	public boolean gpsEnabled() {
		return useGPS;
	}
	
	/**
	 * �����̃^�C���A�E�g�b���w�肵�Ĉʒu���擾���N�G�X�g���J�n����
	 * @param timeOut	�^�C���A�E�g�b
	 * @throws ProviderNotFoundException ���p�\�ȃv���o�C�_��������Ȃ������Ƃ��ɃX���[�����
	 */
	public void start(int timeOut) throws ProviderNotFoundException {
		setTimeout(timeOut);
		start();
	}
	
	/**
	 * �ʒu���擾���N�G�X�g���J�n����B
	 * @throws ProviderNotFoundException ���p�\�ȃv���o�C�_��������Ȃ������Ƃ��ɃX���[�����
	 */
	public void start() throws ProviderNotFoundException {
		
		// �v���o�C�_��I������
		String provider = getProvider();
		
		// �v���o�C�_���I���ł��Ȃ������Ƃ��͗�O���X���[����
		if(provider == null) {
			throw new ProviderNotFoundException("Valid location provider was not found.");
		}
		
		/*
		 * �I�������v���o�C�_�̒��߂̈ʒu�����擾���Ă���
		 * ���̈ʒu���̓^�C���A�E�g�����������Ƃ��̈ʒu���Ƃ��Ďg�p�����
		 */
		latestLocation = locationManager.getLastKnownLocation(provider);
		if(latestLocation != null) {
			Log.d(TAG, "Latest Location: " + latestLocation.getLatitude() + "," + latestLocation.getLongitude());
		} else {
			Log.d(TAG, "Latest Location is null.");
		}
		
		// �ʒu���̎擾�����N�G�X�g����
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

		// �^�C���A�E�g����
		if(handleTimeout) {
			Log.d(TAG, "Location Timeout is enabled.");
			
			// UI�X���b�h�ɑ΂�������POST�ł���悤�AHandler���쐬���Ă���
			timeoutHandler = new Handler();

			// �^�C�}�[�ɂ��^�X�N�����s����
			timer = new Timer();
			timer.schedule(new TimerTask() {
	
				@Override
				public void run() {
					// timeOut�~���b��ɌĂяo�����B
					Log.d(TAG, "Grab location process has been time out!!");
					timer.cancel();
					timer.purge();
					timer = null;
					
					/*
					 * TimerTask#run ���\�b�h������UI�X���b�h�ƈقȂ邽��UI�X���b�h�œ��삷�� LocationListener ��
					 * ���ڌĂяo�����Ƃ͂ł��Ȃ��BHandler�ɑ΂��ARunnable�C���^�[�t�F�[�X��n���Ă����邱�Ƃɂ��A
					 * UI�X���b�h�� LocationListener �̃C�x���g���Ăяo���B
					 */
					timeoutHandler.post(new Runnable() {
						public void run() {
						
							// �ʒu���̎擾�������I��
							stop();
							
							// �^�C���A�E�g�n���h�����Ăяo��
							locationListener.onTimeout(DeviceLocation.this, latestLocation);
						}
					});
				}
			},
			timeOut);
			Log.d(TAG, "Timeout time is " + timeOut + "ms.");
		}
	}
	
	/**
	 * �ʒu���擾���N�G�X�g���I������
	 */
	public void stop() {

		Log.d(TAG, "stop: Stoped location update process.");
		
		// �^�C�}�[�̏�Ԃ��m�F���A�K�v�ɉ����Ē�~����
		if(timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		
		// �ʒu���̍X�V���~����B
		if(locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	/**
	 * �ʒu�����擾���邽�߂̃v���o�C�_��I������B
	 * @return	�I�����ꂽ�v���o�C�_
	 */
	protected String getProvider() {
		String provider;
		
		if(useGPS) {
			provider = LocationManager.GPS_PROVIDER;
		} else {			
			// �v���o�C�_�̎擾����
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);		// �v�����x
			criteria.setPowerRequirement(Criteria.POWER_LOW);	// ���e�d�͏���
			criteria.setSpeedRequired(false);					// ���x�s�v
			criteria.setAltitudeRequired(false);				// ���x�s�v
			criteria.setBearingRequired(false);					// ���ʕs�v
			criteria.setCostAllowed(false);						// ��p�̔����s�H
			provider = locationManager.getBestProvider(criteria, true);
		}
		return provider;
	}
	
}
