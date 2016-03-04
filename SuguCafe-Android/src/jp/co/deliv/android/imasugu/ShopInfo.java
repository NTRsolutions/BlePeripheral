package jp.co.deliv.android.imasugu;

/**
 * ���̃N���X�͍������V���[�Y�ŕ\������X�܂̑������`���Ă���Bean�N���X�B
 * ShopListView��ShopDetailView�ŕ\�����邨�X�̏���`�B
 * ����i�rAPI�Ŏ擾�ł��鑮���ɍœK������Ă���B
 * 
 * �������J�t�F��
 * ����NAVI��API���g�p���Ď擾���������i�[
 * 
 * @author Atsushi
 */
public class ShopInfo {

	private String id;					// �X�܎���ID�i����i�r�j
	private String name;				// �X�ܖ��i����i�r�j
	private String address;				// �X�܏Z���i����i�r�j
	private String access;				// �Ŋ��w����̃A�N�Z�X�i����i�r�j
	private String openHour;			// ���X�̉c�Ǝ��ԁi����i�r�j
	
	private String category;			// �J�e�S��
	private String prShort;				// �Z��PR�i����i�r�j
	private String prLong;				// ����PR�i����i�r�j
		
	private String budget;				// ���ϗ\�Z�i����i�r�j
	
	private String tel;					// ���X�ւ̓d�b�ԍ��i����i�r�j
	private String fax;					// ���X�ւ�FAX�ԍ��i����i�r�j
		
	private String shopImage1Url;		// ���X�̉摜�P�i����i�r�j
	private String shopImage2Url;		// ���X�̉摜�Q�i����i�r�j
	
	private String holiday;				// �x�Ɠ��i����i�r�j
	private String mobileCouponFlg;		// ���o�C���N�[�|���t���O�i����i�r�j
	
	private String shopUrlPC;			// �X��URL�iPC�����j
	private String shopUrlMobile;		// �X��URL�i���o�C�������j
	
	private String couponUrlMobile;		// �N�[�|��URL�i���o�C�������j
	
	private double latitude;		// �X�܈ʒu�F�ܓx
	private double longitude;		// �X�܈ʒu�F�y�x
	
	private double distance;		// ���[�U���݈ʒu����̋����i�P�ʁF���[�g���j
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getOpenHour() {
		return openHour;
	}
	public void setOpenHour(String openHour) {
		this.openHour = openHour;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getShopUrlPC() {
		return shopUrlPC;
	}
	public void setShopUrlPC(String shopUrlPC) {
		this.shopUrlPC = shopUrlPC;
	}
	public String getCouponUrlMobile() {
		return couponUrlMobile;
	}
	public void setCouponUrlMobile(String couponUrlMobile) {
		this.couponUrlMobile = couponUrlMobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getShopImage1Url() {
		return shopImage1Url;
	}
	public void setShopImage1Url(String shopImage1Url) {
		this.shopImage1Url = shopImage1Url;
	}
	public String getShopImage2Url() {
		return shopImage2Url;
	}
	public void setShopImage2Url(String shopImage2Url) {
		this.shopImage2Url = shopImage2Url;
	}
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	public String getMobileCouponFlg() {
		return mobileCouponFlg;
	}
	public void setMobileCouponFlg(String mobileCouponFlg) {
		this.mobileCouponFlg = mobileCouponFlg;
	}
	public String getShopUrlMobile() {
		return shopUrlMobile;
	}
	public void setShopUrlMobile(String shopUrlMobile) {
		this.shopUrlMobile = shopUrlMobile;
	}
	public String getPrShort() {
		return prShort;
	}
	public void setPrShort(String prShort) {
		this.prShort = prShort;
	}
	public String getPrLong() {
		return prLong;
	}
	public void setPrLong(String prLong) {
		this.prLong = prLong;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
