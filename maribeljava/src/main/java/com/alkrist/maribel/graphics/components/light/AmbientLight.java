package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

public class AmbientLight implements Component{

	private Vector3f color;
	private float intensity;
	
	public AmbientLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	public AmbientLight(float r, float g, float b, float intensity) {
		this.color = new Vector3f(r,g,b);
		this.intensity = intensity;
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
	
	public Vector3f getColor() {
		return color;
	}
	
	public float getIntensity() {
		return intensity;
	}
}
