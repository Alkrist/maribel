package com.alkrist.maribel.client.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.alkrist.maribel.client.Settings;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.Vector2f;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private int width;
	private int height;

	private long window;

	private double lastFrameTime;
	private double deltaTime;

	// TODO: fullscreen - windowed - windowed fullscreen translation
	public void createWindow(String title) {

		if (!GLFW.glfwInit()) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to init GLFW!");
			System.exit(-1);
		}

		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, title,
				Settings.CURRENT.fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

		if (window == 0) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to crete window!");
			System.exit(-1);
		}
        
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);

			width = pWidth.get(0);
			height = pHeight.get(0);
		}
        
		// Resize callback - happen on resize
		GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
			@Override
			public void invoke(final long window, final int w, final int h) {
				height = h;
				width = w;
				// TODO: event call for everything that has to be reshaped
			}
		});

		// If vertical synchronization enabled, set fps cap to 60.
		if (Settings.CURRENT.vsyncEnabled) {
			GLFW.glfwSwapInterval(1);
		}
        
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
		InputHandler.init(window);

		GL.createCapabilities();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glViewport(0, 0, width, height);
	}
	
	/**
	 * 
	 * @return current display width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return current display height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * @return delta time taken for one frame in seconds
	 */
	public double deltaTime() {
		return deltaTime;
	}

	/**
	 * If enable requested and settings are set to false - enable; if settings
	 * already true - ignore
	 * 
	 * If disable requested and settings are set to true - disable; if settings
	 * already false - ignore
	 * 
	 * @param enabled - enable request
	 */
	public void toggleVSync(boolean enabled) {
		if (enabled && !Settings.CURRENT.vsyncEnabled) {
			GLFW.glfwSwapInterval(1);
			Settings.CURRENT.vsyncEnabled = true;
		} else if (!enabled && Settings.CURRENT.vsyncEnabled) {
			GLFW.glfwSwapInterval(0);
			Settings.CURRENT.vsyncEnabled = false;
		}
	}

	public void updateWindow() {
		InputHandler.update();

		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();

		// Delta time measurement code
		double currentFrameTime = getCurrentTimeMillis();
		deltaTime = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void destroyWindow() {
		GLFW.glfwDestroyWindow(window);
	}

	private static double getCurrentTimeMillis() {
		return GLFW.glfwGetTime() * 1000; // getTime gives time in seconds, *1000 for ms
	}

	/**
	 * 
	 * @return current mouse position on the screen coordinates (x,y)
	 */
	public Vector2f getMousePosition() {
		DoubleBuffer bfX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer bfY = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, bfX, bfY);
		float x = (float) bfX.get(0);
		float y = (float) bfY.get(0);
		return new Vector2f(x, y);
	}
}
