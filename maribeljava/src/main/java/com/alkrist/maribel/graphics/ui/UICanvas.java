package com.alkrist.maribel.graphics.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class UICanvas {

	protected List<UIElement> elements;
	
	public UICanvas() {
		elements = new ArrayList<UIElement>();
	}
	
	public void addUIElement(UIElement element) {
		elements.add(element);
	}
	
	public void clearUIElements() {
		elements.clear();
	}
	
	public List<UIElement> getUIElements(){
		return elements;
	}
	
	public boolean removeUIElement(UIElement element) {
		return elements.remove(element);
	}
	
	public abstract void render();
	
	public abstract void update(double deltaTime);
}
