package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;

public class ShadowRenderParameter implements RenderParameter{

	public ShadowRenderParameter() {}
	
	@Override
	public void enable() {
		glEnable(GL_POLYGON_OFFSET_FILL);
		glPolygonOffset(2.0f,2.0f);
	}

	@Override
	public void disable() {
		glDisable(GL_POLYGON_OFFSET_FILL);
	}

}
