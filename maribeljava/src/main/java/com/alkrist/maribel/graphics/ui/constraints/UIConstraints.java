package com.alkrist.maribel.graphics.ui.constraints;

import org.joml.Vector2f;

import com.alkrist.maribel.graphics.context.GLContext;

public class UIConstraints {

	
	private PositionConstraint xConstraint;
	private PositionConstraint yConstraint;
	private ScaleConstraint widthConstraint;
	private ScaleConstraint heightConstraint;
	
	private Vector2f position;
	private Vector2f scale;
	
	public UIConstraints() {
		this.position = new Vector2f(0);
		this.scale = new Vector2f(1);
	}
	
	public UIConstraints setX(MarginHorizontal margin, PositionConstraint constraint) {
		
		if(margin == MarginHorizontal.LEFT) {
			constraint.setToNegativeCoord();
		}
		
		constraint.setMargin();
		
		this.xConstraint = constraint;
		
		return this;
	}
	
	public UIConstraints setX(PositionConstraint constraint) {
		this.xConstraint = constraint;
		return this;
	}
	
	public UIConstraints setY(MarginVertical margin, PositionConstraint constraint) {
		
		if(margin == MarginVertical.BOTTOM) {
			constraint.setToNegativeCoord();
		}
		
		constraint.setMargin();
		
		this.yConstraint = constraint;
		return this;
	}
	
	public UIConstraints setY(PositionConstraint constraint) {
		this.yConstraint = constraint;
		return this;
	}
	
	public UIConstraints setWidth(ScaleConstraint constraint) {
		this.widthConstraint = constraint;
		return this;
	}
	
	public UIConstraints setHeight(ScaleConstraint constraint) {
		this.heightConstraint = constraint;
		return this;
	}
	
	public Vector2f getPosition() {
		
		float offsetX = xConstraint.getRelativeValue(GLContext.getConfig().width, scale.x, true);
		float offsetY = yConstraint.getRelativeValue(GLContext.getConfig().height, scale.x, true);
		
		if(xConstraint.isMargin()) {
			position.x = xConstraint.isMarginNegative() ? (-1 + offsetX + scale.x) : (1 - offsetX - scale.x);
		}else {
			position.x = (0 + offsetX);
		}
		
		if(yConstraint.isMargin()) {
			position.y = yConstraint.isMarginNegative() ? (-1 + offsetY + scale.y) : (1 - offsetY - scale.y);
		}else {
			position.y = (0 + offsetY);
		}
		
		return position;
	}
	
	public Vector2f getScale() {
		scale.x = widthConstraint.getRelativeValue(GLContext.getConfig().width, scale.x, false);
		scale.y = heightConstraint.getRelativeValue(GLContext.getConfig().height, scale.x, false);
		
		return scale;
	}
	
	public PositionConstraint getXConstraint() {
		return xConstraint;
	}
	
	public PositionConstraint getYConstraint() {
		return yConstraint;
	}
	
	public ScaleConstraint getWidthConstraint() {
		return widthConstraint;
	}
	
	public ScaleConstraint getHeightConstraint() {
		return heightConstraint;
	}
	
	public static enum MarginHorizontal{
		LEFT,
		RIGHT
	}
	
	public static enum MarginVertical{
		TOP,
		BOTTOM
	}
}
