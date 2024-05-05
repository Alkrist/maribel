package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

public class DirectionLight extends Light{

	private static DirectionLight instance = null;
	
	public static DirectionLight getInstance() {
		if(instance == null) {
			instance = new DirectionLight(new Vector3f(0), new Vector3f(0), 0);
		}
		
		return instance;
	}
	
	private DirectionLight(Vector3f direction, Vector3f color, float intensity) {
		super(direction, color, intensity);
	}
}
