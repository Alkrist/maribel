package com.alkrist.maribel.client.graphics.shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.Transform;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ShadowMapShader;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;

public class ShadowMapMaster {

	private static final int SHADOW_MAP_SIZE = 2048;
	
	private ShadowMapFBO shadowFbo;
	private ShadowBox shadowBox;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	private Matrix4f offset = createOffset();
	
	private ShadowMapShader shader;
	private ShadowMapMMCRenderer mmcRenderer;
	
	public ShadowMapMaster(Camera camera, DisplayManager mgr) {
		this.shader = new ShadowMapShader();
		this.shadowBox = new ShadowBox(lightViewMatrix, camera, mgr);
		this.shadowFbo = new ShadowMapFBO(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE, mgr);
		mmcRenderer = new ShadowMapMMCRenderer(shader, projectionViewMatrix);
	}
	
	public void render(Map<ModelComposite, List<Transform>> mmcInstances, Light sun) {
		shadowBox.update();
		Vector3f sunPosition = sun.position;
		Vector3f lightDirection = new Vector3f(-sunPosition.x, -sunPosition.y, -sunPosition.z);
		prepare(lightDirection, shadowBox);
		//TODO:here technically can be added more shadow renderers
		mmcRenderer.render(mmcInstances);
		
		finish();
	}
	
	public Matrix4f getToShadowMapSpaceMatrix() {
		return Matrix4f.mul(offset, projectionViewMatrix, null);
	}
	
	public void cleanup() {
		shader.cleanUp();
		shadowFbo.cleanUp();
	}
	
	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}
	
	public Matrix4f getLightSpaceTransform() {
		return lightViewMatrix;
	}
	
	private void prepare(Vector3f lightDirection, ShadowBox box) {
		updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDirection, box.getCenter());
		Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
		shadowFbo.bindFrameBuffer();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shader.start();
	}
	
	private void finish() {
		shader.stop();
		shadowFbo.unbindFrameBuffer();
	}
	
	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
		direction.normalise();
		center.negate();
		lightViewMatrix.setIdentity();
		
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix, lightViewMatrix);
		
		Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
	}
	
	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2f / width;
		projectionMatrix.m11 = 2f / height;
		projectionMatrix.m22 = -2f / length;
		projectionMatrix.m33 = 1;
	}
	
	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
		offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
		return offset;
	}
}
