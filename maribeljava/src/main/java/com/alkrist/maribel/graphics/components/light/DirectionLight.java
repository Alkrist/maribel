package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

public class DirectionLight implements Component{

	private Vector3f direction;
	private Vector3f color;
	private float intensity;
	
	
	public DirectionLight(Vector3f direction, Vector3f color, float intensity) {
		this.direction = direction;
		this.color = color;
		this.intensity = intensity;
	}


	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
	public void setDirection(float x, float y, float z) {
		direction.x = x;
		direction.y = y;
		direction.z = z;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b) {
		color.x = r;
		color.y = g;
		color.z = b;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public Vector3f getDirection() {
		return direction;
	}


	public Vector3f getColor() {
		return color;
	}

	public float getIntensity() {
		return intensity;
	}	
}
