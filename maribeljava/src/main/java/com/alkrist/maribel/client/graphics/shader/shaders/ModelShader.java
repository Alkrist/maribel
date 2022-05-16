package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;

public class ModelShader extends ShaderBase{

	int loc_transformationMatrix;
	int loc_projectionMatrix;
	int loc_viewMatrix;
	
	public ModelShader() {
		super("static_vertex", "static_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix4f(loc_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(loc_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix4f(loc_viewMatrix, matrix);
	}
}
