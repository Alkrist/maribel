package com.alkrist.maribel.client.graphics.shader.shaders;

import com.alkrist.maribel.client.graphics.shader.ShaderBase;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector4f;

public class GUIShader extends ShaderBase{

	public GUIShader() {
		super("gui_vertex", "gui_fragment");
	}

	private int loc_color;
	private int loc_hasTexture;
	private int loc_transform;
	private int loc_framePixelPosition;
	private int loc_framePixelSize;
	private int loc_cornerRadius;
	private int loc_borderColor;
	private int loc_borderSize;
	
	@Override
	protected void getAllUniformLocations() {
		loc_color = super.getUniformLocation("frameColor");
		loc_hasTexture = super.getUniformLocation("hasTexture");
		loc_transform = super.getUniformLocation("transformationMatrix");
		loc_framePixelPosition = super.getUniformLocation("framePixelPosition");
		loc_framePixelSize = super.getUniformLocation("framePixelSize");
		loc_cornerRadius = super.getUniformLocation("cornerRadius");
		loc_borderColor = super.getUniformLocation("borderColor");
		loc_borderSize = super.getUniformLocation("borderSize");
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
	
	public void loadFrameValues(Vector2f position, Vector2f size) {
		super.loadVector2f(loc_framePixelPosition, position);
		super.loadVector2f(loc_framePixelSize, size);
	}
	
	public void loadCornerRadius(float radiusPx) {
		super.loadFloat(loc_cornerRadius, radiusPx);
	}
	
	public void loadBorderProperties(float size, Vector4f color) {
		super.loadFloat(loc_borderSize, size);
		super.loadVector4f(loc_borderColor, color);
	}
}
