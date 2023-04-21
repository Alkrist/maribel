package com.alkrist.maribel.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.InstancedRenderable;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public abstract class ArraysInstancedRenderer extends Renderer{
	
	public ArraysInstancedRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}
	
	@Override
	protected void draw(Entity entity) {
		InstancedRenderable component = instancedRenderableComponentMapper.getComponent(entity);
		glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, component.mesh.getVertexCount(), component.instances.size());
	}

}
