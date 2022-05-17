package com.alkrist.maribel.client.graphics;

import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

public class Camera implements Component{

	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(float x, float y, float z, float pitch, float yaw, float roll) {
		this.position = new Vector3f(x,y,z);
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	/*TEST METHOD*/
	public void move() {
		if(InputHandler.keyDown(GLFW.GLFW_KEY_W)) {
			position.z -= 0.01f;
		}
		if(InputHandler.keyDown(GLFW.GLFW_KEY_S)) {
			position.z += 0.01f;
		}
		if(InputHandler.keyDown(GLFW.GLFW_KEY_A)) {
			position.x -= 0.01f;
		}
		if(InputHandler.keyDown(GLFW.GLFW_KEY_D)) {
			position.x += 0.01f;
		}
		if(InputHandler.keyDown(GLFW.GLFW_KEY_Q)) {
			roll -= 1f;
		}
		if(InputHandler.keyDown(GLFW.GLFW_KEY_E)) {
			roll += 1f;
		}
	}
	
	public Vector3f getPosition() {
		return position;
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
}
