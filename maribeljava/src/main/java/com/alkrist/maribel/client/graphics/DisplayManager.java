package com.alkrist.maribel.client.graphics;

import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.WGLEXTSwapControl;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;
import com.alkrist.maribel.utils.math.Vector2f;

public class DisplayManager {

	private static int fullscreen_width;
	private static int fullscreen_height;
	private static int windowed_width;
	private static int windowed_height;
	
	private int width;
	private int height;
	private String title;
	private String iconPath;

	private long window = 0;
	
	private double lastFrameTime;
	private double deltaTime;

	private Matrix4f projectionMatrix;

	public void init(String title, String iconPath) {

		this.title = title;
		this.iconPath = iconPath;

		if (!GLFW.glfwInit()) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to init GLFW!");
			System.exit(-1);
		}

		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		createWindow();
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

	/**
	 * Update the window.
	 */
	public void updateWindow() {
		InputHandler.update();

		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
		// Delta time measurement code
		double currentFrameTime = getCurrentTimeMillis();
		deltaTime = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	/**
	 * 
	 * @return window should be closed.
	 */
	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(window);
	}

	/**
	 * Close the window.
	 */
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
	
	/**
	 * Switch between windowed and fullscreen. The current state is saved in config.
	 */
	public void switchWindowMode() {
		if(Settings.CURRENT.fullscreen) {
			Settings.CURRENT.fullscreen = false;
			GLFW.glfwSetWindowMonitor(window, 0, 0, 0, windowed_width, windowed_height, GLFW.GLFW_DONT_CARE);
			GLFW.glfwSetWindowPos(window, (fullscreen_width - windowed_width) / 2,
					(fullscreen_height - windowed_height) / 2);
		}
			
		else {
			Settings.CURRENT.fullscreen = true;
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, fullscreen_width, fullscreen_height, GLFW.GLFW_DONT_CARE);
		}
	}
	
	/**
	 * Creates a new window in either windowed or fullscreen (primary monitor) modes.
	 */
	private void createWindow() {
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

		fullscreen_width = vidmode.width();
		fullscreen_height = vidmode.height();
		
		windowed_width = Settings.CURRENT.width;
		windowed_height = Settings.CURRENT.height;
		
		if (Settings.CURRENT.fullscreen) {
			window = GLFW.glfwCreateWindow(fullscreen_width, fullscreen_height, title, GLFW.glfwGetPrimaryMonitor(), 0);
		}else {
			window = GLFW.glfwCreateWindow(windowed_width, windowed_height, title, 0, 0);
		}

		if (window == 0) {
			Logging.getLogger().log(Level.SEVERE, "[GLFW]: Failed to crete window!");
			System.exit(-1);
		}
		
		setIcon(FileUtil.getTexturesPath() + iconPath + ".png");
		
		// Set window resize callback
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			
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
				GL11.glViewport(0, 0, width, height);
				projectionMatrix = MatrixMath.createProjectionMatrix(width, height);
				EventManager.callEvent(new WindowResizeEvent(projectionMatrix, width, height));
			}
		});

		GLFW.glfwMakeContextCurrent(window);
		
		setSwapInterval();
		
		GLFW.glfwShowWindow(window);
		InputHandler.init(window);

		GL.createCapabilities();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glViewport(0, 0, width, height);
		projectionMatrix = MatrixMath.createProjectionMatrix(width, height);
	}

	/**
	 * Sets the window icon.
	 * @param path - path, including the root and format
	 */
	private void setIcon(String path) {
		ByteBuffer bufferedImage;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			bufferedImage = STBImage.stbi_load(path, w, h, comp, 4);
		}
		GLFWImage image = GLFWImage.malloc();

		image.set(32, 32, bufferedImage);

		GLFWImage.Buffer images = GLFWImage.malloc(1);
		images.put(0, image);

		glfwSetWindowIcon(getId(), images);
	}

	/**
	 * Sets the swap interval to either unlimited or if vertical sync enabled, 
	 * to 60 fps.
	 */
	private void setSwapInterval() {
		// If vertical synchronization enabled, set fps cap to 60.
		GLFW.glfwSwapInterval(0);
		if (Settings.CURRENT.vsyncEnabled) {
			WGLEXTSwapControl.wglSwapIntervalEXT(1);
		}
	}
	
	public long getId() {
		return window;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
