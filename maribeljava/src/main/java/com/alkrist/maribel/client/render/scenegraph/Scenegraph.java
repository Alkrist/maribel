package com.alkrist.maribel.client.render.scenegraph;

import com.alkrist.maribel.client.math.Transform;

public class Scenegraph extends Node{

	private Node root;
	private Node transparentObjects;
	
	public Scenegraph() {
		setWorldTransform(new Transform());
		this.root = new Node();
		this.transparentObjects = new Node();
		
		root.setParent(this);
		transparentObjects.setParent(this);
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
	
	public void addTransparentObject(Node object) {
		transparentObjects.addChild(object);
	}
	
	public void record(RenderList renderList){
		root.record(renderList);
	}
	
	public void recordTransparentObjects(RenderList renderList) {
		transparentObjects.record(renderList);
	}
}
