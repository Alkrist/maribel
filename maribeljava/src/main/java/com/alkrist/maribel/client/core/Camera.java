package com.alkrist.maribel.client.core;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.alkrist.maribel.client.util.Constants;

public class Camera {

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Matrix4f viewProjectionMatrix;
    
    private float width;
    private float height;
    private float fovY;
    
    private Vector3f position;
    private Vector3f previousPosition;
    private Quaternionf orientation;
    
    private boolean isMoved;
    
    public Camera(Vector3f position, Vector3f rotation, float fovY, float width, float height) {
        this.position = new Vector3f(position);
        this.previousPosition = new Vector3f(position);
        this.orientation = new Quaternionf()
            .rotateX(rotation.x)
            .rotateY(rotation.y)
            .rotateZ(rotation.z);
        
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = new Matrix4f();
        setProjectionMatrix(fovY, width, height);
        updateViewMatrix();
        updateViewProjection();
        
        isMoved = false;
    }
    
    /**
     * Updates camera matrices based on current position and orientation
     * @param deltaTime time since last update in seconds
     */
    public void update(double deltaTime) {
    	
    	isMoved = !position.equals(previousPosition);
    	
        updateViewMatrix();
        updateViewProjection();
        previousPosition.set(position);
    }
    
    /**
     * Moves camera by specified offsets in local space
     * @param dx movement in local X axis
     * @param dy movement in local Y axis
     * @param dz movement in local Z axis
     */
    public void moveLocal(float dx, float dy, float dz) {
        // Transform movement vector from local to world space
        Vector3f move = new Vector3f(dx, dy, dz);
        move.rotate(orientation);
        position.add(move);
    }
    
    /**
     * Rotates camera by specified angles around local axes
     * @param pitch rotation around local X axis (radians)
     * @param yaw rotation around local Y axis (radians)
     * @param roll rotation around local Z axis (radians)
     */
    public void rotateLocal(float pitch, float yaw, float roll) {
        Quaternionf rot = new Quaternionf()
            .rotateLocalX(pitch)
            .rotateLocalY(yaw)
            .rotateLocalZ(roll);
        orientation.mul(rot);
    }
    
    /**
     * Sets the projection matrix with given parameters
     */
    public void setProjectionMatrix(float fovY, float width, float height) {
        this.fovY = fovY;
        this.width = width;
        this.height = height;
        
        float aspectRatio = width / height;
        this.projectionMatrix = new Matrix4f().perspective(fovY, aspectRatio, 
            Constants.ZNEAR, Constants.ZFAR);
    }
    
    /**
     * Updates the view matrix based on current position and orientation
     */
    public void updateViewMatrix() {
        viewMatrix.identity()
            .rotate(orientation.invert(new Quaternionf())) // Invert rotation
            .translate(-position.x, -position.y, -position.z);
    }
    
    /**
     * Updates the combined view-projection matrix
     */
    private void updateViewProjection() {
        viewProjectionMatrix.set(projectionMatrix).mul(viewMatrix);
    }
    
    public Matrix4f getProjectionMatrix() { return new Matrix4f(projectionMatrix); }
    public Matrix4f getViewMatrix() { return new Matrix4f(viewMatrix); }
    public Matrix4f getViewProjectionMatrix() { return new Matrix4f(viewProjectionMatrix); }
    
    public Vector3f getPosition() { return new Vector3f(position); }
    public void setPosition(Vector3fc position) { this.position.set(position); }
    
    public Vector3f getPreviousPosition() { return new Vector3f(previousPosition); }
    public void setPreviousPosition(Vector3fc previousPosition) { 
        this.previousPosition.set(previousPosition); 
    }
    
    public Quaternionf getOrientation() { return new Quaternionf(orientation); }
    public void setOrientation(Quaternionf orientation) { 
        this.orientation.set(orientation); 
    }
    
    public Vector3f getRotationEuler() {
        Vector3f euler = new Vector3f();
        orientation.getEulerAnglesXYZ(euler);
        return euler;
    }
    
    public void setRotationEuler(Vector3fc rotation) {
        orientation.identity()
            .rotateX(rotation.x())
            .rotateY(rotation.y())
            .rotateZ(rotation.z());
    }
    
    public float getFovY() { return fovY; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public boolean isMoved() { return isMoved; }
}
