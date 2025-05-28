package com.alkrist.maribel.graphics.ui.fonts;

public class TextMeshData {

	private float[] vertexPositions;
	private float[] textureCoords;
	
	// Width and height of the text in screen coordinates
	private float width;
	private float height;
	
	protected TextMeshData(float[] vertexPositions, float[] textureCoords, float width, float height){
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
		
		this.width = width;
		this.height = height;
	}

	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public int getVertexCount() {
		return vertexPositions.length/2;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
