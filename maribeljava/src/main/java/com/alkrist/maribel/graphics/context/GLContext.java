package com.alkrist.maribel.graphics.context;

import java.util.logging.Level;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.graphics.platform.GLUtil;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.platform.InputHandler;
import com.alkrist.maribel.utils.Logging;

public class GLContext {

	private static GLWindow window;
	private static GraphicsConfig config;
	private static InputHandler inputHandler;
	private static Camera camera;
	
	private static Engine engine;
	
	public static void create(String windowTitle, String iconPath) {
		config = new GraphicsConfig();
		window = new GLWindow();
		inputHandler = new InputHandler();
		camera = new Camera(new Vector3f(0), 0, 0, 0);
		
		config.load("video");
		window.init(windowTitle, iconPath);
		inputHandler.create(window.getId());
		
		camera.init();
		
		Logging.getLogger().log(Level.INFO, "OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		GLUtil.init();
		
		window.show();
	}
	
	public static void finish() {
		config.save("video");
	}
	
	public static GLWindow getWindow() {
		return window;
	}
	
	public static GraphicsConfig getConfig() {
		return config;
	}
	
	public static InputHandler getInput() {
		return inputHandler;
	}
	
	public static Engine getEngine() {
		return engine;
	}
	
	public static void setEngine(Engine e) {
		engine = e;
	}
	
	public static Camera getMainCamera() {
		return camera;
	}
}
