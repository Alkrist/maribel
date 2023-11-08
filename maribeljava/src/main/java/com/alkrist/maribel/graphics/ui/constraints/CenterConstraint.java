package com.alkrist.maribel.graphics.ui.constraints;

public class CenterConstraint implements PositionConstraint{

	public CenterConstraint() {}
	
	@Override
	public float getRelativeValue(int sideLength, float xScale, boolean isPositon) {
		return 0;
	}

	@Override
	public void setToNegativeCoord() {}

	@Override
	public void setMargin() {}

	@Override
	public boolean isMargin() {
		return false;
	}

	@Override
	public boolean isMarginNegative() {
		return false;
	}

	@Override
	public float getValue() {
		return 0;
	}

	@Override
	public void setValue(float value) {}
	
	
}
