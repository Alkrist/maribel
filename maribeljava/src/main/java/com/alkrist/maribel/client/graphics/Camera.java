package com.alkrist.maribel.client.graphics;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

public class Camera implements Component{

	Vector3f position;
	float pitch;
	float yaw;
	float roll;
	
	public Camera(float x, float y, float z, float pitch, float yaw, float roll) {
		this.position = new Vector3f(x,y,z);
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	//TODO: move methods
	
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
