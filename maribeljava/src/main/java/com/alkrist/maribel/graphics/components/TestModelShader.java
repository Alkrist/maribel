package com.alkrist.maribel.graphics.components;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.math.Transform;
import com.alkrist.maribel.client.render.model.Material;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.client.render.scenegraph.Renderable;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.math.MatrixMath;

public class TestModelShader extends ShaderProgram{

	private static TestModelShader instance = null;
	
	public static TestModelShader getInstance() {
	    if(instance == null) {
	    	instance = new TestModelShader();
	    }
	      return instance;
	}
	
	protected TestModelShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/generic_model_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/generic_model_fragment.glsl")));
		compileShader();
		
		addUniform("material.diffusemap");
		addUniform("material.normalmap");
		addUniform("material.shininess");
		addUniform("material.emission");
		
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		addUniform("modelMatrix");
	}
	
	public void updateUniforms(Renderable object) {
		Transform transform = object.getWorldTransform();
		Matrix4f modelMatrix = MatrixMath.createTransformationMatrix(transform.getTranslation(), transform.getRotation(), transform.getScaling().x);
		Material material = (Material) object.getComponent("material");
		glActiveTexture(GL_TEXTURE0);
		material.getDiffusemap().bind();
		setUniform("material.diffusemap", 0);
		
		glActiveTexture(GL_TEXTURE1);
		material.getNormalmap().bind();
		setUniform("material.normalmap", 1);
		setUniform("material.shininess", material.getShininess());
		setUniform("material.emission", material.getEmission());
		
		setUniform("modelMatrix", modelMatrix);
		setUniform("projectionMatrix", Context.getCamera().getProjectionMatrix());
		setUniform("viewMatrix", Context.getCamera().getViewMatrix());
	}
}
