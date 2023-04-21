package com.alkrist.maribel.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.InstancedRenderable;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public abstract class ElementsInstancedRenderer extends Renderer{
	
	public ElementsInstancedRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}

	@Override
	public void draw(Entity entity) {
		InstancedRenderable component = instancedRenderableComponentMapper.getComponent(entity);
		glDrawElementsInstanced(GL_TRIANGLES, component.mesh.getVertexCount(), GL_UNSIGNED_INT, 0, component.instances.size());
	}
}
