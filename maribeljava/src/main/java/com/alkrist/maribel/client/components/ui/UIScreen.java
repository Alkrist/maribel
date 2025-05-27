package com.alkrist.maribel.client.components.ui;

import java.util.ArrayList;
import java.util.List;

import com.alkrist.maribel.client.scenegraph.RenderList;

public class UIScreen {

	private List<UIElement> elements = new ArrayList<UIElement>();
	
	
	public List<UIElement> getElements(){
		return elements;
	}
	
	public void setElements(List<UIElement> elements) {
		this.elements = elements;
	}
	
	public void render(){
		elements.forEach(element -> element.render());
	}
	
	public void update(){
		elements.forEach(element -> element.update());
	}
	
	public void record(RenderList renderList){
		elements.forEach(element -> element.record(renderList));
	}
	
	public void cleanup(){
		elements.forEach(element -> element.cleanup());
	}
}
