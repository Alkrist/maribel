package com.alkrist.maribel.client.render.pipeline;

import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;

public class DepthTestDisable implements RenderParameter{

	@Override
	public void enable() {
		glDepthMask(false);
		glDepthFunc(GL_LEQUAL);
	}

	@Override
	public void disable() {
		glDepthMask(true);
		glDepthFunc(GL_LESS);
	}

}
