package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;

public class StaticShader extends ShaderBase{

	public StaticShader() {
		super("static_vertex", "static_fragment");
	}

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
	}

}
