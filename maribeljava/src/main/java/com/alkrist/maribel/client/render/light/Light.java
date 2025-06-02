package com.alkrist.maribel.client.render.light;

import org.joml.Vector3f;

import com.alkrist.maribel.client.render.scenegraph.Node;

public class Light extends Node{

	protected Vector3f color;
	protected float intensity;
	
	public Light(Vector3f position, Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
		getLocalTransform().setTranslation(position);
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public float getIntensity() {
		return intensity;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
