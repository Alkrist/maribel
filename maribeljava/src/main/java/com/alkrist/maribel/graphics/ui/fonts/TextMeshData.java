package com.alkrist.maribel.graphics.ui.fonts;

/**
 * Contains data about actual text mesh.
 * <br/>
 * Vertex Positions - coordinates of the text mesh vertices
 * Texture Coordinates - it is understandable what it is :)
 * Width - text width in screen coordinates.
 * Height - text height in screen coordinates.
 * <br/>
 * Based on Karl's (aka ThinMatrix) implementation of TextMeshData.
 */
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
