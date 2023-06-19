package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlendingSrcAlpha;

public class WindowCanvas extends UICanvas implements Component{

	private static final RenderParameter config = new AlphaBlendingSrcAlpha();
	
	public WindowCanvas() {
		super();
	}
	
	@Override
	public void render() {
		glDisable(GL_DEPTH_TEST);    
		glDisable(GL_CULL_FACE);
		config.enable();
		
		elements.forEach(element -> element.render());
		
		config.disable();
		glEnable(GL_DEPTH_TEST);     
		glEnable(GL_CULL_FACE);
	}

	@Override
	public void update(double deltaTime) {
		elements.forEach(element -> element.update(deltaTime));
	}

}
