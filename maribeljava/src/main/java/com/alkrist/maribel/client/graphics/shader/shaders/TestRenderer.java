package com.alkrist.maribel.client.graphics.shader.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.render.ElementsRenderer;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public class TestRenderer extends ElementsRenderer implements Component{

	public TestRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}

	@Override
	protected void bindAttributes(Entity entity) {
		Renderable renderable = renderableComponentMapper.getComponent(entity);
		GL30.glBindVertexArray(renderable.mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0); // vertices
		GL20.glEnableVertexAttribArray(1); // texture coords
		
	}

	@Override
	protected void unbindAttributes() {
		GL20.glDisableVertexAttribArray(0); // vertices
		GL20.glDisableVertexAttribArray(1); // texture coords
		GL30.glBindVertexArray(0);
	}

}
