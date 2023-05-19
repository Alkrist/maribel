package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class AlphaBlendingSrcAlpha implements RenderParameter{

	public AlphaBlendingSrcAlpha() {}
	
	@Override
	public void enable() {
		glEnable(GL_BLEND);	
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void disable() {
		glDisable(GL_BLEND);
	}

}
