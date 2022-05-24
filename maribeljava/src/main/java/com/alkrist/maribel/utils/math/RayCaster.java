package com.alkrist.maribel.utils.math;

import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.DisplayManager;

public class RayCaster {

	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Camera camera;
	
	private Vector3f ray;
	
	public RayCaster(Camera camera, Matrix4f projection) {
		this.camera = camera;
		this.projectionMatrix = projection;
		this.viewMatrix = MatrixMath.createViewMatrix(camera);
	}
	
	public Vector3f getCurrentRay() {
		return ray;
	}
	
	public void setProjectionMatrix(Matrix4f projection) {
		this.projectionMatrix = projection;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
		this.viewMatrix = MatrixMath.createViewMatrix(camera);
	}
	
	public void update(DisplayManager window) {
		this.viewMatrix = MatrixMath.createViewMatrix(camera);
		ray = cast(window.getMousePosition(), window.getWidth(), window.getHeight());
	}
	
	private Vector3f cast(Vector2f mousePos, float width, float height) {
		Vector2f ndc = getNormalizedDeviceCoords(mousePos, width, height);
		Vector4f clipCoords = new Vector4f(ndc.x, ndc.y, -1f, 1f);
		Vector4f eyeCoords = toEyeSpace(clipCoords);
		return toWorldSpace(eyeCoords); 
	}
	
	private Vector3f toWorldSpace(Vector4f eyeSpace) {
		Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedViewMatrix, eyeSpace, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeSpace(Vector4f clipSpace) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipSpace, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector2f getNormalizedDeviceCoords(Vector2f screenPos, float width, float height) {
		float x = (2f * screenPos.x) / width - 1f;
		float y = (2f * screenPos.y) / height - 1f;
		return new Vector2f(x,y);
	}
}
