package com.alkrist.maribel.client.graphics.environment;

import com.alkrist.maribel.common.ecs.Component;

public class FogEffect implements Component{

	public float density;
	public float gradient;
	
	public FogEffect() {
		this.density = 0f;
		this.gradient = 0f;
	}
	
	public FogEffect(float density, float gradient) {
		density = Math.max(density, 0f);
		density = Math.min(density, 1f);
		gradient = Math.max(gradient, 0f);
		this.density = density;
		this.gradient = gradient;
	}
}
