package com.alkrist.maribel.graphics.antialiasing;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;

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
		
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"antialiasing\\fxaa.comp"));
		
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
		
		setUniform("width", (float) GLContext.getConfig().width);
		setUniform("height", (float) GLContext.getConfig().height);
	}
	
}
