package com.alkrist.maribel.graphics.transparency;

import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtils;

public class OpaqueTransparencyBlendShader extends ShaderProgram{

	private static OpaqueTransparencyBlendShader instance = null;
	
	public static OpaqueTransparencyBlendShader getInstance() {
		if(instance == null) {
			instance = new OpaqueTransparencyBlendShader();
		}
		return instance;
	}
	
	protected OpaqueTransparencyBlendShader() {
		super();
		
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/transparency/opaque_transparency_blend.comp")));
		compileShader();
		
		addUniform("width");
		addUniform("height");
		addUniform("transparencyAlphaMap");
		addUniform("opaqueSceneDepthMap");
		addUniform("transparencyLayerDepthMap");
	}
	
	public void updateUniforms(Texture alphaMap, Texture opaqueDepthMap, Texture transparencyDepthMap) {
		float w = GLContext.getWindow().getWidth();
		float h = GLContext.getWindow().getHeight();
		setUniform("width", w);
		setUniform("height", h);
		glActiveTexture(GL_TEXTURE5);
		alphaMap.bind();
		setUniform("transparencyAlphaMap", 5);
		
		glActiveTexture(GL_TEXTURE3);
		opaqueDepthMap.bind();
		setUniform("opaqueSceneDepthMap", 3);
		
		glActiveTexture(GL_TEXTURE4);
		transparencyDepthMap.bind();
		setUniform("transparencyLayerDepthMap", 4);
	}
}
