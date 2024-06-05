package com.alkrist.maribel.graphics.filter.contrast;

import com.alkrist.maribel.graphics.filter.PPEProperty;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class ContrastShader extends ShaderProgram{

	private static ContrastShader instance = null;
	
	public static ContrastShader getInstance() {
		if(instance == null) {
			instance = new ContrastShader();
		}
		return instance;
	}
	
	protected ContrastShader() {
		super();
		
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/filter/contrast.comp")));
		
		compileShader();
		addUniform("contrastFactor");
		addUniform("brightnessFactor");
	}
	
	public void updateUniforms(PPEProperty property) {
		
		ContrastProperty prop = (ContrastProperty) property;
		
		setUniform("contrastFactor", prop.getContrastFactor());
		setUniform("brightnessFactor", prop.getBrightnessFactor());
	}
}
