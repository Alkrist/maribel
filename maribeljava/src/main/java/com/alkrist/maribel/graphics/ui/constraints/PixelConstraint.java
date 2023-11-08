package com.alkrist.maribel.graphics.ui.constraints;

public class PixelConstraint implements PositionConstraint, ScaleConstraint{

	private boolean isNegativeCoord = false;
	private boolean isMargin = false;
	
	private float pixels;
	
	
	public PixelConstraint(int pixels) {
		this.pixels = pixels;
	}
	
	@Override
	public float getRelativeValue(int sideLength, float xScale, boolean isPosition) {	
		return isPosition ? pixels / (sideLength / 2) : pixels / sideLength;
	}

	@Override
	public void setToNegativeCoord() {
		this.isNegativeCoord = true;
	}

	@Override
	public void setMargin() {
		this.isMargin = true;
	}

	@Override
	public boolean isMargin() {
		return isMargin;
	}

	@Override
	public boolean isMarginNegative() {
		return isNegativeCoord;
	}

	@Override
	public float getValue() {
		return pixels;
	}

	@Override
	public void setValue(float value) {
		this.pixels = Math.round(value);
	}

}
