package com.alkrist.maribel.client.graphics;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

public class Light implements Component{

	public Vector3f position;
	public Vector3f color;
	public Vector3f attenuation = new Vector3f(1,0,0);
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public boolean isPointLight() {
		return attenuation != null;
	}
}
