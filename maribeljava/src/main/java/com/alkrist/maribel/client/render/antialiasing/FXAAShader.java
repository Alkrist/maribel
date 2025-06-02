package com.alkrist.maribel.client.render.antialiasing;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.utils.FileUtils;

public class FXAAShader extends ShaderProgram{

	private static FXAAShader instance = null;
	
	public static FXAAShader getInstance() {
		if(instance == null) {
			instance = new FXAAShader();
		}
		return instance;
	}
	
	protected FXAAShader() {
		super();
		
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/antialiasing/fxaa.comp")));
		
		compileShader();
		addUniform("sceneSampler");
		addUniform("width");
		addUniform("height");
	}
	
	public void updateUniforms(Texture sceneTexture){
		glActiveTexture(GL_TEXTURE0);
		sceneTexture.bind();
		sceneTexture.bilinearFilter();
		setUniform("sceneSampler", 0);
		
		setUniform("width", (float) Context.getWindow().getWidth());
		setUniform("height", (float) Context.getWindow().getHeight());
	}
	
}
