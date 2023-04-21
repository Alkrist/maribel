package com.alkrist.maribel.graphics.components;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.render.ElementsRenderer;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public class OpaqueModelRenderer extends ElementsRenderer implements Component{

	public OpaqueModelRenderer(RenderParameter config, ShaderProgram shader) {
		super(config, shader);
	}

	@Override
	protected void bindAttributes(Entity entity) {
		Renderable renderable = renderableComponentMapper.getComponent(entity);
		glBindVertexArray(renderable.mesh.getVaoID());
		glEnableVertexAttribArray(0); // vertices
		glEnableVertexAttribArray(1); // texture coords
		glEnableVertexAttribArray(2); // normals
		glEnableVertexAttribArray(3); // tangents
	}

	@Override
	protected void unbindAttributes() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
	}

}
