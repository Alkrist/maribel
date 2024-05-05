package com.alkrist.maribel.graphics.deferred;


import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;

public class DeferredTestShader extends ShaderProgram{

	private static DeferredTestShader instance = null;
	
	public static DeferredTestShader getInstance() {
		if(instance == null) {
			instance = new DeferredTestShader();
		}
		
		return instance;
	}
	
	protected DeferredTestShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\test_compute.comp"));
		compileShader();
		
		addUniform("zNear");
		addUniform("zFar");
		addUniform("gridSize");
		addUniform("screenDimensions");
		addUniform("viewMatrix");
		addUniform("cameraPosition");
	}
	
	public void updateUniforms(int width, int height, int gridSizeX, int gridSizeY, int gridSizeZ) {
		setUniform("zNear", GLContext.getConfig().NEAR_PLANE);
		setUniform("zFar", GLContext.getConfig().FAR_PLANE);
		setUniform("screenDimensions", width,height);
		setUniform("gridSize", gridSizeX, gridSizeY, gridSizeZ);
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
		setUniform("cameraPosition", GLContext.getMainCamera().getPosition());
	}
}
