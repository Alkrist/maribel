package com.alkrist.maribel.graphics.components;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;

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

	public float intensity;
	
	/**
	 * Constructor for infinite light. Attenuation will be set to (1,0,0), which is
	 * unlimited.
	 * 
	 * @param position - light position
	 * @param color - light color
	 * @param intensity - light power range has no technical upper limit, but over 10 becomes too white
	 */
	public Light(Vector3f position, Vector3f color, float intensity) {
		this.position = position;
		this.color = color;
		
		this.intensity = Math.max(0, intensity);
	}

	/**
	 * Constructor for point light with attenuation. Keep in mind that (1,0,0)
	 * attenuation will make it infinite.
	 * 
	 * @param position - light position
	 * @param color - light color
	 * @param attenuation - the attenuation factor: A = attenuation.x + attenuation.y * d + attenuation * d^2
	 * @param intensity - light power range has no technical upper limit, but over 10 becomes too white
	 * where d = distance from a light position
	 */
	public Light(Vector3f position, Vector3f color, Vector3f attenuation, float intensity) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		
		this.intensity = Math.max(0, intensity);
	}

	/**
	 * 
	 * @return if it's a point light or not, defined by it's attenuation property.
	 */
	public boolean isPointLight() {
		return attenuation.x != 1 || attenuation.y != 0 || attenuation.z != 0;
	}
}
