package com.alkrist.maribel.client.core;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.glfw.GLFWErrorCallback;

import com.alkrist.maribel.client.MaribelTest;
import com.alkrist.maribel.client.util.Constants;
import com.alkrist.maribel.common.ecs.Engine;

/*
 * This represents a dedicated client engine. Here we actually do all the stuff here. Call render, poll inputs, call client-side
 * update, which checks for pending server-side snapshot of updates.
 */
public class CoreEngine extends Engine{

	private boolean isRunning;
	
	private static float framerate = 1000; //adjust maybe
	private static float frameTime = 1.0f/framerate;
	
	private Window window;
	private Input input;
	private RenderEngine renderEngine;
	private GLFWErrorCallback errorCallback;
	
	// FPS tracking variables
    private int frames;
    private double lastFpsUpdateTime;
    private double fpsUpdateInterval = 1.0d;
    
	private void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		renderEngine = Context.getRenderEngine();
		window = Context.getWindow();
		input = Context.getInput();
		
		input.create(window.getId());
		window.show();
		
		frames = 0;
        lastFpsUpdateTime = System.nanoTime() / Constants.NANOSECOND;
	}
	
	public void start() {
		init();
		if(isRunning) {
			return;
		}

		run();
	}
	
	public void run() {
	    this.isRunning = true;
	    
	    int frames = 0;
	    long frameCounter = 0;
	    
	    long lastTime = System.nanoTime();
	    double unprocessedTime = 0;
	    double delta = 0;
	    
	    // Rendering Loop
	    while(isRunning) {
	        boolean render = false;
	        
	        long currentTime = System.nanoTime();
	        long passedTime = currentTime - lastTime;
	        lastTime = currentTime;
	        
	        unprocessedTime += passedTime / Constants.NANOSECOND;
	        frameCounter += passedTime;
	        delta += passedTime / Constants.NANOSECOND;
	        
	        while(unprocessedTime > frameTime) {
	            render = true;
	            unprocessedTime -= frameTime;
	            
	            if(window.isCloseRequested())
	                stop();
	            
	            update(delta);
	            delta = 0;
	        }
	        
	        if(render) {
	            render();
	            frames++;
	            
	            if(frameCounter >= Constants.NANOSECOND) {
	                System.out.println("FPS: " + frames);
	                frames = 0;
	                frameCounter = 0;
	            }
	        }
	        /*else {
	            try {
	                Thread.sleep(10);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }*/
	    }
	    shutdown();
	}
	
	private void render(){
		renderEngine.render();
		window.draw();
	}
	
	public void update(double delta) {
		input.update();
		super.update(delta);
		// TODO: ECS engine update here
		
		//TEST!!!!!!!
		MaribelTest.cameraTestUpdater(delta);
		
		Context.getCamera().update(delta);
	}
	
	private void stop(){
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	private void shutdown() {
		Context.shutdown();
		// dispose render engine
		errorCallback.free();
		glfwTerminate();
	}
}
