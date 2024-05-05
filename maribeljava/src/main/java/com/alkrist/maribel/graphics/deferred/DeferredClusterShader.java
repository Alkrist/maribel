package com.alkrist.maribel.graphics.deferred;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;

public class DeferredClusterShader extends ShaderProgram{

	private static DeferredClusterShader instance = null;
	
	public static DeferredClusterShader getInstance() {
		if(instance == null) {
			instance = new DeferredClusterShader();
		}
		
		return instance;
	}
	
	protected DeferredClusterShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\deferred_cluster_build.comp"));
		compileShader();
		
		addUniform("zNear");
		addUniform("zFar");
		addUniform("inverseProjection");
		addUniform("gridSize");
		addUniform("screenDimensions");
	}
	
	public void updateUniforms(int width, int height) {
		setUniform("zNear", GLContext.getConfig().NEAR_PLANE);
		setUniform("zFar", GLContext.getConfig().FAR_PLANE);
		setUniform("inverseProjection", GLContext.getMainCamera().getInvertedProjectionMatrix());
		setUniform("screenDimensions", width, height);
		setUniform("gridSize", 
				DeferredClusteredLighting.GRID_SIZE_X, 
				DeferredClusteredLighting.GRID_SIZE_Y, 
				DeferredClusteredLighting.GRID_SIZE_Z);
	}
}
