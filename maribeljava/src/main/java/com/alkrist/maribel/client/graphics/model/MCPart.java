package com.alkrist.maribel.client.graphics.model;

import com.alkrist.maribel.client.graphics.texture.Texture;

/**
 * This class represents a textured model. Used as one node in Maribel Model Composite.
 * 
 * @author Mikhail
 *
 */
public class MCPart {

	private Mesh mesh;
	private Texture texture;
	
	private String name;
	//TODO: add material properties
	//TODO: add different constructors based on args
	
	public MCPart(Mesh mesh, Texture texture, String name) {
		this.mesh = mesh;
		this.texture = texture;
		this.name = name;
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public Texture getTexture() {
		return texture;
	}

	public String getName() {
		return name;
	}
	
}
