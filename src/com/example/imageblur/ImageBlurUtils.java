package com.example.imageblur;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;

@SuppressLint("NewApi")
public class ImageBlurUtils {

	public ImageBlurUtils() {
	}

	public static ArrayList<String> getEffectName(){
		ArrayList<String> effectArray = new ArrayList<String>();
		effectArray.add("none");
		effectArray.add("autofix");
		effectArray.add("bw");
		effectArray.add("brightness");
		effectArray.add("contrast");
		effectArray.add("crossprocess");
		effectArray.add("documentary");
		effectArray.add("duotone");
		effectArray.add("filllight");
		effectArray.add("fisheye");
		effectArray.add("flipvert");
		effectArray.add("fliphor");
		effectArray.add("grain");
		effectArray.add("grayscale");
		effectArray.add("lomoish");
		effectArray.add("negative");
		effectArray.add("posterize");
		effectArray.add("rotate");
		effectArray.add("saturate");
		effectArray.add("sepia");
		effectArray.add("sharpen");
		effectArray.add("temperature");
		effectArray.add("tint");
		effectArray.add("vignette");
		
		return effectArray;
	}
	
	
	public static Effect createEffect(int mCurrentEffect, EffectContext effectContext) {

		EffectFactory effectFactory = effectContext.getFactory();
		Effect effect = null;
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
}
