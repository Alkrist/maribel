package com.alkrist.maribel.graphics.render;

import org.joml.Vector3f;

public class InstancedObject {

	public Vector3f position;
	public Vector3f rotation;
	public float scale;
	
	public InstancedObject(Vector3f position, Vector3f rotation, float scale) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public InstancedObject(Vector3f position, Vector3f rotation) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.scale = 1;
	}
	
	public InstancedObject(float x, float y, float z, float rX, float rY, float rZ, float s) {
		this(new Vector3f(x, y, z), new Vector3f(rX,rY, rZ), s);
	}
	
	public InstancedObject(float x, float y, float z, float rX, float rY, float rZ) {
		this(new Vector3f(x, y, z), new Vector3f(rX,rY, rZ));
	}
}
