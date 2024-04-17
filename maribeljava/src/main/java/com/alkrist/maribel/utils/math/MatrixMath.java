package com.alkrist.maribel.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class contains static methods with matrices and vectors that are used
 * for rendering.
 * 
 * @author Mikhail
 *
 */
public class MatrixMath {

	/**
	 * Creates transformation matrix for 2D object without rotation
	 * 
	 * @param translation
	 * @param scale - scale of the object: 1 - normal scale
	 * @return 4x4 floats transformation matrix
	 */
	/*public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		
		Matrix4f.translateLocal(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}*/
	
	/**
	 * Creates a transformation matrix for 3D object with rotation
	 * 
	 * @param translation
	 * @param rx - rotation around X in radians
	 * @param ry - rotation around Y in radians
	 * @param rz - rotation around Z in radians
	 * @param scale - scale of the object: 1 - normal scale
	 * @return 4x4 floats transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(translation).
		
			rotate((float)Math.toRadians(rotation.x), new Vector3f(1,0,0)).
			rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0)).
			rotate((float)Math.toRadians(rotation.z), new Vector3f(0,0,1)).
			scale(scale);
		
		return matrix;
	}
	
	/**
	 * Create projection matrix based on the window width and height and values from settings.
	 * 
	 * @param width - window width
	 * @param height - window height
	 * @return 4x4 floats projection matrix
	 */
	 public static Matrix4f createProjectionMatrix(int width, int height, float zNear, float zFar, float fovY){
		 Matrix4f projectionMatrix = new Matrix4f();
		 
		 float aspectRatio = (float) width / (float) height;
		 float y_scale = (float) (1f / Math.tan(Math.toRadians(fovY/2f)));
		 float x_scale = y_scale / aspectRatio;
		 float frustum_length = zFar - zNear;
	            
	     projectionMatrix = new Matrix4f();
	     projectionMatrix.m00(x_scale);
	     projectionMatrix.m11(y_scale);
	     projectionMatrix.m22(-((zFar + zNear) / frustum_length));
	     projectionMatrix.m23(-1);
	     projectionMatrix.m32(-((2 * zNear * zFar) / frustum_length));
	     projectionMatrix.m33(0);
			
	     return projectionMatrix;
	 }
	 
	 public static Matrix4f createViewMatrix(Vector3f position, float pitch, float yaw, float roll) {
		 Matrix4f viewMatrix = new Matrix4f();
		 viewMatrix.identity();
		 
		 viewMatrix.rotate((float)Math.toRadians(pitch), new Vector3f(1,0,0));
		viewMatrix.rotate((float)Math.toRadians(yaw), new Vector3f(0,1,0));
		viewMatrix.rotate((float)Math.toRadians(roll), new Vector3f(0,0,1));
		 
		 Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		 viewMatrix.translate(negativeCameraPos);
		 //Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		 
		 return viewMatrix;
	 }

	/* public static Vector4f[] createFrustumPlanes(Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
		 Vector4f frustumPlanes[] = new Vector4f[6];
		 Matrix4f pvm = new Matrix4f();
		 Matrix4f.mul(viewMatrix, modelMatrix, pvm);
		 Matrix4f.mul(projectionMatrix, pvm, pvm);
		 
		 Vector4f zNearPlane = new Vector4f(pvm.m20 + pvm.m30, pvm.m21 + pvm.m31, pvm.m22 + pvm.m32, pvm.m23 + pvm.m33);
			frustumPlanes[0] = MatrixMath.normalizePlane(zNearPlane);
			
			Vector4f zFarPlane = new Vector4f(-pvm.m20 + pvm.m30, -pvm.m21 + pvm.m31, -pvm.m22 + pvm.m32, -pvm.m23 + pvm.m33);
			frustumPlanes[1] = MatrixMath.normalizePlane(zFarPlane);
			
			Vector4f bottomPlane = new Vector4f(pvm.m10 + pvm.m30, pvm.m11 + pvm.m31, pvm.m12 + pvm.m32, pvm.m13 + pvm.m33);
			frustumPlanes[2] = MatrixMath.normalizePlane(bottomPlane);
			
			Vector4f topPlane = new Vector4f(-pvm.m10 + pvm.m30, -pvm.m11 + pvm.m31, -pvm.m12 + pvm.m32, -pvm.m13 + pvm.m33);
			frustumPlanes[3] = MatrixMath.normalizePlane(topPlane);
			
			Vector4f leftPlane = new Vector4f(pvm.m00 + pvm.m30, pvm.m01 + pvm.m31, pvm.m02 + pvm.m32, pvm.m03 + pvm.m33);
			frustumPlanes[4] = MatrixMath.normalizePlane(leftPlane);
			
			Vector4f rightPlane = new Vector4f(-pvm.m00 + pvm.m30, -pvm.m01 + pvm.m31, -pvm.m02 + pvm.m32, -pvm.m03 + pvm.m33);
			frustumPlanes[5] = MatrixMath.normalizePlane(rightPlane);
	 
			return frustumPlanes;
	 }*/
	 
	 /*public static Vector4f normalizePlane(Vector4f plane) {
		 float l;
		 l = (float) Math.sqrt(plane.x * plane.x + plane.y * plane.y + plane.z * plane.z);
		 plane.x = (plane.x/l);
		 plane.y = plane.y/l;
		 plane.z = plane.z/l;
		 plane.w = plane.w/l;
		
		 return plane;
	 }*/
}
