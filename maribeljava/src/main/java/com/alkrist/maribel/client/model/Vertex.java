package com.alkrist.maribel.client.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {

	public static final int BYTES = 11 * Float.BYTES;
	public static final int FLOATS = 11;
	
	private Vector3f position;
	private Vector3f normal;
	private Vector2f textureCoord;
	private Vector3f tangent;
	private Vector3f bitangent;
	
	public Vertex() {}
	
	public Vertex(Vector3f pos)
	{
		this.setPosition(pos);
		this.setTextureCoord(new Vector2f(0));
		this.setNormal(new Vector3f(0));
	}
	
	public Vertex(Vector3f pos, Vector2f texture)
	{
		this.setPosition(pos);
		this.setTextureCoord(texture);
		this.setNormal(new Vector3f(0));
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

	public Vector2f getTextureCoord() {
		return textureCoord;
	}

	public void setTextureCoord(Vector2f uv) {
		this.textureCoord = uv;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public Vector3f getTangent() {
		return tangent;
	}

	public void setTangent(Vector3f tangent) {
		this.tangent = tangent;
	}

	public Vector3f getBitangent() {
		return bitangent;
	}

	public void setBitangent(Vector3f bitangent) {
		this.bitangent = bitangent;
	}
}
