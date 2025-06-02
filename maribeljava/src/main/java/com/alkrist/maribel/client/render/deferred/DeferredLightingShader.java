package com.alkrist.maribel.client.render.deferred;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.util.Constants;
import com.alkrist.maribel.utils.FileUtils;

public class DeferredLightingShader extends ShaderProgram{

	private static DeferredLightingShader instance = null;
	
	public static DeferredLightingShader getInstance() {
		if(instance == null) {
			instance = new DeferredLightingShader();
		}
		return instance;
	}
	
	protected DeferredLightingShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/deferred/deferred_lighting.comp")));
		
		//addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/quad/shaders/deferred/deferred_lighting_vertex.glsl")));
		//addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/quad/shaders/deferred/deferred_lighting_fragment.glsl")));
		compileShader();
		
		addUniform("numSamples");
		addUniform("viewPosition");
		
		addUniformBlock("DirectionLight");
		//addUniformBlock("DirectionalLightViewProjections");
	}
	
	public void updateUniforms(int numSamples) {
		setUniform("numSamples", numSamples);
		
		setUniform("viewPosition", Context.getCamera().getPosition());
		
		bindUniformBlock("DirectionLight", Constants.DIRECTION_LIGHT_UBO_INDEX);
		//bindUniformBlock("DirectionalLightViewProjections", Constants.CSM_MATRICES_UBO_INDEX);
		
		/*setUniform("shadowEnable", Context.getVideoConfig().isShadowMapsEnabled ? 1 : 0);
		glActiveTexture(GL_TEXTURE0);
		csm.bind();
		setUniform("csm", 0);*/
	}
}
