package com.alkrist.maribel.client;

import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.InputHandler;
import com.alkrist.maribel.client.graphics.ModelCompositeRenderer;
import com.alkrist.maribel.client.graphics.model.Mesh;
import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.MatrixMath;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing grphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {

	
	public static void main(String[] args) {
		float[] vertices = {
			    -0.5f, 0.5f, 0f,
			    -0.5f, -0.5f, 0f,
			    0.5f, -0.5f, 0f,
			    0.5f, 0.5f, 0f
			  };
		
		int[] indices = {
				0,1,3,
				3,1,2
		};

		float[] textureCoords = {
			0,0,
			0,1,
			1,1,
			1,0
		};
		Logging.initLogger();
		Settings.CURRENT.load();	
		DisplayManager manager = new DisplayManager();
		manager.createWindow("test");
		ModelShader shader = new ModelShader();
		ModelCompositeRenderer renderer = new ModelCompositeRenderer(shader, MatrixMath.createProjectionMatrix(manager.getWidth(), manager.getHeight()));
		BufferObjectLoader loader = new BufferObjectLoader();
		Texture texture = Texture.loadTexture("test");
		Mesh mesh = loader.loadToVAO(vertices, textureCoords, indices);
		
		
		while(!manager.isCloseRequested()) {
			/*shader.start();
			renderer.prepare();
			renderer.render(mesh, texture);
			shader.stop();*/

			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
