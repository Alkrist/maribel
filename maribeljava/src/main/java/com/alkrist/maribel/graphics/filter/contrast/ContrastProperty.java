package com.alkrist.maribel.graphics.filter.contrast;

import org.joml.Vector3f;

import com.alkrist.maribel.graphics.filter.PPEProperty;

public class ContrastProperty implements PPEProperty{

	private Vector3f contrastFactor;
	private Vector3f brightnessFactor;
	
	public ContrastProperty(Vector3f contrastFactor, Vector3f brightnessFactor) {
		this.contrastFactor = contrastFactor;
		this.brightnessFactor = brightnessFactor;
	}
	
	public Vector3f getContrastFactor() {
		return contrastFactor;
	}
	
	public Vector3f getBrightnessFactor() {
		return brightnessFactor;
	}
}
