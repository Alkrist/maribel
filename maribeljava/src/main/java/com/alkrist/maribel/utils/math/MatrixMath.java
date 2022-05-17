package com.alkrist.maribel.utils.math;

import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.client.Settings;
import com.alkrist.maribel.client.graphics.Camera;

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
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
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
		matrix.setIdentity();
		
		Matrix4f.translate(translation, matrix, matrix);
		
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	/**
	 * Create projection matrix based on the window width and height and values from settings.
	 * 
	 * @param width - window width
	 * @param height - window height
	 * @return 4x4 floats projection matrix
	 */
	 public static Matrix4f createProjectionMatrix(int width, int height){
	    	Matrix4f projectionMatrix = new Matrix4f();
	            float aspectRatio = (float) width / (float) height;
	            float y_scale = (float) ((1f / Math.tan(Math.toRadians(Settings.CURRENT.FOV/2f))) * aspectRatio);
	            float x_scale = y_scale / aspectRatio;
	            float frustum_length = Settings.CURRENT.FAR_PLANE - Settings.CURRENT.NEAR_PLANE;
	            
	            projectionMatrix = new Matrix4f();
	            projectionMatrix.m00 = x_scale;
	            projectionMatrix.m11 = y_scale;
	            projectionMatrix.m22 = -((Settings.CURRENT.FAR_PLANE + Settings.CURRENT.NEAR_PLANE) / frustum_length);
	            projectionMatrix.m23 = -1;
	            projectionMatrix.m32 = -((2 * Settings.CURRENT.NEAR_PLANE * Settings.CURRENT.FAR_PLANE) / frustum_length);
	            projectionMatrix.m33 = 0;
			
			return projectionMatrix;
	 }
	 
	 /**
	  * Create view matrix based on camera position and rotation
	  * @param camera
	  * @return 4x4 floats view Matrix
	  */
	 public static Matrix4f createViewMatrix(Camera camera) {
			Matrix4f viewMatrix = new Matrix4f();
			viewMatrix.setIdentity();
			
			Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix); 
			Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
			Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix); 
			
			Vector3f cameraPos = camera.getPosition();
			Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			
			Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
			return viewMatrix;
	}
	
}
