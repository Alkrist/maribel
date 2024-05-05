package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.components.light.PointLight.Attenuation;

public class SpotLight extends Light{

	private Vector3f coneDirection;
	private float cutOff;
	private float cutOffAngle;
	private Attenuation attenuation;
	
	public SpotLight(Vector3f position, Vector3f color, float intensity, Vector3f coneDirection, float cutOffAngle) {
		super(position, color, intensity);
		this.attenuation = new Attenuation(0, 0, 1);
		this.coneDirection = coneDirection;
		this.cutOffAngle = cutOffAngle;
	}
	
	public SpotLight(Vector3f position, Vector3f color, float intensity, 
			float attConstant, float attLinear, float attExponent, Vector3f coneDirection, float cutOffAngle) {
		super(position, color, intensity);
		this.attenuation = new Attenuation(attConstant, attLinear, attExponent);
		this.coneDirection = coneDirection;
		this.cutOffAngle = cutOffAngle;
	}

	public Vector3f getConeDirection() {
        return coneDirection;
    }

    public float getCutOff() {
        return cutOff;
    }

    public float getCutOffAngle() {
        return cutOffAngle;
    }
    
    public Attenuation getAttenuation() {
		return attenuation;
	}
    
    public void setConeDirection(float x, float y, float z) {
        coneDirection.set(x, y, z);
    }

    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    public final void setCutOffAngle(float cutOffAngle) {
        this.cutOffAngle = cutOffAngle;
        cutOff = (float) Math.cos(Math.toRadians(cutOffAngle));
    }
    
    public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
	
	public void setAttenuation(float constant, float linear, float exponent) {
		this.attenuation = new Attenuation(constant, linear, exponent);
	}
}
