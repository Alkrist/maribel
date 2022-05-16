package com.alkrist.maribel.client.graphics;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * Transformation of the object.
 * 
 * Position (x y z) - position of the object in world coordinates. Rotation (x y
 * z) - rotation of the object in radians. Scale - scale of the object (1 is
 * normal scale)
 * 
 * @author Mikhail
 *
 */
public class Transform implements Component {

	public Vector3f position;
	public Vector3f rotation;
	public float scale;

	/**
	 * Constructor with vector parameters.
	 * 
	 * @param pos   - position
	 * @param rot   - rotation
	 * @param scale
	 */
	public Transform(Vector3f pos, Vector3f rot, float scale) {
		this.position = pos;
		this.rotation = rot;
		this.scale = scale;
	}

	/**
	 * Constructor with separate parameters.
	 * 
	 * @param x  - position x
	 * @param y  - position y
	 * @param z  - position z
	 * @param rX - rotation around x
	 * @param rY - rotation around y
	 * @param rZ - rotation around z
	 * @param s  - scale
	 */
	public Transform(float x, float y, float z, float rX, float rY, float rZ, float s) {
		this(new Vector3f(x, y, z), new Vector3f(rX, rY, rZ), s);
	}

	/**
	 * Constructor with no scale, will be set to 1.
	 * 
	 * @param pos - position
	 * @param rot - rotation
	 */
	public Transform(Vector3f pos, Vector3f rot) {
		this(pos, rot, 1);
	}

	public void rotate(float dx, float dy, float dz) {
		rotation.x+=dx;
		rotation.y+=dy;
		rotation.z+=dz;
	}
	
	public void move(float dx, float dy, float dz) {
		position.x+=dx;
		position.y+=dy;
		position.z+=dz;
	}
}
