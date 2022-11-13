package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;

public class ShadowMapShader extends ShaderBase{

	private int loc_mvpMatrix;
	
	public ShadowMapShader() {
		super("shadow_vertex", "shadow_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadMVPMatrix(Matrix4f mvpmatrix) {
		super.loadMatrix4f(loc_mvpMatrix, mvpmatrix);
	}

}
