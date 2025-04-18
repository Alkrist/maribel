package com.alkrist.maribel.client.core;
import static org.lwjgl.glfw.GLFW.glfwInit;

import org.joml.Vector3f;

import com.alkrist.maribel.client.util.GLUtil;

/**
 * A horrible child of rape between oreon-engine Context and my own previous
 * Maribel context
 * 
 * @author Alkrist
 */
public class Context {

	private static ClientConfig clientConfig;
	private static VideoConfig videoConfig;
	private static Window window;
	private static Input input;
	private static Camera camera;
	private static RenderEngine renderEngine;
	
	public static void create() {
		
		// Client core
		clientConfig = new ClientConfig();
		
		// TODO: init Core engine which will be inherited from ECS engine (for client, it will include render engine)
		
		// OpenGL
		videoConfig = new VideoConfig();
		input = new Input();
		
		window = new Window("Maribel", videoConfig.width, videoConfig.height);
		camera = new Camera(new Vector3f(0f), new Vector3f(0f), videoConfig.fovY, videoConfig.width, videoConfig.height);
		
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		window.create();
		
		GLUtil.init();
	}
	
	public static void shutdown() {
		// OpenGL
		window.dispose();
		videoConfig.save();
		
		// client core
		clientConfig.save();
	}
	
	public static VideoConfig getVideoConfig() {
		return videoConfig;
	}
	
	public static ClientConfig getClientConfig() {
		return clientConfig;
	}
	
	public static Input getInput() {
		return input;
	}
	
	public static Window getWindow() {
		return window;
	}
	
	public static Camera getCamera() {
		return camera;
	}
	
	public static void setRenderEngine(RenderEngine theRenderEngine) {
		renderEngine = theRenderEngine;
	}
	
	public RenderEngine getRenderEngine() {
		return renderEngine;
	}
}
