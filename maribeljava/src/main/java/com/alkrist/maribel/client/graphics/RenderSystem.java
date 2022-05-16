package com.alkrist.maribel.client.graphics;

import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;

public class RenderSystem extends SystemBase{

	private DisplayManager window;
	
	//TODO: must have link to all entity things
	private ModelCompositeRenderer modelRenderer;
	private ModelShader shader;
	
	private Matrix4f projectionMatrix;
	
	public RenderSystem(DisplayManager manager) {
		super();
		window = manager;
		
		shader = new ModelShader();
		Matrix4f projectionMatrix = MatrixMath.createProjectionMatrix(window.getWidth(), window.getHeight());
		modelRenderer = new ModelCompositeRenderer(shader, projectionMatrix);
	}
	
	@Override
	public void update(double deltaTime) {
		
		
	}
	
	private void render() {
		prepare();
		//TODO: implement
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//TODO: set bg color
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
