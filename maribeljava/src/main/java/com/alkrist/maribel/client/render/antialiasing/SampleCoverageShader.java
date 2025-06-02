package com.alkrist.maribel.client.render.antialiasing;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.utils.FileUtils;

public class SampleCoverageShader extends ShaderProgram{

private static SampleCoverageShader instance = null;
	
	public static SampleCoverageShader getInstance() {
		if(instance == null) {
			instance = new SampleCoverageShader();
		}
		return instance;
	}
	
	protected SampleCoverageShader() {
		super();
		
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/antialiasing/sample_coverage.comp")));
		compileShader();
		
		addUniform("multisamples");
	}
	
	public void updateUniforms(){
		
		setUniform("multisamples", Context.getVideoConfig().multisampleSamplesCount);
	}
}
