package com.alkrist.maribel.client.components.skybox;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.model.Material;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.client.render.scenegraph.Renderable;
import com.alkrist.maribel.utils.FileUtils;

public class SkyboxShader extends ShaderProgram{

	private static SkyboxShader instance = null;
	
	public static SkyboxShader getInstance() {
		if(instance == null) {
			instance = new SkyboxShader();
		}
		
		return instance;
	}
	
	protected SkyboxShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/skybox/skybox_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/skybox/skybox_fragment.glsl")));
		compileShader();
		
		addUniform("viewMatrix");
		addUniform("projectionMatrix");
		
		addUniform("textureSampler");
	}
	
	public void updateUniforms(Renderable object) {
		setUniform("projectionMatrix", Context.getCamera().getProjectionMatrix());
		
		Matrix4f viewMatrix = Context.getCamera().getViewMatrix();
		viewMatrix.m30(0); // Set translation X to 0
        viewMatrix.m31(0); // Set translation Y to 0
        viewMatrix.m32(0); // Set translation Z to 0
        setUniform("viewMatrix", viewMatrix);
		
		Material material = (Material) object.getComponent("material");
		
		glActiveTexture(GL_TEXTURE0);
		material.getDiffusemap().bind();
		setUniform("textureSampler", 0);
	}
}
