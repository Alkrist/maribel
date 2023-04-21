package com.alkrist.maribel.graphics.render;

import static org.lwjgl.opengl.GL11.glDrawElements;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public abstract class ElementsRenderer extends Renderer{
	
	public ElementsRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}

	@Override
	protected void draw(Entity entity) {
		Renderable component = renderableComponentMapper.getComponent(entity);
		glDrawElements(GL_TRIANGLES, component.mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
	}
}
