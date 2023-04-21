package com.alkrist.maribel.graphics.render;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;

import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public abstract class ArraysRenderer extends Renderer{
	
	public ArraysRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}
	
	@Override
	protected void draw(Entity entity) {
		Renderable component = renderableComponentMapper.getComponent(entity);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, component.mesh.getVertexCount());
	}
}
