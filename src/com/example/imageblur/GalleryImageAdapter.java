package com.example.imageblur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.R.string;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.imageblur.roundimage.RoundedImageView;

@SuppressLint("NewApi")
public class GalleryImageAdapter extends BaseAdapter {

	private List<String> effectArray = new ArrayList<String>();
	private Context context;
	private LayoutInflater inflater;
	private Bitmap srcBitmap;
	
	public Effect effect;
	private int selectedItem = -1;
	private List<Bitmap> previewList;
	private Map<String, Bitmap> previewImageMap = new HashMap<String, Bitmap>();
	private ImageBlurRender imageBlurRender;
	private PixelBuffer pixelBuffer;
	
	public GalleryImageAdapter(Context context, Bitmap imageBitmap) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.srcBitmap = imageBitmap;
		imageBlurRender = new ImageBlurRender(context);
	}

	@Override
	public int getCount() {
		return ImageBlurUtils.getEffectName().size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedItem(int selectItemid){
		Log.i("jiangtao4", "in setSelectItem");
		if (this.selectedItem != selectItemid) {
			this.selectedItem = selectItemid;
			notifyDataSetChanged();
		}
	}
	
	public void setBlurPreviewImage(List<Bitmap> list){
		previewList = list;
	}
	
	@Override
	public View getView(int position, View conteView, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if (conteView == null) {
			conteView = (LinearLayout)inflater.inflate(R.layout.gallery_image_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.roundedImageView = (RoundedImageView) conteView.findViewById(R.id.gallery_image_preview_item);
			viewHolder.textView = (TextView)conteView.findViewById(R.id.gallery_text);
			conteView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)conteView.getTag();
		}
		
	//	viewHolder.roundedImageView.setImageBitmap(previewList.get(position));
		String effectName = ImageBlurUtils.getEffectName().get(position);
		Bitmap preBitmap = previewImageMap.get(effectName);
		if (preBitmap == null) {
			if (position == 0) {
				viewHolder.roundedImageView.setImageBitmap(srcBitmap);
			}else {
				viewHolder.roundedImageView.setBackground(context.getResources().getDrawable(R.drawable.nine_patch_icn));
				viewHolder.roundedImageView.setTag(effectName);
				
				//start asynctask blurimage
				
				new BlurImagesTask(position, effectName, viewHolder.roundedImageView).execute(srcBitmap);
				
			}
		}else {
			viewHolder.roundedImageView.setImageBitmap(preBitmap);
		}
		viewHolder.textView.setText(ImageBlurUtils.getEffectName().get(position));
		
		if (selectedItem == position){
			//selected item will show different
			viewHolder.roundedImageView.setBorderColor(Color.parseColor("#f1c40f"));
			viewHolder.textView.setTextColor(Color.parseColor("#f1c40f"));
			ObjectAnimator bAnimatorX = ObjectAnimator.ofFloat(viewHolder.roundedImageView, "scaleX", 1.1f);
			ObjectAnimator bAnimatorY = ObjectAnimator.ofFloat(viewHolder.roundedImageView, "scaleY", 1.1f);
			bAnimatorX.setDuration(500);
			bAnimatorY.setDuration(500);
			AnimatorSet animationSet = new AnimatorSet();
			animationSet.play(bAnimatorX).with(bAnimatorY);
			animationSet.start();
		}else {
			viewHolder.textView.setTextColor(Color.parseColor("#000000"));
			viewHolder.roundedImageView.setBorderColor(Color.TRANSPARENT);
		}
		return conteView;
	}
	
	static class ViewHolder{
		public RoundedImageView roundedImageView;
		public TextView textView;
	}
	
	public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap>{

		private int position;
		private String strEffectName;
		private RoundedImageView roundedImageView;
		
		public BlurImagesTask(int pos, String name, RoundedImageView imageView){
			this.position = pos;
			this.strEffectName = name;
			this.roundedImageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(Bitmap... param) {
			Bitmap bitmap = param[0];
			pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
			pixelBuffer.setRenderer(imageBlurRender);	
			imageBlurRender.mCurrentEffect = position;
		    
			return  pixelBuffer.getBitmap(null, null);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (((String)roundedImageView.getTag()).equals(strEffectName)) {
				roundedImageView.setImageBitmap(result);
			}
			previewImageMap.put(strEffectName, result);
		}
		
	}

}
