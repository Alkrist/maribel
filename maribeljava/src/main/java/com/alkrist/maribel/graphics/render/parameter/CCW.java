package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.glFrontFace;

public class CCW implements RenderParameter{

	public CCW() {}
	
	@Override
	public void enable() {
		glFrontFace(GL_CCW);
	}

	@Override
	public void disable() {
		glFrontFace(GL_CW);
	}

}
