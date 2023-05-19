package com.alkrist.maribel.client.graphics.shader.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.math.MatrixMath;

public class TestTransparencyShader extends ShaderProgram{

	private static final ComponentMapper<Renderable> renderableComponentMapper = ComponentMapper.getFor(Renderable.class);
	private static final ComponentMapper<Transform> transformComponentMapper = ComponentMapper.getFor(Transform.class);
	
	private static TestTransparencyShader instance = null;
	
	public static TestTransparencyShader getInstance() {
	    if(instance == null) {
	    	instance = new TestTransparencyShader();
	    }
	      return instance;
	}
	
	protected TestTransparencyShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"transparency\\sample_model_vertex.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"transparency\\sample_model_fragment.glsl"));
		compileShader();
		
		addUniform("diffusemap");
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		addUniform("modelMatrix");
	}
	
	@Override
	public void updateUniforms(Entity e) {
		Renderable renderable = renderableComponentMapper.getComponent(e);
		Transform transform = transformComponentMapper.getComponent(e);
		
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
		setUniform("modelMatrix", MatrixMath.createTransformationMatrix(transform.position, transform.rotation, transform.scale));
		setUniform("projectionMatrix", GLContext.getMainCamera().getProjectionMatrix());
		glActiveTexture(GL_TEXTURE0);
		renderable.material.getDiffuseMap().bind();
		setUniform("diffusemap", 0);
	}
}
