package jp.co.deliv.android.imasugu.sugucafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * QuickSearch�V���[�Y�F�����J�t�F�̃o�[�W�������\���r���[
 * @author DLV4002
 * @author 120007HTT
 */
public class SuguCafeAboutView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutview);
		
		// Manifest�t�@�C���ɋL�q����Ă���o�[�W���������擾����
		String packageName = getPackageName();
		PackageInfo packageInfo = null;
		String version;
		try {
			packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			version = "-----";
			
		}
		
		// versionName���e�L�X�g�r���[�ɕ\��
		TextView versionName = (TextView)findViewById(R.id.versionName);
		versionName.setText(version);
		
		/*
		 * ����Ȃу��S���N���b�N����ƁA�u���E�U���N�����Ă���Ȃт̃z�[���y�[�W�Ɉړ�����
		 */
		ImageView gnaviLogo = (ImageView)findViewById(R.id.gnavi_logo);
		gnaviLogo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Resources res = SuguCafeAboutView.this.getResources();
				Uri uri = Uri.parse(res.getString(R.string.web_service_url));
				Intent i = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(i);				
			}
		});
		
		Button btn_linkSupport = (Button)findViewById(R.id.btn_link_support);
		btn_linkSupport.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Resources res = SuguCafeAboutView.this.getResources();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse(res.getString(R.string.support_url)));
		        startActivity(intent);

			}
		});
	}

}
