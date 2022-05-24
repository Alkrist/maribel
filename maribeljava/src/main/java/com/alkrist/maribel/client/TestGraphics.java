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
import com.alkrist.maribel.client.graphics.gui.GUIFrame;
import com.alkrist.maribel.client.graphics.gui.GUIRenderer;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.MatrixMath;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;
import com.alkrist.maribel.utils.math.Vector4f;

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
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		ModelComposite dragon = ModelComposite.loadFromMMC("dragon", loader);
		ModelComposite tent = ModelComposite.loadFromMMC("tent", loader);
		Transform transform = new Transform(new Vector3f(0,-5,-25), new Vector3f(0,0,0), 1);
		Transform transform2 = new Transform(new Vector3f(0,0,-15), new Vector3f(0,20,0), 1);
		Map<ModelComposite, List<Transform>> objects = new HashMap<ModelComposite, List<Transform>>();
		List<Transform> batch = new ArrayList<Transform>();
		List<Transform> batch2 = new ArrayList<Transform>();
		batch.add(transform);
		batch2.add(transform2);
		objects.put(dragon, batch);
		//objects.put(tent, batch2);
		GUIFrame coloredFrame = new GUIFrame(new Vector2f(0,0), new Vector2f(0.25f, 0.25f), new Vector4f(0,1,0,1));
		GUIFrame texturedFrame = new GUIFrame(new Vector2f(-0.5f,-0.5f), new Vector2f(0.25f, 0.25f), "samplegui", new Vector4f(1,0,0,0));
		List<GUIFrame> frames = new ArrayList<GUIFrame>();
		frames.add(texturedFrame);
		frames.add(coloredFrame);		
		Camera camera = new Camera(0,0,0,0,0,0);
		Light light1 = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		Light light2 = new Light(new Vector3f(0,-20,0), new Vector3f(0,1,1), new Vector3f(1,0.1f, 0.002f));
		Light light3 = new Light(new Vector3f(-20,0,0), new Vector3f(0,1,0), new Vector3f(1,0.1f, 0.002f));
		Light light4 = new Light(new Vector3f(0,0,5), new Vector3f(1,0,1), new Vector3f(1,0.1f, 0.002f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
		lights.add(light4);
		
		while(!manager.isCloseRequested()) {
			
			texturedFrame.color.w+=0.001f;
			texturedFrame.color.w = Math.min(texturedFrame.color.w, 1);
			transform.rotation.y +=0.1f;
			camera.move();
			shader.start();
			renderer.prepare();
			shader.loadViewMatrix(MatrixMath.createViewMatrix(camera));
			shader.loadLights(lights);
			renderer.render(objects);
			shader.stop();
			
			//guiRenderer.render(frames);
			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
