package com.alkrist.maribel.utils.math;

import java.nio.FloatBuffer;

/**
 * 2x2 float Matrix
 * 
 * Based on LWJGL 2 Matrix2f by cix_foo, since this class is not included in
 * LWJGL 3.
 * 
 * @author Mikhail
 *
 */
public class Matrix2f extends Matrix {

	/*
	 * | m00 m01 | 
	 * | m10 m11 |
	 */
	public float m00, m01, m10, m11;

	/**
	 * Constructor for Matrix2f. The matrix is initialised to the identity. 
	 * | m00 m01 | 
	 * | m10 m11 |
	 */
	public Matrix2f() {
		setIdentity();
	}

	/**
	 * Constructor 
	 * | m00 m01 | 
	 * | m10 m11 |
	 */
	public Matrix2f(Matrix2f src) {
		load(src);
	}

	/**
	 * Load from another matrix
	 * 
	 * @param src The source matrix
	 * @return this
	 */
	public Matrix2f load(Matrix2f src) {
		return load(src, this);
	}

	/**
	 * Copy the source matrix to the destination matrix.
	 * 
	 * @param src  The source matrix
	 * @param dest The destination matrix, or null if a new one should be created.
	 * @return The copied matrix
	 */
	public static Matrix2f load(Matrix2f src, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = src.m00;
		dest.m01 = src.m01;
		dest.m10 = src.m10;
		dest.m11 = src.m11;

		return dest;
	}

	@Override
	public Matrix setIdentity() {
		return setIdentity(this);
	}

	/**
	 * Set the source matrix to be the identity matrix.
	 * 
	 * @param src The matrix to set to the identity.
	 * @return The source matrix
	 */
	public static Matrix2f setIdentity(Matrix2f src) {
		src.m00 = 1.0f;
		src.m01 = 0.0f;
		src.m10 = 0.0f;
		src.m11 = 1.0f;
		return src;
	}

	@Override
	public Matrix invert() {
		return invert(this, this);
	}

	/**
	 * Invert the source matrix and place the result in the destination matrix.
	 * 
	 * @param src  The source matrix to be inverted
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return The inverted matrix, or null if source can't be reverted.
	 */
	public static Matrix2f invert(Matrix2f src, Matrix2f dest) {
		/*
		 * inv(A) = 1/det(A) * adj(A);
		 */

		float determinant = src.determinant();
		if (determinant != 0) {
			if (dest == null)
				dest = new Matrix2f();
			float determinant_inv = 1f / determinant;
			float t00 = src.m11 * determinant_inv;
			float t01 = -src.m01 * determinant_inv;
			float t11 = src.m00 * determinant_inv;
			float t10 = -src.m10 * determinant_inv;

			dest.m00 = t00;
			dest.m01 = t01;
			dest.m10 = t10;
			dest.m11 = t11;
			return dest;
		} else
			return null;
	}

	@Override
	public Matrix load(FloatBuffer buf) {
		m00 = buf.get();
		m01 = buf.get();
		m10 = buf.get();
		m11 = buf.get();

		return this;
	}

	@Override
	public Matrix loadTranspose(FloatBuffer buf) {
		m00 = buf.get();
		m10 = buf.get();
		m01 = buf.get();
		m11 = buf.get();

		return this;
	}

	@Override
	public Matrix negate() {
		return negate(this);
	}

	@Override
	public Matrix store(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m10);
		buf.put(m11);
		return this;
	}

	@Override
	public Matrix storeTranspose(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m10);
		buf.put(m01);
		buf.put(m11);
		return this;
	}

	/**
	 * Add two matrices together and place the result in a third matrix.
	 * 
	 * @param left  The left source matrix
	 * @param right The right source matrix
	 * @param dest  The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f add(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;

		return dest;
	}

	/**
	 * Subtract the right matrix from the left and place the result in a third
	 * matrix.
	 * 
	 * @param left  The left source matrix
	 * @param right The right source matrix
	 * @param dest  The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f sub(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;

		return dest;
	}

	/**
	 * Multiply the right matrix by the left and place the result in a third matrix.
	 * 
	 * @param left  The left source matrix
	 * @param right The right source matrix
	 * @param dest  The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f mul(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		float m00 = left.m00 * right.m00 + left.m10 * right.m01;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m10 = m10;
		dest.m11 = m11;

		return dest;
	}

	@Override
	public Matrix transpose() {
		return transpose(this);
	}

	/**
	 * Transpose this matrix and place the result in another matrix.
	 * 
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the transposed matrix
	 */
	public Matrix2f transpose(Matrix2f dest) {
		return transpose(this, dest);
	}

	/**
	 * Transpose the source matrix and place the result in the destination matrix.
	 * 
	 * @param src  The source matrix or null if a new matrix is to be created
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the transposed matrix
	 */
	public static Matrix2f transpose(Matrix2f src, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		float m01 = src.m10;
		float m10 = src.m01;

		dest.m01 = m01;
		dest.m10 = m10;

		return dest;
	}

	/**
	 * Negate this matrix and stash the result in another matrix.
	 * 
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return the negated matrix
	 */
	public Matrix2f negate(Matrix2f dest) {
		return negate(this, dest);
	}

	/**
	 * Negate the source matrix and stash the result in the destination matrix.
	 * 
	 * @param src  The source matrix to be negated
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return the negated matrix
	 */
	public static Matrix2f negate(Matrix2f src, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = -src.m00;
		dest.m01 = -src.m01;
		dest.m10 = -src.m10;
		dest.m11 = -src.m11;

		return dest;
	}

	@Override
	public Matrix setZero() {
		return setZero(this);
	}

	public static Matrix2f setZero(Matrix2f src) {
		src.m00 = 0.0f;
		src.m01 = 0.0f;
		src.m10 = 0.0f;
		src.m11 = 0.0f;
		return src;
	}

	@Override
	public float determinant() {
		return m00 * m11 - m01 * m10;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(m00).append(' ').append(m10).append(' ').append('\n');
		buf.append(m01).append(' ').append(m11).append(' ').append('\n');
		return buf.toString();
	}
}
