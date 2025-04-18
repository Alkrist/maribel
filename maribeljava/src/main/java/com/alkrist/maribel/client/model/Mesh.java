package com.alkrist.maribel.client.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class Mesh {

	private Vertex[] vertices;
	private int[] indices;
	private boolean tangentSpace = false;
	
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public List<Vector2f> getTextureCoords(){
		
		ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
				
		for (Vertex v : vertices){
			texCoords.add(v.getTextureCoord());
		}
		
		return texCoords;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public boolean isTangentSpace() {
		return tangentSpace;
	}

	public void setTangentSpace(boolean tangentSpace) {
		this.tangentSpace = tangentSpace;
	}
}
