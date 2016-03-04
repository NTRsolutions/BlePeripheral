package jp.co.deliv.android.http;

import jp.co.deliv.android.cache.ImageMemoryCache;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private static final String LOG_TAG = "ImageDownloadTask";
	
    // �A�C�R����\������r���[  
    private ImageView imageView = null;  
    
    private String url = null;
    
    // �C���[�W�̑���ɕ\���Ă����փr���[
    private View substitudeView = null;
    
    private boolean bResize = false;	// �摜�̃��T�C�Y�t���O
    private int width = 128;			// ���T�C�Y��̉摜�̕�
    private int height = 128;			// ���T�C�Y��̉摜�̍���

    /**
     * ���T�C�Y��C���[�W���̎擾
     * @return
     */
    public int getWidth() {
		return width;
	}

    /**
     * ���T�C�Y��C���[�W���̐ݒ�
     * @param width
     */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ���T�C�Y��C���[�W�����̎擾
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ���T�C�Y��C���[�W�����̐ݒ�
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	// �R���X�g���N�^(���T�C�Y�w��Ȃ�)  
    public ImageDownloadTask(ImageView imageView) {  
        this.imageView = imageView;
        this.bResize = false;
    }  

    // �R���X�g���N�^(���T�C�Y�w�肠��)  
    public ImageDownloadTask(ImageView imageView, boolean bResize) {  
        this.imageView = imageView;
        this.bResize = bResize;
    }  

    // �R���X�g���N�^(���T�C�Y�w�肠��)  
    public ImageDownloadTask(ImageView imageView, View subView, boolean bResize) {  
        this.imageView = imageView;
        this.substitudeView = subView;
        this.bResize = bResize;
    }
    
    
    @Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		/*
		 * ImageView�̑�փr���[���ݒ肳��Ă��Ƃ��A
		 * �C���[�W�̃_�E�����[�h�O�ɑ�փr���[��\������ImageView���������Ă���
		 */
		if(this.substitudeView != null) {
			this.substitudeView.setVisibility(View.VISIBLE);
			this.imageView.setVisibility(View.GONE);
		}
	}

    /**
     * �C���[�W�_�E�����[�h�^�X�N�̃f�t�H���g�摜���쐬����
     * @param width
     * @param height
     * @return
     */
    private Bitmap createDefaultImage(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.GRAY);
		return bitmap;
    }
    
	/**
     * �o�b�N�O���E���h�X���b�h�ɂ��p�����[�^�œn�����URL�̃C���[�W�摜���擾����
     */
    @Override  
    protected Bitmap doInBackground(String... urls) {
        
    	// �������󕶎���̏ꍇ�͉�������null��Ԃ�
        if("".equals(urls[0].trim())) {
        	return createDefaultImage(width, height);
        }
        
        url = urls[0];
        
        // �L���b�V������C���[�W���擾
        Bitmap image = ImageMemoryCache.getImage(urls[0]);
        if(image == null) {
        	// �L���b�V���Ƀq�b�g���Ȃ������ꍇ�AURL����摜���擾����
        	image = HttpClient.getImage(urls[0]);
        	
        	// �w�肷��URL����f�[�^���擾�ł��Ȃ������ꍇ�AColor.GRAY�̑�։摜���쐬����
        	if(image == null) {
        		image = createDefaultImage(width, height);
        	}
        	
        	// �������L���b�V���Ƀf�[�^��ۑ�
        	ImageMemoryCache.setImage(urls[0], image);
        }

        /*
         * ���T�C�Y�s�v�ȏꍇ�͂��̂܂܃C���[�W��ԋp����
         */
        if(bResize == false) {
        	return image;
        }
        
        /*
         * �摜���w��̑傫���Ƀ��T�C�Y����
         */
    	Bitmap resizeImage = null;
        if(image.getWidth() != width && image.getHeight() != height) {
        	int srcWidth = image.getWidth();
        	int srcHeight = image.getHeight();
        	
        	Log.d(LOG_TAG, "Src image width:" + srcWidth + " Src image height:" + srcHeight);
        	
        	Matrix scaleMat = new Matrix();
        	float scaleX = (float)width / (float)srcWidth;
        	float scaleY = (float)height / (float)srcHeight;
        	
        	Log.d(LOG_TAG, "Scale X:" + scaleX + " Scale Y:" + scaleY);
        	
        	scaleMat.postScale(scaleX, scaleY);
        	
        	// resize
        	resizeImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), scaleMat, true);
        	Log.d(LOG_TAG, "Resized image width:" + resizeImage.getWidth() + " Resized image height:" + resizeImage.getHeight());
        	
        } else {
        	resizeImage = image;
        }
        // �C���[�W��Ԃ�
        return resizeImage;  
    }  
  
    /**
     * ���C���X���b�h�Ŏ��s���鏈��  
     */
    @Override  
    protected void onPostExecute(Bitmap result) {
    	if(result != null && this.imageView.getTag().equals(url)) {
    		this.imageView.setImageBitmap(result);
    	} else {
    		// TODO: ���̂Ƃ��̏������ǂ����邩�B�B�B
    	}
    	
		/*
		 * ImageView�̑�փr���[���ݒ肳��Ă��Ƃ��A
		 * ��փr���[���������ă_�E�����[�h�����C���[�W���Z�b�g����ImageView��\������
		 */
		if(this.substitudeView != null) {
			this.substitudeView.setVisibility(View.GONE);
			this.imageView.setVisibility(View.VISIBLE);
		}
    }  
}
