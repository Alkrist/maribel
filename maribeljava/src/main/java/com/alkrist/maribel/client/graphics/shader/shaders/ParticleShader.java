package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;

public class ParticleShader extends ShaderBase{

	private int loc_numberOfRows;
	private int loc_projectionMatrix;
	
	
	public ParticleShader() {
		super("particle_vertex", "particle_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_numberOfRows = super.getUniformLocation("numberOfRows");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "textureOffsets");
		super.bindAttribute(6, "blendFactor");
	}
	
	public void loadNumberOfRows(float numberOfRows) {
		super.loadFloat(loc_numberOfRows, numberOfRows);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(loc_projectionMatrix, matrix);
	}

}
