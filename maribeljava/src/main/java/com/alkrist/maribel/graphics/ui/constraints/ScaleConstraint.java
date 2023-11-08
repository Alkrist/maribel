package com.alkrist.maribel.graphics.ui.constraints;

public interface ScaleConstraint {

	public float getRelativeValue(int sideLength, float xScale, boolean isPosition);
	
	public float getValue();
	
	public void setValue(float value);
}
