package com.alkrist.maribel.graphics.context;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.graphics.platform.InputHandler;

public class Camera {
	
	private Vector3f position;
	
	private float pitch;
	private float yaw;
	private float roll;
	
	private Matrix4f projectionMatrix;
	
	private Matrix4f viewMatrix;
	private Matrix4f invViewMatirx;
	
	private Matrix4f projectionViewMatrix;
	private Matrix4f invProjectionMatrix;
	
	private FrustumIntersection frustumIntersection;
	
	private boolean isMoved = false;
	 
	
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		setPosition(position);
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
		
		viewMatrix = new Matrix4f();
		invViewMatirx = new Matrix4f();
		projectionViewMatrix = new Matrix4f();
		invProjectionMatrix = new Matrix4f();
		frustumIntersection = new FrustumIntersection();
	}
	
	public void init() {
		//setProjectionMatrix(GLContext.getConfig().fovY, width, height, GLContext.getConfig().NEAR_PLANE, GLContext.getConfig().FAR_PLANE);
		updateViewMatrix();
		//viewMatrix = MatrixMath.createViewMatrix(position, pitch, yaw, roll);
		updateFrustum();
	}
	
	//TODO: change camera update method based on type, this is just a TEST METHOD!!!
	public void update() {
		
		InputHandler input = GLContext.getInput();
		isMoved = false;
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_Q)) {
			roll -= 0.1f;
			isMoved = true;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_E)) {
			roll += 0.1f;
			isMoved = true;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_W)) {
			position.z += 0.1f;
			isMoved = true;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_S)) {
			position.z -= 0.1f;
			isMoved = true;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_A)) {
			position.x -= 0.1f;
			isMoved = true;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_D)) {
			position.x += 0.1f;
			isMoved = true;
		}
		
		updateViewMatrix();
		//viewMatrix = MatrixMath.createViewMatrix(position, pitch, yaw, roll);
		updateFrustum();
	}
	
	
	public boolean insideFrustum(Vector3f center, float boundingRadius) {
		return frustumIntersection.testSphere(center, boundingRadius);
	}
	
	public void setProjectionMatrix(float fovY, int width, int height, float zNear, float zFar) {
		//setWidthHeight(width, height);
		//this.projectionMatrix = MatrixMath.createProjectionMatrix(width, height, zNear, zFar, fovY);
		projectionMatrix = new Matrix4f();
		this.projectionMatrix.perspective(fovY, ((float) width / (float) height), zNear, zFar);
		
		this.invProjectionMatrix.set(projectionMatrix).invert();
		
		/*float aspectRatio = (float) width / (float) height;
	    float yScale = (float) (1.0 / Math.tan(Math.toRadians(fovY / 2.0)));
	    float xScale = yScale / aspectRatio;
	    float frustumLength = zFar - zNear;

	    projectionMatrix.m00(xScale);
	    projectionMatrix.m11(yScale);
	    projectionMatrix.m22(-((zFar + zNear) / frustumLength));
	    projectionMatrix.m23(-1);
	    projectionMatrix.m32(-((2 * zNear * zFar) / frustumLength));
	    projectionMatrix.m33(0);*/
	    
	    //this.invProjectionMatrix.set(projectionMatrix).invert();
	}
	
	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public Matrix4f getInvertedViewMatrix() {
		return invViewMatirx;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f getInvertedProjectionMatrix() {
		return invProjectionMatrix;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public boolean isMoved() {
		return isMoved;
	}
	
	private Matrix4f updateViewMatrix() {
		
		viewMatrix.identity();
		
		viewMatrix.rotate((float)Math.toRadians(pitch), new Vector3f(1,0,0));
		viewMatrix.rotate((float)Math.toRadians(yaw), new Vector3f(0,1,0));
		viewMatrix.rotate((float)Math.toRadians(roll), new Vector3f(0,0,1));
		
		Vector3f negativePos = new Vector3f(-position.x, -position.y, -position.z);
		viewMatrix.translate(negativePos);
		
		this.invViewMatirx.set(viewMatrix).invert();
		
		return viewMatrix;
	}
	
	private void updateFrustum() {
		projectionViewMatrix.set(projectionMatrix);
		projectionViewMatrix.mul(viewMatrix);
		
		frustumIntersection.set(projectionViewMatrix);
	}
}
