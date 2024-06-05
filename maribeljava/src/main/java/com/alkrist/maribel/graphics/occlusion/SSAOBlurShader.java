package com.alkrist.maribel.graphics.occlusion;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class SSAOBlurShader extends ShaderProgram{

	private static SSAOBlurShader instance = null;
	
	public static SSAOBlurShader getInstance() {
		if(instance == null) {
			instance = new SSAOBlurShader();
		}
		return instance;
	}
	
	protected SSAOBlurShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/filter/ssao/ssao_blur.comp")));
		compileShader();
	}
}
