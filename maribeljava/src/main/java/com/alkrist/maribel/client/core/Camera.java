package com.alkrist.maribel.client.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.alkrist.maribel.client.util.Constants;

public class Camera {

	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	//private Matrix4f viewProjectionMatrix;
	
	private boolean cameraMoved;
	private boolean cameraRotated;
	
	private float width;
	private float height;
	private float fovY;
	
	private Vector3f position;
	private Vector3f previousPosition;
	private Vector3f direction;
	private Vector3f right;
	private Vector3f up;
	
	private Vector3f rotation;
	
	protected Camera(Vector3f position, Vector3f rotation, float fovY, float width, float height) {
		direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        
        this.position = position;
        this.rotation = rotation;
        
        this.previousPosition = position;
        
        // mind the order
        setProjectionMatrix(fovY, width, height);
        updateViewMatrix();

	}
	
	public void update() {
		// TODO: ???
		if(cameraMoved || cameraRotated) {
			updateViewMatrix();
		}
	}
	
	 	public void moveBackwards(float inc) {
	        viewMatrix.positiveZ(direction).negate().mul(inc);
	        position.sub(direction);
	        cameraMoved = true;
	    }

	    public void moveDown(float inc) {
	        viewMatrix.positiveY(up).mul(inc);
	        position.sub(up);
	        cameraMoved = true;
	        
	    }

	    public void moveForward(float inc) {
	        viewMatrix.positiveZ(direction).negate().mul(inc);
	        position.add(direction);
	        cameraMoved = true;
	    }

	    public void moveLeft(float inc) {
	        viewMatrix.positiveX(right).mul(inc);
	        position.sub(right);
	        cameraMoved = true;
	    }

	    public void moveRight(float inc) {
	        viewMatrix.positiveX(right).mul(inc);
	        position.add(right);
	        cameraMoved = true;
	    }

	    public void moveUp(float inc) {
	        viewMatrix.positiveY(up).mul(inc);
	        position.add(up);
	        cameraMoved = true;
	    }

	    public void addRotation(float x, float y, float z) {
	        rotation.add(x, y, z);
	        cameraRotated = true;
	    }
	    
	public void setProjectionMatrix(float fovY, float width, float height) {
		this.fovY = fovY;
		this.width = width;
		this.height = height;
		
		float aspectRatio = width / height;
		
		this.projectionMatrix = new Matrix4f().perspective(fovY, aspectRatio, Constants.ZNEAR, Constants.ZFAR);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void updateViewMatrix() {
		viewMatrix.identity();
		viewMatrix
		.rotateX(rotation.x)
		.rotateY(rotation.y)
		.rotateZ(rotation.z)
		.translate(-position.x, -position.y, -position.z);
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public float getFovY(){
		return fovY;
	}
	
	public float getWidth(){
		return width;
	}

	public float getHeight(){
		return height;
	}
	
	public boolean isCameraMoved() {
		return cameraMoved;
	}

	public boolean isCameraRotated() {
		return cameraRotated;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPreviousPosition() {
		return previousPosition;
	}
	
	public void setPreviousPositon(Vector3f previousPosition) {
		this.previousPosition = previousPosition;
	}
	
	/*private void updateViewProjection() {
		viewProjectionMatrix.zero();
		viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
	}*/
}
