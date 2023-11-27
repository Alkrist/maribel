package com.alkrist.maribel.graphics.ui;

import java.util.ArrayList;
import java.util.List;

import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;


/**
 * This is an abstract class that represents all the UI elements of the engine.
 * We consider a UI element the 2-dimensional quad that can have some properties to be rendered on.
 * <br/>
 * UI elements can be on-screen, those are rendered flat on top of the scene and in-game, which are rendered inside the 3D scene (think of texts).
 * @author Alkrist
 */
public abstract class UIElement {
	
	private List<UIElement> children;
	private UIElement parent;

	protected UIConstraints constraints;
	
	public UIElement(UIConstraints constraints) {
		this.constraints = constraints;
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
	
	public UIConstraints getConstraits() {
		return constraints;
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
