package com.alkrist.maribel.client.graphics.shader.shaders;

import java.util.List;

import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;

public class ModelShader extends ShaderBase{

	private static final int MAX_LIGHTS = 4;
	
	int loc_transformationMatrix;
	int loc_projectionMatrix;
	int loc_viewMatrix;
	int loc_lightPosition[];
	int loc_lightColor[];
	int loc_lightAttenuation[];
	int loc_shineDamper;
	int loc_reflectivity;
	//TODO: new stuff
	int loc_numberOfRows;
	int loc_textureOffset;
	
	public ModelShader() {
		super("static_vertex", "static_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_shineDamper = super.getUniformLocation("shineDamper");
		loc_reflectivity = super.getUniformLocation("reflectivity");
		loc_lightPosition = new int [MAX_LIGHTS];
		loc_lightColor = new int[MAX_LIGHTS];
		loc_lightAttenuation = new int[MAX_LIGHTS];
		for(int i=0; i<4; i++) {
			loc_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			loc_lightColor[i] = super.getUniformLocation("lightColor["+i+"]");
			loc_lightAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
		//TODO: new stuff
		loc_numberOfRows = super.getUniformLocation("numberOfRows");
		loc_textureOffset = super.getUniformLocation("textureOffset");
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
	
	public void loadLights(List<Light>lights) {
		for(int i=0; i<MAX_LIGHTS; i++) {
			if(i<lights.size()) {
				super.loadVector3f(loc_lightPosition[i], lights.get(i).position);
				super.loadVector3f(loc_lightColor[i], lights.get(i).color);
				super.loadVector3f(loc_lightAttenuation[i], lights.get(i).attenuation);
			}else {
				super.loadVector3f(loc_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector3f(loc_lightColor[i], new Vector3f(0,0,0));
				super.loadVector3f(loc_lightAttenuation[i], new Vector3f(1,0,0));
			}
		}
	}
	
	public void loadSpecularProperties(float shine, float reflect) {
		super.loadFloat(loc_shineDamper, shine);
		super.loadFloat(loc_reflectivity, reflect);
	}
	
	//TODO: new stuff
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(loc_numberOfRows, numberOfRows);
	}
	
	//TODO: new stuff
	public void loadTextureOffset(float x, float y) {
		super.loadVector2f(loc_textureOffset, new Vector2f(x,y));
	}
}