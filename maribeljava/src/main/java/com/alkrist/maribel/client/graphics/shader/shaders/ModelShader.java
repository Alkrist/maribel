package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;

public class ModelShader extends ShaderBase{

	int loc_transformationMatrix;
	int loc_projectionMatrix;
	int loc_viewMatrix;
	int loc_lightPosition;
	int loc_lightColor;
	int loc_shineDamper;
	int loc_reflectivity;
	
	public ModelShader() {
		super("static_vertex", "static_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_lightPosition = super.getUniformLocation("lightPosition");
		loc_lightColor = super.getUniformLocation("lightColor");
		loc_shineDamper = super.getUniformLocation("shineDamper");
		loc_reflectivity = super.getUniformLocation("reflectivity");
		
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
	
	public void loadLight(Light light) {
		super.loadVector3f(loc_lightPosition, light.position);
		super.loadVector3f(loc_lightColor, light.color);
	}
	
	public void loadSpecularProperties(float shine, float reflect) {
		super.loadFloat(loc_shineDamper, shine);
		super.loadFloat(loc_reflectivity, reflect);
	}
}
