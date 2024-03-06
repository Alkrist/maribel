package com.alkrist.maribel.graphics.platform;

import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

import org.joml.Vector2f;
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

import com.alkrist.maribel.graphics.context.Camera;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.context.GraphicsConfig;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

/**
 * This class represents the game window. Projection matrix is created here as
 * well as all resize events.
 * 
 * @author Alkrist
 *
 */
public class GLWindow {

	private GraphicsConfig config;
	private Camera mainCamera;
	
	// window sizes for fullscreen and windowed
	private static int fullscreen_width;
	private static int fullscreen_height;
	private static int windowed_width;
	private static int windowed_height;
	
	// Current window size
	private int width;
	private int height;

	// Window title
	private String title;

	// Path to the window icon
	private String iconPath;

	// Window id
	private long window = 0;

	// Time-related variables
	private double lastFrameTime;
	private double deltaTime;

	/**
	 * Called at the beginning, when the GL context is created. Creates a window.
	 * 
	 * @param title    - window title
	 * @param iconPath - path to the window icon. icon must have resolution as power
	 *                 of 2 and <= 32 pixels
	 */
	public void init(String title, String iconPath) {

		this.config = GLContext.getConfig();
		this.mainCamera = GLContext.getMainCamera();
		
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
	 * @return window id
	 */
	public long getId() {
		return window;
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
		if (enabled && !config.vsyncEnabled) {
			GLFW.glfwSwapInterval(1);
			config.vsyncEnabled = true;
		} else if (!enabled && config.vsyncEnabled) {
			GLFW.glfwSwapInterval(0);
			config.vsyncEnabled = false;
		}
	}

	/**
	 * Update the window.
	 */
	public void updateWindow() {
		//InputHandlerOld.update();

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

	/**
	 * Show the window.
	 */
	public void show() {
		GLFW.glfwShowWindow(window);
	}
	
	/**
	 * Switch between windowed and fullscreen. The current state is saved in config.
	 */
	public void switchWindowMode() {
		if (config.fullscreen) {
			config.fullscreen = false;
			GLFW.glfwSetWindowMonitor(window, 0, 0, 0, windowed_width, windowed_height, GLFW.GLFW_DONT_CARE);
			GLFW.glfwSetWindowPos(window, (fullscreen_width - windowed_width) / 2,
					(fullscreen_height - windowed_height) / 2);
		}

		else {
			config.fullscreen = true;
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, fullscreen_width, fullscreen_height,
					GLFW.GLFW_DONT_CARE);
		}
	}

	/**
	 * 
	 * @return current mouse position on the screen coordinates (x,y)
	 */
	protected Vector2f getMousePosition() {
		DoubleBuffer bfX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer bfY = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, bfX, bfY);
		float x = (float) bfX.get(0);
		float y = (float) bfY.get(0);
		return new Vector2f(x, y);
	}
	
	/**
	 * Creates a new window in either windowed or fullscreen (primary monitor)
	 * modes.
	 */
	private void createWindow() {
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

		fullscreen_width = vidmode.width();
		fullscreen_height = vidmode.height();

		windowed_width = config.width;
		windowed_height = config.height;

		if (config.fullscreen) {
			window = GLFW.glfwCreateWindow(fullscreen_width, fullscreen_height, title, GLFW.glfwGetPrimaryMonitor(), 0);
		} else {
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
				mainCamera.setProjectionMatrix(config.fovY, width, height, config.NEAR_PLANE, config.FAR_PLANE);
			}
		});

		GLFW.glfwMakeContextCurrent(window);

		setSwapInterval();

		//InputHandlerOld.init(this);

		GL.createCapabilities();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glViewport(0, 0, width, height);
		mainCamera.setProjectionMatrix(config.fovY, width, height, config.NEAR_PLANE, config.FAR_PLANE);
	}

	/**
	 * Sets the swap interval to either unlimited or if vertical sync enabled, to 60
	 * fps.
	 */
	private void setSwapInterval() {
		// If vertical synchronization enabled, set fps cap to 60.
		GLFW.glfwSwapInterval(0);
		if (config.vsyncEnabled) {
			WGLEXTSwapControl.wglSwapIntervalEXT(1);
		}
	}

	/**
	 * Sets the window icon.
	 * 
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

	private static double getCurrentTimeMillis() {
		return GLFW.glfwGetTime() * 1000; // getTime gives time in seconds, *1000 for ms
	}
}
