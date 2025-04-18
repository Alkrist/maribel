package com.alkrist.maribel.client.scenegraph;

import com.alkrist.maribel.client.math.Transform;

public abstract class NodeComponent {

	private Renderable parent;
	
	public void update(){};
	
	public void render(){};
	
	public void cleanup(){};
	
	public Renderable getParent() {
		return parent;
	}

	public void setParent(Renderable parent) {
		this.parent = parent;
	}

	public Transform getTransform(){
		return parent.getWorldTransform();
	}
}
