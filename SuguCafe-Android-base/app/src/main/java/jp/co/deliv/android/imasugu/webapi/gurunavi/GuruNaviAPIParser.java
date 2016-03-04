package jp.co.deliv.android.imasugu.webapi.gurunavi;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;


/**
 * ����i�rWEB�T�[�r�X�̃��X�g��������API���R�[�����A�擾����XML�f�[�^���p�[�X����B
 * �p�[�X�������ʂ́AShopInfo�N���X�̃��X�g�Ƃ��ĕԋp����B
 * BR�^�O���܂܂��ꍇ�́A���s�ɒu������B
 * @author Atsushi
 */
public class GuruNaviAPIParser implements ShopInfoAPIPaser {  

	private static final String RESULT_TAG = "response";
	private static final String SHOP_TAG = "rest";
	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	
	private static final String CATEGORY_TAG = "category";
	
	private static final String LATITUDE_TAG = "latitude";
	private static final String LONGITUDE_TAG = "longitude";

	private static final String SHOP_IMAGE_1_TAG = "shop_image1";
	private static final String SHOP_IMAGE_2_TAG = "shop_image2";
		
	private static final String ADDRESS_TAG = "address";
	
	private static final String TEL_TAG = "tel";
	private static final String FAX_TAG = "fax";
	
	private static final String OPEN_TIME_TAG = "opentime";
	private static final String HOLIDAY_TAG = "holiday";
	
	private static final String PR_SHORT_TAG = "pr_short";
	private static final String PR_LONG_TAG = "pr_long";
	
	private static final String BUDGET_TAG = "budget";
	//private static final String PC_COUPON_TAG = "pc_coupon";
	
	private static final String PC_URL_TAG = "url";
	private static final String MOBILE_URL_TAG = "url_mobile";
	//private static final String MOBILE_SITE_TAG = "mobile_site";
	private static final String MOBILE_COUPON_TAG = "mobile_coupon";
	
    private String urlStr;  
  
    public GuruNaviAPIParser(String urlStr) {  
        this.urlStr = urlStr;  
    }  
	
    private String getText(XmlPullParser paser) throws XmlPullParserException, IOException {
    	String text = paser.nextText();
    	text = text.replace("<BR>", "\n");
    	text = text.replace("<br>", "\n");
    	text = text.replace("<BR />", "\n");
    	text = text.replace("<br />", "\n");
    	return text;
    }
    
	public List<ShopInfo> parse() {
		
        List<ShopInfo> list = null;
        Stack<String> tagStack = new Stack<String>();
        XmlPullParser parser = Xml.newPullParser();  
        
        try {
        	/*
        	 * URL�ɃA�N�Z�X����XML�f�[�^��byte�z��Ŏ擾����
        	 */
        	byte[] data = HttpClient.getByteArrayFromURL(urlStr);
        	
        	// �f�[�^���擾�ł��Ȃ������Ƃ���null��Ԃ�
        	if(data == null) {
        		return null;
        	}
        	
        	// XmlPullParser�Ƀf�[�^���Z�b�g����
        	parser.setInput(new StringReader(new String(data, "UTF-8")));
        	int eventType = parser.getEventType();
        	ShopInfo currentShop = null;
        	boolean isFinished = false;

        	while (eventType != XmlPullParser.END_DOCUMENT && !isFinished) {
        		
	            String name = null;
	            //int depth = 0;
	            
	            switch (eventType) {
	            
	            case XmlPullParser.START_DOCUMENT:  
	                list = new ArrayList<ShopInfo>();  
	                break;  
	            
	            case XmlPullParser.START_TAG:

	            	// �J�n�^�O���擾
	                name = parser.getName();
	                
	                // �K�w���擾
	                //depth = parser.getDepth();
	                
	                // �^�O�X�^�b�N�ɐς�
	                tagStack.push(name);
	                
	                /*
	                 * �^�O���ɉ���������
	                 */
	                if (name.equalsIgnoreCase(SHOP_TAG)) {
	                	// shop�^�O���J�n���ꂽ��ShopInfo�N���X�̃C���X�^���X�����
	                    currentShop = new ShopInfo();  
	                } else if (currentShop != null) {
	                    if (name.equalsIgnoreCase(ID_TAG)) {  
	                        currentShop.setId(getText(parser));
	                    } else if (name.equalsIgnoreCase(NAME_TAG)) {  
	                        currentShop.setName(getText(parser));  
	                    } else if (name.equals(ADDRESS_TAG)) {
	                    	currentShop.setAddress(getText(parser));
	                    } else if (name.equals(LATITUDE_TAG)) {
	                    	currentShop.setLatitude(Double.parseDouble(getText(parser)));
	                    } else if (name.equals(LONGITUDE_TAG)) {
	                    	currentShop.setLongitude(Double.parseDouble(getText(parser)));
	                    } else if (name.equals(PR_LONG_TAG)) {
	                    	currentShop.setPrShort(getText(parser));
	                    } else if (name.equals(PR_SHORT_TAG)) {
	                    	currentShop.setPrLong(getText(parser));
	                    } else if (name.equals(OPEN_TIME_TAG)) {
	                    	currentShop.setOpenHour(getText(parser));
	                    } else if (name.equals(ADDRESS_TAG)) {
	                    	currentShop.setAddress(getText(parser));
	                    } else if(name.equalsIgnoreCase(BUDGET_TAG)) {
                    		currentShop.setBudget(getText(parser));
	                    } else if(name.equalsIgnoreCase(SHOP_IMAGE_1_TAG)) {
	                    	currentShop.setShopImage1Url(getText(parser));
	                    } else if(name.equalsIgnoreCase(SHOP_IMAGE_2_TAG)) {
	                    	currentShop.setShopImage2Url(getText(parser));	                    	
	                    } else if(name.equalsIgnoreCase(PC_URL_TAG)) {
                    		currentShop.setShopUrlPC(getText(parser));
	                    } else if(name.equalsIgnoreCase(MOBILE_URL_TAG)) {
                    		currentShop.setShopUrlMobile(getText(parser));
	                    } else if(name.equalsIgnoreCase(MOBILE_COUPON_TAG)) {
	                    	currentShop.setMobileCouponFlg(getText(parser));
	                    } else if(name.equalsIgnoreCase(TEL_TAG)) {
	                    	currentShop.setTel(getText(parser));
	                    } else if(name.equalsIgnoreCase(FAX_TAG)) {
	                    	currentShop.setFax(getText(parser));
	                    } else if(name.equalsIgnoreCase(HOLIDAY_TAG)) {
	                    	currentShop.setHoliday(getText(parser));
	                    } else if(name.equalsIgnoreCase(CATEGORY_TAG)) {
	                    	currentShop.setCategory(getText(parser));
	                    }
	                }  
	                break;  
	            
	            case XmlPullParser.END_TAG:
	            	
	            	// �I���^�O�����擾
	                name = parser.getName();
	                
	                if (name.equalsIgnoreCase(SHOP_TAG) && currentShop != null) {  
	                	// shop�^�O�̏I���̏ꍇ�́A���X�g��ShopInfo��ǉ�����
	                    list.add(currentShop);
	                    currentShop = null;
	                } else if (name.equalsIgnoreCase(RESULT_TAG)) {
	                	// ���[�g�m�[�h�̏I���̏ꍇ�́A�����������I������
	                    isFinished = true;
	                    
	                    // �^�O�X�^�b�N�͋�ɂȂ��Ă���͂�
	                    if(tagStack.empty()) {
	                    	Log.e("GNaviAPIParser", "����i�rWEB�T�[�r�X���X�g��������API��XML��͂����������B�v�����B");
	                    }
	                }  
	                break;  
	            }  
	            eventType = parser.next();  
        	}  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	    return list;  
	}  
}
