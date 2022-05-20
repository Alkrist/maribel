package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector4f;

public class GUIShader extends ShaderBase{

	public GUIShader() {
		super("gui_vertex", "gui_fragment");
	}

	private int loc_color;
	private int loc_hasTexture;
	private int loc_transform;
	
	@Override
	protected void getAllUniformLocations() {
		loc_color = super.getUniformLocation("frameColor");
		loc_hasTexture = super.getUniformLocation("hasTexture");
		loc_transform = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadColor(Vector4f color) {
		super.loadVector4f(loc_color, color);
	}
	
	public void loadHasTexture(boolean hasTexture) {
		super.loadBoolean(loc_hasTexture, hasTexture);
	}
	
	public void loadTransformation(Matrix4f transform) {
		super.loadMatrix4f(loc_transform, transform);
	}
}
