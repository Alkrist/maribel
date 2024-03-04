package com.alkrist.maribel.graphics.ui.constraints;

public interface PositionConstraint {

	public float getRelativeValue(int sideLength, float xScale);
	
	public void setToNegativeCoord();
	
	public void setMargin();
	
	public boolean isMargin();
	
	public boolean isMarginNegative();
	
	public float getValue();
	
	public void setValue(float value);
}
