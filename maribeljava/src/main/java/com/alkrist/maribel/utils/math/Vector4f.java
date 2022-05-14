package com.alkrist.maribel.utils.math;

import java.nio.FloatBuffer;

/**
 * Vector of 4 floats
 * 
 * Based on LWJGL 2 Vector2f by cix_foo, since this class is not included in
 * LWJGL 3.
 * 
 * @author Mikhail
 *
 */
public class Vector4f extends Vector {

	public float x, y, z, w;

	/**
	 * Constructor for Vector4f.
	 */
	public Vector4f() {
		super();
	}

	/**
	 * Constructor
	 */
	public Vector4f(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	/**
	 * Set values
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	/**
	 * Translate a vector
	 * 
	 * @param x The translation in x
	 * @param y the translation in y
	 * @return this
	 */
	public Vector4f translate(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}

	/**
	 * Add a vector to another vector and place the result in a destination vector.
	 * 
	 * @param left  The LHS vector
	 * @param right The RHS vector
	 * @param dest  The destination vector, or null if a new vector is to be created
	 * @return the sum of left and right in dest
	 */
	public static Vector4f add(Vector4f left, Vector4f right, Vector4f dest) {
		if (dest == null)
			return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
		else {
			dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
			return dest;
		}
	}

	/**
	 * Subtract a vector from another vector and place the result in a destination
	 * vector.
	 * 
	 * @param left  The LHS vector
	 * @param right The RHS vector
	 * @param dest  The destination vector, or null if a new vector is to be created
	 * @return left minus right in dest
	 */
	public static Vector4f sub(Vector4f left, Vector4f right, Vector4f dest) {
		if (dest == null)
			return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
		else {
			dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
			return dest;
		}
	}

	@Override
	public Vector load(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
		w = buf.get();
		return this;
	}

	@Override
	public Vector negate() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;
		return this;
	}

	/**
	 * Negate a vector and place the result in a destination vector.
	 * 
	 * @param dest The destination vector or null if a new vector is to be created
	 * @return the negated vector
	 */
	public Vector4f negate(Vector4f dest) {
		if (dest == null)
			dest = new Vector4f();
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		dest.w = -w;
		return dest;
	}

	/**
	 * Normalise this vector and place the result in another vector.
	 * 
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return the normalised vector
	 */
	public Vector4f normalise(Vector4f dest) {
		float l = length();

		if (dest == null)
			dest = new Vector4f(x / l, y / l, z / l, w / l);
		else
			dest.set(x / l, y / l, z / l, w / l);

		return dest;
	}

	/**
	 * The dot product of two vectors is calculated as v1.x * v2.x + v1.y * v2.y +
	 * v1.z * v2.z + v1.w * v2.w
	 * 
	 * @param left  The LHS vector
	 * @param right The RHS vector
	 * @return left dot right
	 */
	public static float dot(Vector4f left, Vector4f right) {
		return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
	}

	/**
	 * Calculate the angle between two vectors, in radians
	 * 
	 * @param a A vector
	 * @param b The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static float angle(Vector4f a, Vector4f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float) Math.acos(dls);
	}

	@Override
	public Vector store(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
		buf.put(w);

		return this;
	}

	@Override
	public Vector scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector4f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(", ");
		sb.append(w);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector4f other = (Vector4f) obj;

		if (x == other.x && y == other.y && z == other.z && w == other.w)
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash += Integer.valueOf(String.valueOf(x));
		hash += Integer.valueOf(String.valueOf(y));
		hash += Integer.valueOf(String.valueOf(z));
		hash += Integer.valueOf(String.valueOf(w));
		return hash * 31;
	}
}
