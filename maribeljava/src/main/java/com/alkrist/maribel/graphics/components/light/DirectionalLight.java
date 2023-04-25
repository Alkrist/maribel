package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

public class DirectionalLight extends Light {

	public DirectionalLight(Vector3f direction, Vector3f color, float intensity) {
		super(direction, color, intensity);
	}
}
