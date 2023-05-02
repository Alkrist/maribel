package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.client.TestGraphics;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;

public class DeferredLightingShader extends ShaderProgram{

	private static DeferredLightingShader instance = null;
	
	public static DeferredLightingShader getInstance() {
		if(instance == null) {
			instance = new DeferredLightingShader();
		}
		return instance;
	}
	
	protected DeferredLightingShader() {
		addComputeShader(readShaderFromFile(FileUtil.getShadersPath()+"deferred\\deferredLighting.comp"));
		compileShader();
		
		addUniform("dirLight.color");
		addUniform("dirLight.direction");
		addUniform("dirLight.intensity");
		
		addUniform("pointLight.color");
		addUniform("pointLight.position");
		addUniform("pointLight.intensity");
		addUniform("pointLight.att.constant");
		addUniform("pointLight.att.linear");
		addUniform("pointLight.att.exponent");
		
		addUniform("viewMatrix");
		addUniform("pssm");
		for(int i=0; i<PSSMCamera.PSSM_SPLITS; i++) {
			addUniform("projViewMatrices["+i+"]");
			addUniform("splitDistances["+i+"]");
		}
	}
	
	public void updateUniforms(Texture pssm) {
		setUniform("dirLight.direction", TestGraphics.sun.getPosition());
		setUniform("dirLight.color", TestGraphics.sun.getColor());
		setUniform("dirLight.intensity", TestGraphics.sun.getIntensity());
		
		setUniform("pointLight.color", TestGraphics.light1.getColor());
		setUniform("pointLight.position", TestGraphics.light1.getPosition());
		setUniform("pointLight.intensity", TestGraphics.light1.getIntensity());
		setUniform("pointLight.att.constant", TestGraphics.light1.getAttenuation().getConstant());
		setUniform("pointLight.att.linear", TestGraphics.light1.getAttenuation().getLinear());
		setUniform("pointLight.att.exponent", TestGraphics.light1.getAttenuation().getExponent());
		
		setUniform("viewMatrix", GLContext.getMainCamera().getViewMatrix());
		for(int i=0; i<PSSMCamera.PSSM_SPLITS; i++) {
			setUniform("projViewMatrices["+i+"]",PSSMCamera.getProjectionViewMatrices()[i]);
			setUniform("splitDistances["+i+"]",PSSMCamera.getSplitDistances()[i]);
		}

		glActiveTexture(GL_TEXTURE0);
		pssm.bind();
		setUniform("pssm", 0);
	}
}
