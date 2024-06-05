package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector3f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtils;

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
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/deferred/deferred_lighting.comp")));
		compileShader();
		
		// General uniforms
		addUniform("zNear");
		addUniform("zFar");
		addUniform("gridSize");
		addUniform("screenDimensions");
		addUniform("viewMatrix");
		addUniform("cameraPosition");
		
		// SSAO
		addUniform("ssaoEnable");
		
		// MSAA
		addUniform("numSamples");
		
		// Shadow
		addUniform("pssm");
		addUniform("shadowEnable");
		addUniform("shadowsQuality");
		for(int i=0; i<PSSMCamera.PSSM_SPLITS; i++) {
			addUniform("projViewMatrices["+i+"]");
			addUniform("splitDistances["+i+"]");
		}
		
		//Fog
		addUniform("sightRange");
		addUniform("fogColor");
	}
	
	public void updateUniforms(int width, int height, int gridSizeX, int gridSizeY, int gridSizeZ, Texture pssm) {
		// General uniforms
		setUniform("zNear", GLContext.getConfig().NEAR_PLANE);
		setUniform("zFar", GLContext.getConfig().FAR_PLANE);
		setUniform("screenDimensions", width,height);
		setUniform("gridSize", gridSizeX, gridSizeY, gridSizeZ);
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
		setUniform("cameraPosition", GLContext.getMainCamera().getPosition());
		
		// SSAO
		setUniformUnsignedInt("ssaoEnable", GLContext.getConfig().isSSAOEnabled ? 1 : 0);
		
		// MSAA
		setUniformUnsignedInt("numSamples", GLContext.getConfig().multisampleSamplesCount);
		
		// PSSM
		if(GLContext.getConfig().isShadowMapsEnabled) {
			for(int i=0; i<PSSMCamera.PSSM_SPLITS; i++) {
				setUniform("projViewMatrices["+i+"]",PSSMCamera.getProjectionViewMatrices()[i]);
				setUniform("splitDistances["+i+"]",PSSMCamera.getSplitDistances()[i]);
			}
			setUniformUnsignedInt("shadowEnable", 1);
			setUniformUnsignedInt("shadowsQuality", GLContext.getConfig().shadowMapQuality);
			
			glActiveTexture(GL_TEXTURE0);
			pssm.bind();
			setUniform("pssm", 0);

		}else {
			setUniformUnsignedInt("shadowEnable", 0);
		}
		
		//TODO: to test
		setUniform("sightRange", 0.5f); //this is a lot, like, for real, trust me xD
		setUniform("fogColor", new Vector3f(0.5f));
	}	
}
