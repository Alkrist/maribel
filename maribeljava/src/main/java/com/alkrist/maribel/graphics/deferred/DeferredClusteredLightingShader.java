package com.alkrist.maribel.graphics.deferred;

import com.alkrist.maribel.graphics.components.light.AmbientLight;
import com.alkrist.maribel.graphics.components.light.DirectionLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;

public class DeferredClusteredLightingShader extends ShaderProgram{

private static DeferredClusteredLightingShader instance = null;
	
	public static DeferredClusteredLightingShader getInstance() {
		if(instance == null) {
			instance = new DeferredClusteredLightingShader();
		}
		return instance;
	}
	
	protected DeferredClusteredLightingShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\deferred_lighting.comp"));
		compileShader();
		
		// General uniforms
		addUniform("zNear");
		addUniform("zFar");
		addUniform("gridSize");
		addUniform("screenDimensions");
		addUniform("viewMatrix");
		addUniform("cameraPosition");
		
		// Direction light
		addUniform("directionLight.color");
		addUniform("directionLight.direction");
		addUniform("directionLight.intensity");
		addUniform("directionLightEnable");
		
		// Ambient light
		addUniform("ambientLight.color");
		addUniform("ambientLight.intensity");
		addUniform("ambientLightEnable");
		
		// SSAO
		addUniform("ssaoEnable");
		
		// MSAA
		addUniform("numSamples");
	}
	
	public void updateUniforms(int width, int height, int gridSizeX, int gridSizeY, int gridSizeZ) {
		setUniform("zNear", GLContext.getConfig().NEAR_PLANE);
		setUniform("zFar", GLContext.getConfig().FAR_PLANE);
		setUniform("screenDimensions", width,height);
		setUniform("gridSize", gridSizeX, gridSizeY, gridSizeZ);
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
		setUniform("cameraPosition", GLContext.getMainCamera().getPosition());
	}
	
	public void updateUniforms(DirectionLight dirLight, AmbientLight ambLight) {
		if(dirLight != null) {
			setUniform("directionLight.color", dirLight.getColor());
			setUniform("directionLight.direction", dirLight.getPosition());
			setUniform("directionLight.intensity", dirLight.getIntensity());
			setUniformUnsignedInt("directionLightEnable", 1);
		}else {
			setUniformUnsignedInt("directionLightEnable", 0);
		}
		
		if(ambLight != null) {
			setUniform("ambientLight.color", ambLight.getColor());
			setUniform("ambientLight.intensity", ambLight.getIntensity());
			setUniformUnsignedInt("ambientLightEnable", 1);
		}else {
			setUniformUnsignedInt("ambientLightEnable", 0);
		}
	}
	
	public void updateUniforms(boolean ssaoEnable, int numSamples) {
		setUniformUnsignedInt("ssaoEnable", ssaoEnable ? 1 : 0);
		setUniformUnsignedInt("numSamples", numSamples);
	}
}
