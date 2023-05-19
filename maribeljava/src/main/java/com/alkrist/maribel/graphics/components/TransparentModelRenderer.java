package com.alkrist.maribel.graphics.components;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.render.ElementsRenderer;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public class TransparentModelRenderer extends ElementsRenderer implements Component{

	public TransparentModelRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}

	@Override
	protected void bindAttributes(Entity entity) {
		Renderable renderable = renderableComponentMapper.getComponent(entity);
		glBindVertexArray(renderable.mesh.getVaoID());
		glEnableVertexAttribArray(0); // vertices
		glEnableVertexAttribArray(1); // texture coords
		
	}

	@Override
	protected void unbindAttributes() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

}
