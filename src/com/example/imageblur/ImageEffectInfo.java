package com.example.imageblur;

import android.media.effect.Effect;

public class ImageEffectInfo {

	private String effectName;
	private Effect effect;
	
	public ImageEffectInfo(String name, Effect effect) {
		this.effectName = name;
		this.effect = effect;
	}

	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	
	

}
