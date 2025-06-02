package com.alkrist.maribel.client.render.light;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.alkrist.maribel.client.render.memory.UBO;
import com.alkrist.maribel.client.render.shadows.CascadeShadow;
import com.alkrist.maribel.client.util.Constants;
import com.alkrist.maribel.client.util.Util;

public class DirectionLight extends Light{

	private Vector3f ambientColor;
	
	protected final int lightBufferSize = Float.BYTES * 12;
	private FloatBuffer floatBufferLight;
	private UBO uboLight;
	
	private CascadeShadow[] csmCameras;
	private FloatBuffer floatBufferMatrices;
	private UBO uboMatrices;
	protected final int matricesBufferSize = Float.BYTES * (16 * 3 + 4 * 3);// TODO: define size
	
	public DirectionLight(Vector3f direction, Vector3f primaryColor, Vector3f ambientColor, float intensity) {
		super(direction, primaryColor, intensity);
		this.ambientColor = ambientColor;
		
		// light ubo
		floatBufferLight = Util.createFloatBuffer(lightBufferSize);
		this.uboLight = new UBO();
		uboLight.setBindingIndex(Constants.DIRECTION_LIGHT_UBO_INDEX);
		uboLight.bindBufferBase();
		uboLight.allocate(lightBufferSize);
		//uboLight.updateData(getFloatBufferLight(), lightBufferSize);
		updateLightBuffer();
		
		// csm ubo
		csmCameras = new CascadeShadow[Constants.CSM_SPLITS];
		for(int i=0; i < Constants.CSM_SPLITS; i++) {
			csmCameras[i] = new CascadeShadow();
		}
		CascadeShadow.updateCascadeShadows(csmCameras, this);
		uboMatrices = new UBO();
		uboMatrices.setBindingIndex(Constants.CSM_MATRICES_UBO_INDEX);
		uboMatrices.bindBufferBase();
		uboMatrices.allocate(matricesBufferSize);
		updateMatrixBuffer();
		
	}

	public void updateLightBuffer() {
		floatBufferLight.clear();
		floatBufferLight.put(Util.createFlippedBuffer(getLocalTransform().getTranslation()));
		floatBufferLight.put(intensity);
		floatBufferLight.put(Util.createFlippedBuffer(ambientColor));
		floatBufferLight.put(0);
		floatBufferLight.put(Util.createFlippedBuffer(color));
		floatBufferLight.put(0);
		floatBufferLight.flip();
		
		uboLight.updateData(floatBufferLight, lightBufferSize);
	}
	
	public void updateMatrixBuffer() {
		// Initialize buffer if not done yet
	    if (floatBufferMatrices == null) {
	        floatBufferMatrices = Util.createFloatBuffer(matricesBufferSize);
	        this.uboMatrices = new UBO();
	        uboMatrices.setBindingIndex(Constants.CSM_MATRICES_UBO_INDEX);
	        uboMatrices.bindBufferBase();
	        uboMatrices.allocate(matricesBufferSize);
	    }

	    floatBufferMatrices.clear();
	    
	    // Store matrices and split distances with proper std140 alignment
	    for (int i = 0; i < Constants.CSM_SPLITS; i++) {
	        // Each mat4 takes 16 floats (column-major)
	        Matrix4f projView = csmCameras[i].getProjViewMatrix();
	        floatBufferMatrices.put(projView.m00()).put(projView.m10()).put(projView.m20()).put(projView.m30());
	        floatBufferMatrices.put(projView.m01()).put(projView.m11()).put(projView.m21()).put(projView.m31());
	        floatBufferMatrices.put(projView.m02()).put(projView.m12()).put(projView.m22()).put(projView.m32());
	        floatBufferMatrices.put(projView.m03()).put(projView.m13()).put(projView.m23()).put(projView.m33());
	    }
	    
	    // Align to vec4 boundary (std140 requires arrays of floats to be padded to vec4)
	    for (int i = 0; i < Constants.CSM_SPLITS; i++) {
	        floatBufferMatrices.put(csmCameras[i].getSplitDistance());
	        // Pad with 3 floats to make it vec4-aligned
	        if (i % 4 != 3) {
	            floatBufferMatrices.put(0f).put(0f).put(0f);
	        }
	    }
	    
	    floatBufferMatrices.flip();
	    uboMatrices.updateData(floatBufferMatrices, matricesBufferSize);
	}
	
	public Vector3f getDirection() {
		return getLocalTransform().getTranslation();
	}
	
	public Vector3f getAmbientColor() {
		return ambientColor;
	}
	
	public FloatBuffer getFloatBufferLight() {
		return floatBufferLight;
	}
}
