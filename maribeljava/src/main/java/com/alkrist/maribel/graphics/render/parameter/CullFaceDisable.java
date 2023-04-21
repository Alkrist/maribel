package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class CullFaceDisable implements RenderParameter{

	public CullFaceDisable() {}
	
	@Override
	public void enable() {
		glDisable(GL_CULL_FACE);
	}

	@Override
	public void disable() {
		glEnable(GL_CULL_FACE);
	}

}
