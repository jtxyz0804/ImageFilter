package com.example.imageblur;

import java.util.ArrayList;

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
}
