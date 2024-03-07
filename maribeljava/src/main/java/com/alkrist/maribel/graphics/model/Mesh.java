package com.alkrist.maribel.graphics.model;

/**
 * This class represents a raw mesh containing VAO id and vertex count of this
 * object
 * 
 * @author Alkrist
 *
 */
public class Mesh {

	private int vaoID;
	private int vertexCount;
	private float boundingRadius = 0;
	
	/**
	 * Constructor for raw mesh.
	 * 
	 * @param vaoID       - id of the VAO where this mesh data is stored.
	 * @param vertexCount - number of vertices of this mesh.
	 */
	public Mesh(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public Mesh(int vaoID, int vertexCount, float boundingRadius) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.boundingRadius = boundingRadius;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public boolean hasBoundingSphere() {
		return boundingRadius > 0;
	}
	
	public float getBoundingRadius() {
		return boundingRadius;
	}
	
	@Override
	public String toString() {
		return "[VAO ID: "+vaoID+", vertices: "+vertexCount+"]";
	}
	
	public boolean clearBuffer() {
		return ResourceLoader.deleteVAO(vaoID);
	}

}
