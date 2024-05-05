package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

public abstract class Light implements Component{

	protected Vector3f position;
	protected Vector3f color;
	protected float intensity;
	
	public Light(Vector3f position, Vector3f color, float intensity) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getIntensity() {
		return intensity;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b) {
		this.color.set(r, g, b);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public float getDistanceToLight(Vector3f cameraPosition) {
		return (position.distance(cameraPosition));
	}
}
