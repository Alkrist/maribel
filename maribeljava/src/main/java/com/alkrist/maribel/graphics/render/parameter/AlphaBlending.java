package com.alkrist.maribel.graphics.render.parameter;

import com.alkrist.maribel.graphics.render.RenderParameter;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class AlphaBlending implements RenderParameter{

	private int sourceBlendFactor;
	private int destinationBlendFactor;
	
	public AlphaBlending(int sourceBlendFactor, int destinationBlendFactor) {
		this.sourceBlendFactor = sourceBlendFactor;
		this.destinationBlendFactor = destinationBlendFactor;
	}

	@Override
	public void enable() {
		glEnable(GL_BLEND);	
		glBlendFunc(sourceBlendFactor, destinationBlendFactor);
	}

	@Override
	public void disable() {
		glDisable(GL_BLEND);
	}

}
