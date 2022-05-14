package com.alkrist.maribel.utils.math;

import java.nio.FloatBuffer;

/**
 * Matrix base class
 * 
 * Based on LWJGL 2 Matrix by cix_foo, since this class is not included in LWJGL 3.
 * 
 * @author Mikhail
 *
 */
public abstract class Matrix {

	/**
	 * Constructor for Matrix.
	 */
	protected Matrix() {}
	
	/**
	 * Set this matrix to be the identity matrix.
	 * @return this
	 */
	public abstract Matrix setIdentity();
	
	/**
	 * Invert this matrix
	 * @return this
	 */
	public abstract Matrix invert();
	
	/**
	 * Load from a float buffer. The buffer stores the matrix in column major
	 * (OpenGL) order.
	 *
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public abstract Matrix load(FloatBuffer buf);
	
	/**
	 * Load from a float buffer. The buffer stores the matrix in row major
	 * (mathematical) order.
	 *
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public abstract Matrix loadTranspose(FloatBuffer buf);

	
	/**
	 * Negate this matrix
	 * @return this
	 */
	public abstract Matrix negate();
	
	/**
	 * Store this matrix in a float buffer. The matrix is stored in column
	 * major (openGL) order.
	 * @param buf The buffer to store this matrix in
	 * @return this
	 */
	public abstract Matrix store(FloatBuffer buf);
	
	/**
	 * Store this matrix in a float buffer. The matrix is stored in row
	 * major (maths) order.
	 * @param buf The buffer to store this matrix in
	 * @return this
	 */
	public abstract Matrix storeTranspose(FloatBuffer buf);
	
	/**
	 * Transpose this matrix
	 * @return this
	 */
	public abstract Matrix transpose();
	
	/**
	 * Set this matrix to 0.
	 * @return this
	 */
	public abstract Matrix setZero();
	
	/**
	 * @return the determinant of the matrix
	 */
	public abstract float determinant();
}
