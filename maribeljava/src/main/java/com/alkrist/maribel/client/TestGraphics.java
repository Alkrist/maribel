package com.alkrist.maribel.client;

import java.io.File;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.Renderer;
import com.alkrist.maribel.client.graphics.model.Mesh;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

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

		Logging.initLogger();
		Settings.CURRENT.load();	
		DisplayManager manager = new DisplayManager();
		manager.createWindow("test");
		
		Renderer renderer = new Renderer();
		BufferObjectLoader loader = new BufferObjectLoader();
		Mesh mesh = loader.loadToVAO(vertices, indices);
		
		while(!manager.isCloseRequested()) {
			renderer.prepare();
			renderer.render(mesh);
			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
