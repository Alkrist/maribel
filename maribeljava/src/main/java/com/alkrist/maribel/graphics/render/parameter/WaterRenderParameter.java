package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;


import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class WaterRenderParameter implements RenderParameter{

	private float r;
	private float g;
	private float b;
	
	public WaterRenderParameter(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void enable() {
		glEnable(GL_CULL_FACE);
	}

	@Override
	public void disable() {
		glDisable(GL_CULL_FACE);
	}

	public void clearScreenDeepOcean(){
		glClearColor(r, g, b, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
}
