package com.alkrist.maribel.client.graphics.environment;

import com.alkrist.maribel.common.ecs.Component;

public class BackgroundColor implements Component{

	public float red;
	public float green;
	public float blue;
	
	public BackgroundColor() {
		this.red = 0f;
		this.green = 0f;
		this.blue = 0f;
	}
	
	public BackgroundColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
}
