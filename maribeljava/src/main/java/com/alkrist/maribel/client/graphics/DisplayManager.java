package com.alkrist.maribel.client.graphics;

import java.nio.IntBuffer;
import java.util.logging.Level;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import com.alkrist.maribel.client.Settings;
import com.alkrist.maribel.utils.Logging;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private long window;
	

	public void createWindow(String title) {
		
		if(!GLFW.glfwInit()) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to init GLFW!");
			System.exit(-1);
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, title, 0, 0);
		if(window == 0) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to crete window!");
			System.exit(-1);
		}
		
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		
		//If vertical synchronization enabled, set fps cap to 60.
		if(Settings.CURRENT.vsyncEnabled) {
			GLFW.glfwSwapInterval(1);
		}
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
	}
	
	public void updateWindow() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void destroyWindow() {
		GLFW.glfwDestroyWindow(window);
	}
}
