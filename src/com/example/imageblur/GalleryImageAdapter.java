package com.example.imageblur;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
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
	private Bitmap bitmap;
	
	public Effect effect;
	private int selectedItem = -1;
	private List<Bitmap> previewList;
	
	public GalleryImageAdapter(Context context, Bitmap imageBitmap) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.bitmap = imageBitmap;
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
		conteView = (LinearLayout)inflater.inflate(R.layout.gallery_image_layout, null);
		RoundedImageView imageView = (RoundedImageView) conteView.findViewById(R.id.gallery_image_preview_item);
		imageView.setImageBitmap(previewList.get(position));
		TextView textView = (TextView)conteView.findViewById(R.id.gallery_text);
		textView.setText(ImageBlurUtils.getEffectName().get(position));
		if (selectedItem == position){
			//selected item will show different
			imageView.setBorderColor(Color.parseColor("#f1c40f"));
			textView.setTextColor(Color.parseColor("#f1c40f"));
			ObjectAnimator bAnimatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.1f);
			ObjectAnimator bAnimatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.1f);
			bAnimatorX.setDuration(500);
			bAnimatorY.setDuration(500);
			AnimatorSet animationSet = new AnimatorSet();
			animationSet.play(bAnimatorX).with(bAnimatorY);
			animationSet.start();
		}else {
			textView.setTextColor(Color.parseColor("#000000"));
			imageView.setBorderColor(Color.TRANSPARENT);
		}
		return conteView;
	}

}
