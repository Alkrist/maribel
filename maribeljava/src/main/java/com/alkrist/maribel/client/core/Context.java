package com.alkrist.maribel.client.core;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.AL10.alGetString;
import static org.lwjgl.openal.AL10.AL_VENDOR;
import static org.lwjgl.openal.AL10.AL_VERSION;
import static org.lwjgl.openal.AL10.AL_RENDERER;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import com.alkrist.maribel.client.sound.SoundListener;
import com.alkrist.maribel.client.util.GLUtil;
import com.alkrist.maribel.utils.Logging;

/**
 * A horrible child of rape between oreon-engine Context and my own previous
 * Maribel context
 * 
 * @author Alkrist
 */
public class Context {

	private static ClientConfig clientConfig;
	private static CoreEngine coreEngine;
	
	private static MasterLoader loader;
	
	private static VideoConfig videoConfig;
	private static Window window;
	private static Input input;
	private static Camera camera;
	private static RenderEngine renderEngine;
	
	private static long alContext;
	private static long alDevice;
	private static SoundListener alListener;

	public static void create() {
		
		// Client core
		clientConfig = new ClientConfig();
		coreEngine = new CoreEngine();
		
		loader = new MasterLoader();
		
		// OpenGL
		videoConfig = new VideoConfig();
		input = new Input();
		
		window = new Window("Maribel", videoConfig.width, videoConfig.height);
		camera = new Camera(new Vector3f(0f), new Vector3f(0f), videoConfig.fovY, videoConfig.width, videoConfig.height);
		
		renderEngine = new RenderEngine();
		
		// openAL
		createALContext();
		alListener = new SoundListener();
		
		// GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		window.create();
		
		GLUtil.init();
		renderEngine.init();
	}
	
	public static void shutdown() {
		
		// OpenAL
		disposeALContext();
		
		// OpenGL
		window.dispose();
		videoConfig.save();
		input.shutdown();
		
		// resources
		loader.dispose();
		
		// client core
		clientConfig.save();
	}
	
	private static void createALContext() {
	    alDevice = alcOpenDevice((ByteBuffer) null);
	    if (alDevice == NULL) {
	        throw new IllegalStateException("Failed to open the default OpenAL device.");
	    }

	    ALCCapabilities deviceCaps = ALC.createCapabilities(alDevice);
	    alContext = alcCreateContext(alDevice, (IntBuffer) null);
	    if (alContext == NULL) {
	        throw new IllegalStateException("Failed to create OpenAL context.");
	    }
	    alcMakeContextCurrent(alContext);
	    AL.createCapabilities(deviceCaps);

	    // OpenAL/ALC details
	    String defaultDevice = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
	    StringBuilder sb = new StringBuilder();
	    sb.append("\n");
	    sb.append("=== OpenAL Specs ===").append("\n");
	    sb.append("Default OpenAL Device: " + defaultDevice).append("\n");
	    sb.append("Vendor: " + alGetString(AL_VENDOR)).append("\n");
	    sb.append("Version: " + alGetString(AL_VERSION)).append("\n");
	    sb.append("Renderer: " + alGetString(AL_RENDERER)).append("\n");
	    sb.append("====================");
	    Logging.getLogger().log(Level.INFO, sb.toString());
	}
	
	private static void disposeALContext() {
		if (alContext != NULL) {
            alcDestroyContext(alContext);
        }
        if (alDevice != NULL) {
            alcCloseDevice(alDevice);
        }
        
	}
	
	public static VideoConfig getVideoConfig() {
		return videoConfig;
	}
	
	public static ClientConfig getClientConfig() {
		return clientConfig;
	}
	
	public static MasterLoader getMasterLoader() {
		return loader;
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
	
	public static SoundListener getSoundListener() {
		return alListener;
	}

	public static RenderEngine getRenderEngine() {
		return renderEngine;
	}
	
	public static CoreEngine getCoreEngine() {
		return coreEngine;
	}
}
