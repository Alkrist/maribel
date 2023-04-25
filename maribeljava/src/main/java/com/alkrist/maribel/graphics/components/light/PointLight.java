package com.alkrist.maribel.graphics.components.light;

import org.joml.Vector3f;

public class PointLight extends Light {

	private Attenuation attenuation;
	
	public PointLight(Vector3f position, Vector3f color, float intensity) {
		super(position, color, intensity);
		this.attenuation = new Attenuation(0, 0, 1);
	}
	
	public PointLight(Vector3f position, Vector3f color, float intensity, 
			float attConstant, float attLinear, float attExponent) {
		super(position, color, intensity);
		this.attenuation = new Attenuation(attConstant, attLinear, attExponent);
	}
	
	public Attenuation getAttenuation() {
		return attenuation;
	}
	
	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
	
	public void setAttenuation(float constant, float linear, float exponent) {
		this.attenuation = new Attenuation(constant, linear, exponent);
	}
	
	public static class Attenuation {
		
		private float constant;
		private float linear;
		private float exponent;
		
		public Attenuation(float constant, float linear, float exponent) {
			this.constant = constant;
			this.exponent = exponent;
			this.linear = linear;
		}
		
		public float getConstant() {
            return constant;
        }

        public float getExponent() {
            return exponent;
        }

        public float getLinear() {
            return linear;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }
	}
}
