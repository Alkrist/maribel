package com.alkrist.maribel.graphics.ui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.alkrist.maribel.graphics.ui.UIConstraint.ConstraintType;
import com.alkrist.maribel.graphics.ui.UIConstraint.OffsetFrom;


public abstract class UIElement {
	
	private List<UIElement> children;
	private UIElement parent;
	
	protected Vector2f position;
	protected UIConstraint posXconstraint;
	protected UIConstraint posYconstraint;
	
	public UIElement() {
		this.parent = null;
		this.children = null;
	}
	
	public void addChild(UIElement child) {
		if(children == null) {
			children = new ArrayList<UIElement>();
		}
		children.add(child);
		child.setParent(this);
	}
	
	private void setParent(UIElement parent) {
		this.parent = parent;
	}
	
	public void removeChild(UIElement child) {
		if(children != null) {
			child.setParent(null);
			children.remove(child);
		}
	}
	
	public List<UIElement> getChildren(){
		return children;
	}
	
	public UIElement getParent() {
		return parent;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setXConstraint(UIConstraint constraint) {
		this.posXconstraint = constraint;
	}
	
	public void setYConstraint(UIConstraint constraint) {
		this.posYconstraint = constraint;
	}
	
	public void render() {
		renderInternal();
		if(children != null) {
			children.forEach(child -> child.render());
		}
	}
	
	public void update(double deltaTime) {
		updateInternal(deltaTime);
		if(children != null) {
			children.forEach(child -> child.update(deltaTime));
		}
		
	}
	
	protected abstract void updateInternal(double deltaTime);
	protected abstract void renderInternal();
}
