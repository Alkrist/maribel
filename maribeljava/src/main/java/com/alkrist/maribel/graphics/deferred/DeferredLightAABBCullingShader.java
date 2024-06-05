package com.alkrist.maribel.graphics.deferred;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class DeferredLightAABBCullingShader extends ShaderProgram{

	private static DeferredLightAABBCullingShader instance = null;
	
	public static DeferredLightAABBCullingShader getInstance() {
		if(instance == null) {
			instance = new DeferredLightAABBCullingShader();
		}
		
		return instance;
	}
	
	protected DeferredLightAABBCullingShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/deferred/deferred_cluster_culling.comp")));
		compileShader();
		
		addUniform("viewMatrix");
	}
	
	public void updateUniforms() {
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
	}
}
