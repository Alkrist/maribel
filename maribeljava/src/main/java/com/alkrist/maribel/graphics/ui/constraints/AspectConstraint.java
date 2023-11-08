package com.alkrist.maribel.graphics.ui.constraints;

public class AspectConstraint implements ScaleConstraint{

	private float aspectRatio;
	
	
	public AspectConstraint(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
	
	@Override
	public float getRelativeValue(int sideLength, float xScale, boolean isPosition) {
		return xScale * aspectRatio;
	}

	@Override
	public float getValue() {
		return aspectRatio;
	}

	@Override
	public void setValue(float value) {
		this.aspectRatio = value;
	}

}
