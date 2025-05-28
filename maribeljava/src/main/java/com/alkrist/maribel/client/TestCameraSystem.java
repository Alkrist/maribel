package com.alkrist.maribel.client;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.client.core.Camera;
import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.core.Input;
import com.alkrist.maribel.client.sound.SoundBuffer;
import com.alkrist.maribel.client.sound.SoundSource;
import com.alkrist.maribel.common.ecs.SystemBase;

public class TestCameraSystem extends SystemBase {

	private static final float MOVEMENT_SPEED = 3.0f; // units per second
    private static final float ROTATION_SPEED = 90.0f; // degrees per second
    
	private Input input;
	private Camera camera;
	
	private SoundSource bibaSource;
	
	public TestCameraSystem(SoundBuffer sampleSound) {
		input = Context.getInput();
		camera = Context.getCamera();
		
		this.bibaSource = new SoundSource(false, false);
		
		bibaSource.setPosition(new Vector3f(0, -1, -5));
		bibaSource.setBuffer(sampleSound.getBufferId());
	}
	
	@Override
	public void update(double deltaTime) {
        // Convert deltaTime to float and multiply by speed factors
        float delta = (float)deltaTime;
        
        // Mouse rotation (only when mouse is locked)
        if (input.isMouseLocked()) {
            Vector2f mouseDelta = input.getMouseDelta();
            
            // Apply yaw (horizontal movement)
            camera.rotateLocal(0, -mouseDelta.x * delta * ROTATION_SPEED, 0);
            
            // Apply pitch (vertical movement)
            camera.rotateLocal(mouseDelta.y * delta * ROTATION_SPEED, 0, 0);
        }

        // Keyboard movement
        if (input.isKeyHolding(GLFW.GLFW_KEY_E)) {
            camera.rotateLocal(0, 0, -delta * ROTATION_SPEED * 0.01f);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_Q)) {
            camera.rotateLocal(0, 0, delta * ROTATION_SPEED * 0.01f);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_W)) {
            camera.moveLocal(0, 0, -delta * MOVEMENT_SPEED);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_S)) {
            camera.moveLocal(0, 0, delta * MOVEMENT_SPEED);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_A)) {
            camera.moveLocal(-delta * MOVEMENT_SPEED, 0, 0);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_D)) {
            camera.moveLocal(delta * MOVEMENT_SPEED, 0, 0);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_SPACE)) {
            camera.moveLocal(0, delta * MOVEMENT_SPEED, 0);
        }
        
        if(input.isKeyHolding(GLFW.GLFW_KEY_C)) {
            camera.moveLocal(0, -delta * MOVEMENT_SPEED, 0);
        }
        
        if (input.isKeyPushed(GLFW.GLFW_KEY_V)) {
        	if(!bibaSource.isPlaying()) {
        		bibaSource.play();
        	}  
        }
        
        Vector3f position = camera.getPosition();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Context.getSoundListener().setPosition(position);
        
        Vector3f at = new Vector3f();
        viewMatrix.positiveZ(at).negate();
        Vector3f up = new Vector3f();
        viewMatrix.positiveY(up);
        
        Context.getSoundListener().setOrientation(at, up);
    }

}
