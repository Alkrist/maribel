package com.alkrist.maribel.client.graphics.shader.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;


import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.math.MatrixMath;

public class TestShader extends ShaderProgram{

	private static TestShader instance = null;
	private static final ComponentMapper<Renderable> renderableComponentMapper = ComponentMapper.getFor(Renderable.class);
	private static final ComponentMapper<Transform> transformComponentMapper = ComponentMapper.getFor(Transform.class);
	
	public static TestShader getInstance() {
	    if(instance == null) {
	    	instance = new TestShader();
	    }
	      return instance;
	}
	
	protected TestShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/test_vs.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/test_fs.glsl")));
		compileShader();
		
		addUniform("modelTexture");
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
		setUniform("modelTexture", 0);
	}
}
