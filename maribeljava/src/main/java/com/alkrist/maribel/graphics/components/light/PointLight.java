package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

public class PointLight implements Component{

	private Vector3f position;
	private Vector3f color;
	private float radius;
	private float intensity = 1f;	
	private float attenuationFactor = 0.5f;
	
	
	public PointLight(Vector3f position, Vector3f color, float radius) {
		this.position = position;
		this.color = color;
		this.radius = radius;
	}
	
	public PointLight(Vector3f position, Vector3f color, float radius, float intensity) {
		this(position, color, radius);
		
		this.intensity = intensity;
	}
	
	public PointLight(Vector3f position, Vector3f color, float radius, float intensity, float attenuationFactor) {
		this(position, color, radius, intensity);
		
		this.attenuationFactor = attenuationFactor;
	}
	
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b) {
		color.x = r;
		color.y = g;
		color.z = b;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public void setAttenuationFactor(float attFactor) {
		this.attenuationFactor = attFactor;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

	public float getRadius() {
		return radius;
	}

	public float getIntensity() {
		return intensity;
	}

	public float getAttenuationFactor() {
		return attenuationFactor;
	}
}
