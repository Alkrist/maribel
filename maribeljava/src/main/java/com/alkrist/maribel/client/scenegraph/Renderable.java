package com.alkrist.maribel.client.scenegraph;

import java.util.HashMap;
import java.util.Map;

public class Renderable extends Node{

	private HashMap<String, NodeComponent> components;
	protected boolean render;
	
	public Renderable(){
		super();
		
		render = true;
		components = new HashMap<String, NodeComponent>();
	}
	
	public void addComponent(String type, NodeComponent component){
		component.setParent(this);
		components.put(type, component);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getComponent(String type) {
		return (T) this.components.get(type);
	}
	
	public Map<String, NodeComponent> getComponents() {
		return components;
	}
	
	public void cleanup(){
		
		components.values().forEach(component -> component.cleanup());
		
		super.cleanup();
	}
	
	public void record(RenderList renderList){

		if (render){
			if (!renderList.contains(id)){
				renderList.add(this);
				renderList.setChanged(true);
			}
		}
		else {
			if (renderList.contains(id)){
				renderList.remove(this);
				renderList.setChanged(true);
			}
		}
		
		super.record(renderList);
	}
}
