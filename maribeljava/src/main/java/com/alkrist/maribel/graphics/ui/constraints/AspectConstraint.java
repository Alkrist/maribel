package com.alkrist.maribel.graphics.ui.constraints;

import com.alkrist.maribel.graphics.context.GLContext;

/**
 * Creates a constraint that will set the ratio of HEIGHT based on WIDTH.
 * So, do not set this constraint for width!<br/>
 * Can be used only for scale.
 */
public class AspectConstraint implements ScaleConstraint{

	private float aspectRatio;
	
	/**
	 * Aspect Constraint constructor.
	 * @param aspectRatio - ratio of WIDTH to HEIGHT. (0.5) will result in HEIGHT be 50% of width.
	 * (mind also the type of WIDTH constraint, if it is relative, ratio can look weird based on the screen resolution,
	 * if it is pixels, the ratio will be strict.)
	 */
	public AspectConstraint(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
	
	@Override
	public float getRelativeValue(int sideLength, float xScale) {
		
		float windowAspectRatio = (float) GLContext.getWindow().getWidth() / (float) sideLength;
		return xScale * windowAspectRatio * aspectRatio;
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
