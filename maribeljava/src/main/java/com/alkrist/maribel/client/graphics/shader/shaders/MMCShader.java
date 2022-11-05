package com.alkrist.maribel.client.graphics.shader.shaders;

import java.util.List;

import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;
import com.alkrist.maribel.utils.math.Vector4f;

public class MMCShader extends ShaderBase{

private static final int MAX_LIGHTS = 4;
	
	private int loc_transformationMatrix;
	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_lightPosition[];
	private int loc_lightColor[];
	private int loc_lightAttenuation[];
	private int loc_shineDamper;
	private int loc_reflectivity;
	private int loc_numberOfRows;
	private int loc_textureOffset;
	private int loc_modelTexture;
	private int loc_normalMap;
	private int loc_hasNormalMap;
	private int loc_density;
	private int loc_gradient;
	private int loc_skyColor;
	
	public MMCShader() {
		super("mmc_normal_vertex", "mmc_normal_fragment");
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
		for(int i=0; i<MAX_LIGHTS; i++) {
			loc_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			loc_lightColor[i] = super.getUniformLocation("lightColor["+i+"]");
			loc_lightAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
		
		loc_numberOfRows = super.getUniformLocation("numberOfRows");
		loc_textureOffset = super.getUniformLocation("textureOffset");
		loc_modelTexture = super.getUniformLocation("modelTexture");
		loc_normalMap = super.getUniformLocation("normalMap");
		loc_hasNormalMap = super.getUniformLocation("hasNormalMap");
		
		loc_density = super.getUniformLocation("density");
		loc_gradient = super.getUniformLocation("gradient");
		loc_skyColor = super.getUniformLocation("skyColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	public void loadBackgroundColor(float r, float g, float b) {
		super.loadVector3f(loc_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadFogEffect(float density, float gradient) {
		super.loadFloat(loc_gradient, gradient);
		super.loadFloat(loc_density, density);
	}
	
	public void connectTextureUnits() {
		super.loadInt(loc_modelTexture, 0);
		super.loadInt(loc_normalMap, 1);
	}
	
	public void loadHasNormalMap(boolean hasNormalMap) {
		super.loadBoolean(loc_hasNormalMap, hasNormalMap);
	}
	
	public void loadSpecularProperties(float shine, float reflect) {
		super.loadFloat(loc_shineDamper, shine);
		super.loadFloat(loc_reflectivity, reflect);
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
	
	public void loadLights(List<Light>lights, Matrix4f viewMatrix) {
		for(int i=0; i<MAX_LIGHTS; i++) {
			if(i<lights.size()) {
				super.loadVector3f(loc_lightPosition[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVector3f(loc_lightColor[i], lights.get(i).color);
				super.loadVector3f(loc_lightAttenuation[i], lights.get(i).attenuation);
			}else {
				super.loadVector3f(loc_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector3f(loc_lightColor[i], new Vector3f(0,0,0));
				super.loadVector3f(loc_lightAttenuation[i], new Vector3f(1,0,0));
			}
		}
	}

	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(loc_numberOfRows, numberOfRows);
	}

	public void loadTextureOffset(float x, float y) {
		super.loadVector2f(loc_textureOffset, new Vector2f(x,y));
	}
	
	private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.position;
		Vector4f eyeSpacePosition = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePosition, eyeSpacePosition);
		return new Vector3f(eyeSpacePosition.x, eyeSpacePosition.y, eyeSpacePosition.z);
	}
}
