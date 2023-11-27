package com.alkrist.maribel.graphics.ui.fonts;

import org.joml.Vector3f;

import com.alkrist.maribel.graphics.ui.UIElement;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;

public class UIText extends UIElement{

	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);
	
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	
	public UIText(UIConstraints constraints, String text, float fontSize, FontType font, float maxLineLength,
			boolean centered) {
		super(constraints);
		
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
	}

	@Override
	protected void updateInternal(double deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderInternal() {
		// TODO Auto-generated method stub
		
	}

	public int getMesh() {
		return textMeshVao;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void setMeshData(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	public Vector3f getColour() {
		return color;
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public FontType getFont() {
		return font;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	protected String getTextString() {
		return textString;
	}
	
	protected float getFontSize() {
		return fontSize;
	}
	
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}
	
	protected boolean isCentered() {
		return centerText;
	}
	
	protected float getMaxLineSize() {
		return lineMaxSize;
	}
}
