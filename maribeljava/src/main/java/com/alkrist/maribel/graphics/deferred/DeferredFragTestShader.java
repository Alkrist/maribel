package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;

public class DeferredFragTestShader extends ShaderProgram{

private static DeferredFragTestShader instance = null;
	
	public static DeferredFragTestShader getInstance() {
		if(instance == null) {
			instance = new DeferredFragTestShader();
		}
		
		return instance;
	}
	
	protected DeferredFragTestShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\test_vertex.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\test_fragment.glsl"));
		compileShader();
		
		addUniform("zNear");
		addUniform("zFar");
		addUniform("gridSize");
		addUniform("screenDimensions");
		addUniform("viewMatrix");
		
		addUniform("albedoSampler");
		addUniform("worldPositionSampler");
		addUniform("normalSampler");
		addUniform("specular_emission_diffuse_ssao_bloom_Sampler");
	}
	
	public void updateUniforms(Texture albedo, Texture position, Texture normal, Texture ssao) {
		glActiveTexture(GL_TEXTURE0);
		albedo.bind();
		setUniform("albedoSampler", 0);
		
		glActiveTexture(GL_TEXTURE1);
		position.bind();
		setUniform("worldPositionSampler", 1);
		
		glActiveTexture(GL_TEXTURE2);
		normal.bind();
		setUniform("normalSampler", 2);
		
		glActiveTexture(GL_TEXTURE3);
		ssao.bind();
		setUniform("specular_emission_diffuse_ssao_bloom_Sampler", 3);
	}
	
	public void updateUniforms(int width, int height, int gridSizeX, int gridSizeY, int gridSizeZ) {
		setUniform("zNear", GLContext.getConfig().NEAR_PLANE);
		setUniform("zFar", GLContext.getConfig().FAR_PLANE);
		setUniform("screenDimensions", width,height);
		setUniform("gridSize", gridSizeX, gridSizeY, gridSizeZ);
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
	}
}
