package com.example.imageblur;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

@SuppressLint("NewApi")
public class ImageBlurRender implements GLSurfaceView.Renderer{

	public ImageBlurRender(Context context) {
		this.context = context;
	}
	private Context context;
	private TextureRenderer mTexRenderer = new TextureRenderer();
	private int[] mTextures = new int[2];
	private EffectContext mEffectContext;
	private Effect mEffect;
	private int mImageWidth;
	private int mImageHeight;
	private boolean mInitialized = false;
	
	public int mCurrentEffect = 1;

	@Override
	public void onDrawFrame(GL10 arg0) {
		//if (!mInitialized) {
			// Only need to do this once
			mEffectContext = EffectContext.createWithCurrentGlContext();
			mTexRenderer.init();
			loadTextures();
			mInitialized = true;
		//}

		// if an effect is chosen initialize it and apply it to the texture
		initEffect();
		applyEffect();
		renderResult();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		if (mTexRenderer != null) {
			mTexRenderer.updateViewSize(width, height);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
	}

	private void loadTextures() {
		// Generate textures
		GLES20.glGenTextures(2, mTextures, 0);

		// Load input bitmap
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.wallpaper_select_beauty);
		mImageWidth = bitmap.getWidth();
		mImageHeight = bitmap.getHeight();
		mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

		// Set texture parameters
		GLToolbox.initTexParams();
	}

	private void initEffect() {
		 EffectFactory effectFactory = mEffectContext.getFactory();
	        if (mEffect != null) {
	            mEffect.release();
	        }
	        Log.i("jiangtao", "mCurrentEffect is : " + mCurrentEffect);
	        /**
	         * Initialize the correct effect based on the selected menu/action item
	         */
	        switch (mCurrentEffect) {

	            case 0:
	                break;

	            case 1:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_AUTOFIX);
	                mEffect.setParameter("scale", 0.5f);
	                break;

	            case 2:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_BLACKWHITE);
	                mEffect.setParameter("black", .1f);
	                mEffect.setParameter("white", .7f);
	                break;

	            case 3:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_BRIGHTNESS);
	                mEffect.setParameter("brightness", 2.0f);
	                break;

	            case 4:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_CONTRAST);
	                mEffect.setParameter("contrast", 1.4f);
	                break;

	            case 5:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_CROSSPROCESS);
	                break;

	            case 6:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_DOCUMENTARY);
	                break;

	            case 7:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_DUOTONE);
	                mEffect.setParameter("first_color", Color.YELLOW);
	                mEffect.setParameter("second_color", Color.DKGRAY);
	                break;

	            case 8:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_FILLLIGHT);
	                mEffect.setParameter("strength", .8f);
	                break;

	            case 9:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_FISHEYE);
	                mEffect.setParameter("scale", .5f);
	                break;

	            case 10:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_FLIP);
	                mEffect.setParameter("vertical", true);
	                break;

	            case 11:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_FLIP);
	                mEffect.setParameter("horizontal", true);
	                break;

	            case 12:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_GRAIN);
	                mEffect.setParameter("strength", 1.0f);
	                break;

	            case 13:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_GRAYSCALE);
	                break;

	            case 14:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_LOMOISH);
	                break;

	            case 15:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_NEGATIVE);
	                break;

	            case 16:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_POSTERIZE);
	                break;

	            case 17:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_ROTATE);
	                mEffect.setParameter("angle", 180);
	                break;

	            case 18:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_SATURATE);
	                mEffect.setParameter("scale", .5f);
	                break;

	            case 19:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_SEPIA);
	                break;

	            case 20:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_SHARPEN);
	                break;

	            case 21:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_TEMPERATURE);
	                mEffect.setParameter("scale", .9f);
	                break;

	            case 22:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_TINT);
	                mEffect.setParameter("tint", Color.MAGENTA);
	                break;

	            case 23:
	                mEffect = effectFactory.createEffect(
	                        EffectFactory.EFFECT_VIGNETTE);
	                mEffect.setParameter("scale", .5f);
	                break;

	            default:
	                break;

	        }
	}

	private void applyEffect() {
		mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
	}

	private void renderResult() {
		mTexRenderer.renderTexture(mTextures[1]);
	}
}
