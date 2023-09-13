package com.alkrist.maribel.graphics.ui;

import com.alkrist.maribel.graphics.context.GLContext;

public class UIConstraint {

	private float value;
	private ConstraintType type;
	private OffsetFrom side;
	
	public UIConstraint(ConstraintType type) {
		if(type != ConstraintType.CENTER) {
			throw new IllegalArgumentException("Only center constraint applies with zero args!");
		}
		
		this.type = type;
		this.value = 0;
	}
	
	public UIConstraint(ConstraintType type, float arg) {
		if(type == ConstraintType.CENTER) {
			throw new IllegalArgumentException("Center constraint applies with zero args!");
		}
		
		this.type = type;
		this.value = arg;
	}
	
	public UIConstraint(ConstraintType type, OffsetFrom side, float arg) {
		this(type, arg);
		
		this.side = side;
	}
	
	public float getValue() {
		return value;
	}
	
	public ConstraintType getType() {
		return type;
	}
	
	public OffsetFrom getOffsetSide() {
		return side;
	}
	
	public boolean hasOffsetSide() {
		return side != null;
	}
	
	public enum ConstraintType {
		PIXEL,
		RELATIVE,
		CENTER,
		ASPECT
	}
	
	public enum OffsetFrom {
		LEFT,
		RIGHT,
		BOTTOM,
		TOP
	}
}
