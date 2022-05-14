package com.alkrist.maribel.utils.math;

import java.nio.FloatBuffer;

/**
 * Base class for Vectors.
 * 
 * Based on LWJGL 2 Vector by cix_foo, since this class is not included in LWJGL
 * 3.
 * 
 * @author Mikhail
 *
 */
public abstract class Vector {

	/**
	 * Constructor to be used in child classes
	 */
	protected Vector() {
	}

	/**
	 * 
	 * @return length of this vector
	 */
	public final float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * 
	 * @return (length of this vector) ^ 2
	 */
	public abstract float lengthSquared();

	/**
	 * Load this vector from a float buffer
	 * 
	 * @param buf - buffer that contains vector
	 * @return vector
	 */
	public abstract Vector load(FloatBuffer buf);

	/**
	 * Nehave a vector
	 * 
	 * @return negated vector
	 */
	public abstract Vector negate();

	/**
	 * Store this vector in a given float buffer
	 * 
	 * @param buf - buffer to store the vector
	 * @return current vector
	 */
	public abstract Vector store(FloatBuffer buf);

	/**
	 * Scale vector
	 * 
	 * @param scale
	 * @return this scaled vector
	 */
	public abstract Vector scale(float scale);

	/**
	 * Normalize this vector.
	 * 
	 * @return this normalized vector
	 */
	public final Vector normalise() {
		float len = length();
		if (len != 0.0f) {
			float l = 1.0f / len;
			return scale(l);
		} else
			throw new IllegalStateException("Zero length vector");
	}

}
