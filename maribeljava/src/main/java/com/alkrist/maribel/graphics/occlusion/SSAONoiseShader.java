package com.alkrist.maribel.graphics.occlusion;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class SSAONoiseShader extends ShaderProgram{

	private static SSAONoiseShader instance = null;
	
	public static SSAONoiseShader getInstance() {
		if(instance == null) {
			instance = new SSAONoiseShader();
		}
		return instance;
	}
	
	protected SSAONoiseShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/filter/ssao/ssao_noise.comp")));
		compileShader();
		
		for (int i=0; i<16; i++){
			addUniform("randomx[" + i + "]");
		}
		
		for (int i=0; i<16; i++){
			addUniform("randomy[" + i + "]");
		}
	}
	
	public void updateUniforms(float[] x, float[] y) {
		for (int i=0; i<16; i++){
			setUniform("randomx[" + i + "]", x[i]);
		}
		
		for (int i=0; i<16; i++){
			setUniform("randomy[" + i + "]", y[i]);
		}
	}
}
