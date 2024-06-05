package com.alkrist.maribel.graphics.occlusion;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class SSAOShader extends ShaderProgram{

	private static SSAOShader instance = null;

	public static SSAOShader getInstance() {
		if(instance == null) {
			instance = new SSAOShader();
		}
		return instance;
	}
	
	protected SSAOShader() {
		super();
		addComputeShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/filter/ssao/ssao_scene.comp")));
		compileShader();
		
		addUniform("viewMatrix");
		addUniform("projectionMatrix");
		addUniform("kernelSize");
		addUniform("uRadius");
		addUniform("threshold");
		addUniform("width");
		addUniform("height");
		
		for (int i=0; i<64; i++){
			addUniform("kernel[" + i + "]");
		}
	}
	
	public void updateUniforms(Matrix4f view, Matrix4f projection, int width, int height, Vector3f[] kernel) {
		setUniform("viewMatrix", view);
		setUniform("projectionMatrix", projection);
		setUniform("kernelSize", 64);
		setUniform("uRadius", 2f);
		setUniform("threshold", 0.02f);
		setUniform("width", width);
		setUniform("height", height);
		
		for (int i=0; i<64; i++){
			setUniform("kernel[" + i + "]", kernel[i]);
		}
	}
}
