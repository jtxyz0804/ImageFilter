package com.example.imageblur;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private ImageBlurGLSurfaceView imageBlurGLSurfaceView;
	//private Gallery gallery;
	private Bitmap bitmap;
	private GalleryImageAdapter galleryImageAdapter;
	private ImageBlurRender imageBlurRender;
	private List<Bitmap> bitmapPreview = new ArrayList<Bitmap>();
	private PixelBuffer pixelBuffer;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		imageBlurRender = new ImageBlurRender(MainActivity.this);
		bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wallpaper_select_beauty);
		imageBlurGLSurfaceView.setImageSource(bitmap);
		
//		progressDialog = new ProgressDialog(MainActivity.this, R.style.progress_dialog_stule);
//		progressDialog.setMessage("Loading...");
//		
//		progressDialog.show();		
//		new BlurImageTask().execute(bitmap);
		//move image blur to ImageAdapter
		setImageBlurSource();
		
	}
	
	public void setImageBlurSource(){
		//imageBlurGLSurfaceView.setVisibility(View.VISIBLE);
		galleryImageAdapter = new GalleryImageAdapter(MainActivity.this, bitmap);
		galleryImageAdapter.setBlurPreviewImage(bitmapPreview);
		Gallery gallery = (Gallery)findViewById(R.id.image_blur_preview);
		gallery.setSelection(5);
		gallery.setAnimationDuration(3000);
		imageBlurGLSurfaceView.setEffectAdapter(galleryImageAdapter);
		gallery.setAdapter(galleryImageAdapter);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				galleryImageAdapter.setSelectedItem(position);
				imageBlurGLSurfaceView.setCurrentEffectId(position);
				imageBlurGLSurfaceView.requestRender();
			}
		});
	}
	
	
    @Override
    protected void onResume() {
    	imageBlurGLSurfaceView.onResume();
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	imageBlurGLSurfaceView.onPause();
    }
    
	public void findView(){
		imageBlurGLSurfaceView = (ImageBlurGLSurfaceView) findViewById(R.id.image_blur);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class BlurImageTask extends AsyncTask<Bitmap, Void, Void>{

		@Override
		protected Void doInBackground(Bitmap... param) {
			//后台数据一步处理完成，修改为在adpter中异步处理每一张图片？？？
			Bitmap bitmap = param[0];
			bitmapPreview.add(bitmap);
			for (int i = 1; i < ImageBlurUtils.getEffectName().size(); i++) {
				pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
				pixelBuffer.setRenderer(imageBlurRender);	
				imageBlurRender.mCurrentEffect = i;
				bitmapPreview.add(pixelBuffer.getBitmap(null, null));
			}
			
			Log.i("jiangtao4", "bitmapPreview size is : " + bitmapPreview.size());
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.i("jiangtao4", "onPostExecute");
			progressDialog.dismiss();
			
			setImageBlurSource();
		}
	}

}
