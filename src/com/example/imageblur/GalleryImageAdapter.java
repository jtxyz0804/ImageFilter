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
import android.media.effect.EffectFactory;
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
	private EffectContext effectContext;
	private int selectedItem = -1;
	private List<Bitmap> previewList;
	
	public GalleryImageAdapter(Context context, Bitmap imageBitmap, EffectContext effectcontect) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.bitmap = imageBitmap;
		this.effectContext = effectcontect;
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
	
	public Effect createEffect(int currentfilterID,
			EffectContext mEffectContext2) {
		// TODO Auto-generated method stub
		effectContext = mEffectContext2;
		return effect = createEffect(currentfilterID);
	}

	public Effect createEffect(int mCurrentEffect) {

		EffectFactory effectFactory = null;
		if (effectContext == null) {
			try {
				effectContext = EffectContext.createWithCurrentGlContext();
				if (effect != null) {
					effect.release();
				}
			} catch (Exception e) {
				Log.e("EffectException", "create effect exception");
				e.printStackTrace();
			}
		}
		effectFactory = effectContext.getFactory();
		// Initialize the correct effect based on the selected menu/action item
		if (effectFactory == null) {
			return null;
		}
		switch (mCurrentEffect) {

		case 0:
			break;

		case 1:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
			effect.setParameter("scale", 0.5f);
			break;

		case 2:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_BLACKWHITE);
			effect.setParameter("black", .1f);
			effect.setParameter("white", .7f);
			break;

		case 3:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
			effect.setParameter("brightness", 2.0f);
			break;

		case 4:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
			effect.setParameter("contrast", 1.4f);
			break;

		case 5:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
			break;

		case 6:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
			break;

		case 7:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
			effect.setParameter("first_color", Color.YELLOW);
			effect.setParameter("second_color", Color.DKGRAY);
			break;

		case 8:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_FILLLIGHT);
			effect.setParameter("strength", .8f);
			break;

		case 9:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
			effect.setParameter("scale", .5f);
			break;

		case 10:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			effect.setParameter("vertical", true);
			break;

		case 11:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			effect.setParameter("horizontal", true);
			break;

		case 12:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
			effect.setParameter("strength", 1.0f);
			break;

		case 13:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_GRAYSCALE);
			break;

		case 14:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
			break;

		case 15:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
			break;

		case 16:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_POSTERIZE);
			break;

		case 17:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
			effect.setParameter("angle", 180);
			break;

		case 18:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
			effect.setParameter("scale", .5f);
			break;

		case 19:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
			break;

		case 20:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
			break;

		case 21:
			effect = effectFactory
					.createEffect(EffectFactory.EFFECT_TEMPERATURE);
			effect.setParameter("scale", .9f);
			break;

		case 22:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
			effect.setParameter("tint", Color.MAGENTA);
			break;

		case 23:
			effect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
			effect.setParameter("scale", .5f);
			break;

		default:
			break;
		}
		return effect;
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
