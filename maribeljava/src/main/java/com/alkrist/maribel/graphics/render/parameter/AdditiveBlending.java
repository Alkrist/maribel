package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class AdditiveBlending implements RenderParameter{

	public AdditiveBlending() {}
	
	@Override
	public void enable() {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);	
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
	}

	@Override
	public void disable() {
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

}
