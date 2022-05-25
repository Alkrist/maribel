package com.alkrist.maribel.client.graphics;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * Light Source Component.
 * 
 * Parameters:
 * 
 * position (x y z) - position of the light source in the world space. color (r
 * g b) - color of the light attenuation(a1 a2 a3) - attenuation of a light
 * source, used for point lights.
 * 
 * @author Mikhail
 *
 */
public class Light implements Component {

	public Vector3f position;
	public Vector3f color;
	public Vector3f attenuation = new Vector3f(1, 0, 0);

	/**
	 * Constructor for infinite light. Attenuation will be set to (1,0,0), which is
	 * unlimited.
	 * 
	 * @param position
	 * @param color
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}

	/**
	 * Constructor for point light with attenuation. Keep in mind that (1,0,0)
	 * attenuation will make it infinite.
	 * 
	 * @param position
	 * @param color
	 * @param attenuation
	 */
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	/**
	 * 
	 * @return if it's a point light or not, defined by it's attenuation property.
	 */
	public boolean isPointLight() {
		return attenuation.x != 1 || attenuation.y != 0 || attenuation.z != 0;
	}
}
