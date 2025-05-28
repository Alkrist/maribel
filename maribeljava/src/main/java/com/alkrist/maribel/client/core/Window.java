package com.alkrist.maribel.client.core;

import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL30.GL_NUM_EXTENSIONS;
import static org.lwjgl.opengl.GL40.GL_MAX_UNIFORM_BUFFER_BINDINGS;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.WGLEXTSwapControl;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.Logging;

public class Window {

	private long id;
	private int width;
	private int height;
	private String title;
	
	protected Window(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void create() {
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);	
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);	
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);	
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);	
		
		
		// full screen
		if (Context.getVideoConfig().fullscreen) {
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			setId(glfwCreateWindow(vidmode.width(), vidmode.height(), 
					title, glfwGetPrimaryMonitor(), 0));
		}
		
		// windowed
		else {
			setId(glfwCreateWindow(getWidth(), getHeight(), getTitle(), 0, 0));
		}
			
		if(getId() == 0) {
		    throw new RuntimeException("Failed to create window");
		}
		
		setIcon(FileUtils.getResourceLocation("textures/system/icon32.png"));
		
		glfwMakeContextCurrent(getId());
		
		glfwSwapInterval(0);
		
		if (Context.getVideoConfig().vsyncEnabled){
			WGLEXTSwapControl.wglSwapIntervalEXT(1);
			glfwSwapInterval(1);
		}
		
		GL.createCapabilities();
		
		printHardwareInfo();
	}
	
	public void show() {
		glfwShowWindow(getId());
	}
	
	public void draw() {
		glfwSwapBuffers(getId());
	}
	
	public void dispose() {
		glfwDestroyWindow(getId());
	}
	
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(getId());
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long window) {
		this.id = window;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void resize(int width, int height) {
		glfwSetWindowSize(getId(), width, height);
		setHeight(height);
		setWidth(width);
		Context.getVideoConfig().width = width;
		Context.getVideoConfig().height = height;
		// TODO set camera projection
	}
	
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
	
	private void printHardwareInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
	    sb.append("=== Hardware Information ===\n");
	    sb.append("OpenGL Version: ").append(glGetString(GL_VERSION)).append("\n");
	    sb.append("GLSL Version: ").append(glGetString(GL_SHADING_LANGUAGE_VERSION)).append("\n");
	    sb.append("Vendor: ").append(glGetString(GL_VENDOR)).append("\n");
	    sb.append("Renderer: ").append(glGetString(GL_RENDERER)).append("\n");
	    
	    // Get extensions count
	    IntBuffer numExtensions = MemoryUtil.memAllocInt(1);
	    glGetIntegerv(GL_NUM_EXTENSIONS, numExtensions);
	    sb.append("Available Extensions: ").append(numExtensions.get(0)).append("\n");
	    MemoryUtil.memFree(numExtensions);
	    
	    // Get maximum texture size
	    IntBuffer maxTextureSize = MemoryUtil.memAllocInt(1);
	    glGetIntegerv(GL_MAX_TEXTURE_SIZE, maxTextureSize);
	    sb.append("Max Texture Size: ").append(maxTextureSize.get(0)).append("\n");
	    MemoryUtil.memFree(maxTextureSize);
	    
	    // Get maximum uniform buffer bindings
	    IntBuffer maxUBOBindings = MemoryUtil.memAllocInt(1);
	    glGetIntegerv(GL_MAX_UNIFORM_BUFFER_BINDINGS, maxUBOBindings);
	    sb.append("Max UBO Bindings: ").append(maxUBOBindings.get(0)).append("\n");
	    MemoryUtil.memFree(maxUBOBindings);
	    
	    sb.append("============================");
	    Logging.getLogger().log(Level.INFO, sb.toString());
	}
}
