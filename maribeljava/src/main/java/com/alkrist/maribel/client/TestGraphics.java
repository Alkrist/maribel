package com.alkrist.maribel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.ModelCompositeRenderer;
import com.alkrist.maribel.client.graphics.Transform;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.MatrixMath;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing grphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {
	
	public static void main(String[] args) {
		Logging.initLogger();
		Settings.CURRENT.load();	
		DisplayManager manager = new DisplayManager();
		manager.createWindow("test");
		ModelShader shader = new ModelShader();
		ModelCompositeRenderer renderer = new ModelCompositeRenderer(shader, MatrixMath.createProjectionMatrix(manager.getWidth(), manager.getHeight()));
		BufferObjectLoader loader = new BufferObjectLoader();
		ModelComposite stall = ModelComposite.loadFromJson("sample", loader);
		ModelComposite dragon = ModelComposite.loadFromMMC("dragon", loader);
		Transform transform = new Transform(new Vector3f(0,-5,-25), new Vector3f(0,0,0), 1);
		Map<ModelComposite, List<Transform>> objects = new HashMap<ModelComposite, List<Transform>>();
		List<Transform> batch = new ArrayList<Transform>();
		batch.add(transform);
		objects.put(dragon, batch);
		Camera camera = new Camera(0,0,0,0,0,0);
		Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		while(!manager.isCloseRequested()) {
			transform.rotation.y +=0.1f;
			camera.move();
			shader.start();
			renderer.prepare();
			shader.loadViewMatrix(MatrixMath.createViewMatrix(camera));
			shader.loadLight(light);
			renderer.render(objects);
			shader.stop();

			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
