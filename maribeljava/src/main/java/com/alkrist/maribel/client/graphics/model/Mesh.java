package com.alkrist.maribel.client.graphics.model;

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

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
