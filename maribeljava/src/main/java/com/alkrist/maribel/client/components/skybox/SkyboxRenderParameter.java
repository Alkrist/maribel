package com.alkrist.maribel.client.components.skybox;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import com.alkrist.maribel.graphics.render.RenderParameter;

public class SkyboxRenderParameter implements RenderParameter{

	@Override
	public void enable() {
		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
	}

	@Override
	public void disable() {
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);  
	}

}
