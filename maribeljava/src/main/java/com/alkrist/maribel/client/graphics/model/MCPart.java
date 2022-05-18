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
	private String material;
	private float shineDamper;
	private float reflecivity;
	private boolean transparency;
	
	public MCPart(Mesh mesh, Texture texture, String name, 
			float shineDamper, float reflecivity, String material, boolean transparency) {
		this.mesh = mesh;
		this.texture = texture;
		this.name = name;
		this.material = material;
		this.transparency = transparency;
		this.shineDamper = shineDamper;
		this.reflecivity = reflecivity;
	}
	
	public String getMaterial() {
		return material;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflecivity() {
		return reflecivity;
	}

	public boolean isTransparent() {
		return transparency;
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
