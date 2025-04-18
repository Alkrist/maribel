package com.alkrist.maribel.client.scenegraph;

import com.alkrist.maribel.client.math.Transform;

public class Scenegraph extends Node{

	private Node root;
	
	public Scenegraph() {
		setWorldTransform(new Transform());
		this.root = new Node();
		
		root.setParent(this);
	}
	
	public void render() {
		root.render();
	}
	
	public void update() {
		root.update();
	}
	
	public void cleanup() {
		root.cleanup();
	}
	
	public void addObject(Node object) {
		root.addChild(object);
	}
	
	public void record(RenderList renderList){
		root.record(renderList);
	}
}
