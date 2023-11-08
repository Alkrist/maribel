package com.alkrist.maribel.graphics.ui.constraints;

/**
 * Creates the constraint of specified percents of the screen
 * 
 * @author Alkrist
 */
public class RelativeConstraint implements PositionConstraint, ScaleConstraint{

	private boolean isNegativeCoord = false;
	private boolean isMargin = false;
	
	private float percents;
	
	
	public RelativeConstraint(float percents) {
		this.percents = Math.max(percents, -1);
		this.percents = Math.min(this.percents, 1);
	}
	
	@Override
	public float getRelativeValue(int sideLength, float xScale, boolean isPosition) {
		return percents;
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
		return percents;
	}

	@Override
	public void setValue(float value) {
		value = Math.max(value, -1);
		value = Math.min(1, value);
		this.percents = value;
	}

}