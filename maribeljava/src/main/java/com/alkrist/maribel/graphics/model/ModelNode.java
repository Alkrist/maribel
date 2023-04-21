package com.alkrist.maribel.graphics.model;

/**
 * This class represents a textured model. Used as one node in Maribel Model Composite.
 * 
 * @author Alkrist
 *
 */
public class ModelNode {

	private Mesh mesh;
	private Material material;
	
	private String name;
	
	private int textureOffsetIndex = 0;

	

	public ModelNode(Mesh mesh, Material material, String name) {
		this.mesh = mesh;
		this.material = material;
		this.name = name;
	}

	//TODO: new concept
	public float getTextureXOffset() {
		int column = textureOffsetIndex % material.getDiffuseMap().getNumberOfRows();
		return (float) column / (float) material.getDiffuseMap().getNumberOfRows();
	}
	
	//TODO: new concept
	public float getTextureYOffset() {
		int row = textureOffsetIndex / material.getDiffuseMap().getNumberOfRows();
		return (float) row / (float) material.getDiffuseMap().getNumberOfRows();
	}

	public Mesh getMesh() {
		return mesh;
	}
	
	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public int getTextureOffsetIndex() {
		return textureOffsetIndex;
	}

	public void setTextureOffsetIndex(int textureOffsetIndex) {
		this.textureOffsetIndex = textureOffsetIndex;
	}
	
}
