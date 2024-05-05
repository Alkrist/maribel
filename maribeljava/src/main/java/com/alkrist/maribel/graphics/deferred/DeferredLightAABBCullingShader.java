package com.alkrist.maribel.graphics.deferred;

import org.joml.Matrix4f;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;

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
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\deferred_cluster_culling.comp"));
		compileShader();
		
		addUniform("viewMatrix");
	}
	
	public void updateUniforms(Matrix4f viewMatrix) {
		setUniform("viewMatrix", viewMatrix);
	}
}
