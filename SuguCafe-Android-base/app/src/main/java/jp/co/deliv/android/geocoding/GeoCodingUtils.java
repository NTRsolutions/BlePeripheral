package jp.co.deliv.android.geocoding;

/**
 * �ܓx�o�x�Ȃǂ�p�����W�I�R�[�f�B���O�ɂ����郆�[�e�B���e�B�N���X�B
 * �Q�_�̈ܓx�o�x����A�Q�_�Ԃ̋��������߂�A�ܓx�o�x����Z���ɕϊ����铙�֗̕����\�b�h����`����Ă���B
 * @author Atsushi
 */
public class GeoCodingUtils {

    // �����a(WGS84)
    private static final double a = 6378137D;

    // �G����(WGS84)
    private static final double f = 1D / 298.257222101D;
    
	/**
	 * �q���x�j�̌������g���ĂQ�_�Ԃ̋��������߂�
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return �Q�_�Ԃ̋����i�P�ʁF���[�g���j
	 * 
	 * @see http://www.kashmir3d.com/kash/manual-e/std_siki.htm
	 * @see http://vldb.gsi.go.jp/sokuchi/surveycalc/algorithm/ellipse/ellipse.htm
	 */
	public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
		
        //�ܓx�o�x�����W�A���ɕϊ�
        double radLatStart = latitude1 * Math.PI/180D;
        double radLonStart = longitude1 * Math.PI/180D;
        double radLatEnd = latitude2 * Math.PI/180D;
        double radLonEnd = longitude2 * Math.PI/180D;
        
        //��_�Ԃ̕��ψܓx�i���W�A���j
        double avgLat = (radLatStart + radLatEnd) / 2D;
        
        //�G�����̋t��
        double F = 1D / f;
        
        //��ꗣ�S��
        double e = (Math.sqrt(2 * F - 1)) / F;
        double W = Math.sqrt(1 - Math.pow(e, 2) * Math.pow(Math.sin(avgLat), 2));
        
        //�q�ߐ��ȗ����a
        double M = (a * (1 - Math.pow(e, 2))) / Math.pow(W, 3);

        //�K�ѐ��ȗ����a
        double N = a / W;
        
        //2�_�Ԃ̈ܓx��(���W�A��)    
        double dLat = radLatStart - radLatEnd;
        
        //2�_�Ԃ̌o�x��(���W�A��)
        double dLon = radLonStart - radLonEnd;
        
        //2�_�Ԃ̋����i���[�g���j
        double d = Math.sqrt(Math.pow(M * dLat, 2) + Math.pow(N * Math.cos(avgLat) * dLon, 2));
        
        return d;
	}
}
