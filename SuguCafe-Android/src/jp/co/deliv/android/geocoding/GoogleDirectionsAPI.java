package jp.co.deliv.android.geocoding;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jp.co.deliv.android.http.HttpClient;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;


/**
 * Google Maps API WEB�T�[�r�X�� Directions API ���g�p���āA
 * �ܓx�o�x�Ŏw�肳���Q�_�Ԃ̃��[�g�������s���A�o���n����ړI�n�܂ł�
 * �o�H�����擾����B����API���g�p����Ƃ���GoogleMap�ƕ��p���邱�ƁB
 * @author Atsushi
 */
public class GoogleDirectionsAPI {  

	/*
	 * DirectionsAPI�Ŏ擾����^�O���
	 */
	private static final String DIRECTIONS_RESPONSE_TAG = "DirectionsResponse";
	private static final String STEP_TAG = "step";
	private static final String START_LOCATION_TAG = "start_location";
	private static final String END_LOCATION_TAG = "end_location";		
	private static final String LATITUDE_TAG = "latitude";
	private static final String LONGITUDE_TAG = "longitude";

	/*
	 * �o�H��������
	 */
	private List<RouteStep> routeSteps = null;
	
	/*
	 * DirectionsAPI�̃A�N�Z�XURL
	 */
    private String urlStr;  
  
    private static final String API_URL = "http://maps.google.com/maps/api/directions/xml?mode=walking&sensor=true";
        
    public GoogleDirectionsAPI(double orgLat, double orgLng, double destLat, double destLng) {
    	String apiOption = "origin=" + orgLat + "," + orgLng + "&destination=" + destLat + "," + destLng;
        this.urlStr = API_URL + "&" + apiOption;  
    }  
	  
	public void parse() {
		
        XmlPullParser parser = Xml.newPullParser();  
        
        try {
        	byte[] data = HttpClient.getByteArrayFromURL(urlStr);
        	parser.setInput(new StringReader(new String(data, "UTF-8")));
        	int eventType = parser.getEventType();
        	boolean isFinished = false;

        	RouteStep currentStep = null;
        	GeoLocation currentLocation = null;
        	
        	while (eventType != XmlPullParser.END_DOCUMENT && !isFinished) {
        		
	            String name = null;
	            
	            switch (eventType) {
	            
	            case XmlPullParser.START_DOCUMENT:  
	            	routeSteps = new ArrayList<RouteStep>();  
	                break;  
	            
	            case XmlPullParser.START_TAG:

	            	// �J�n�^�O���擾
	                name = parser.getName();
	                	                
	                /*
	                 * �^�O���ɉ���������
	                 */
	                if (name.equalsIgnoreCase(STEP_TAG)) {
	                	// step�^�O���J�n���ꂽ��RouteStep�N���X�̃C���X�^���X�����
	                    currentStep = new RouteStep();  
	                } else if (currentStep != null) {
	                    if (name.equalsIgnoreCase(START_LOCATION_TAG)) {  
	                    	currentLocation = currentStep.getStartLocation();
	                    } else if (name.equalsIgnoreCase(END_LOCATION_TAG)) {  
	                    	currentLocation = currentStep.getEndLocation();
	                    } else if (name.equals(LATITUDE_TAG)) {
	                    	if(currentLocation != null) {
	                    		currentLocation.setLatitude(Double.parseDouble(parser.nextText()));
	                    	}
	                    } else if (name.equals(LONGITUDE_TAG)) {
	                    	if(currentLocation != null) {
	                    		currentLocation.setLongitude(Double.parseDouble(parser.nextText()));
	                    	}
	                    }
	                }  
	                break;  
	            
	            case XmlPullParser.END_TAG:
	            	
	            	// �I���^�O�����擾
	                name = parser.getName();

	                if(currentStep != null && currentLocation != null) {
		                if(name.equalsIgnoreCase(START_LOCATION_TAG)) {
		                	currentStep.setStartLocation(currentLocation);
		                	currentLocation = null;
		                } else if(name.equalsIgnoreCase(END_LOCATION_TAG)) {
		                	currentStep.setEndLocation(currentLocation);
		                	currentLocation = null;		                	
		                }
	                }
	                if(name.equalsIgnoreCase(STEP_TAG) && currentStep != null) {
	                	routeSteps.add(currentStep);
	                	currentStep = null;
	                } else if(name.equalsIgnoreCase(DIRECTIONS_RESPONSE_TAG)) {
	                	isFinished = true;
	                }
	                break;  
	            }  
	            eventType = parser.next();  
        	}  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	}  
}
