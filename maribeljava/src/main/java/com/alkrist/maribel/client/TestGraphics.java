package com.alkrist.maribel.client;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.RenderSystem;
import com.alkrist.maribel.client.graphics.Transform;
import com.alkrist.maribel.client.graphics.model.Model;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing graphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {

	public static void main(String[] args) {
		Logging.initLogger();
		Settings.CURRENT.load();	
		DisplayManager manager = new DisplayManager();
		manager.createWindow("test");
		BufferObjectLoader loader = new BufferObjectLoader();
		
		
		Engine engine = new Engine();
		engine.addSystem(new RenderSystem(manager));
		
		Entity e1 = engine.createEntity();
		Entity e2 = engine.createEntity();
		ModelComposite dragon = ModelComposite.loadFromMMC("dragon", loader);
		ModelComposite tent = ModelComposite.loadFromMMC("tent", loader);
		Transform transform = new Transform(new Vector3f(0,-5,-25), new Vector3f(0,0,0), 1);
		Transform transform2 = new Transform(new Vector3f(0,0,-15), new Vector3f(0,20,0), 1);
		e1.addComponent(new Model(dragon));
		e1.addComponent(transform);
		e2.addComponent(new Model(tent));
		e2.addComponent(transform2);
		
		Light light1 = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		Light light2 = new Light(new Vector3f(0,-20,0), new Vector3f(0,1,1), new Vector3f(1,0.1f, 0.002f));
		Light light3 = new Light(new Vector3f(-20,0,0), new Vector3f(0,1,0), new Vector3f(1,0.1f, 0.002f));
		Light light4 = new Light(new Vector3f(0,0,5), new Vector3f(1,0,1), new Vector3f(1,0.1f, 0.002f));
		Entity e3 = engine.createEntity();
		Entity e4 = engine.createEntity();
		Entity e5 = engine.createEntity();
		Entity e6 = engine.createEntity();
		e3.addComponent(light1);
		e4.addComponent(light2);
		e5.addComponent(light3);
		e6.addComponent(light4);
		
		engine.addEntity(e1);
		engine.addEntity(e2);
		engine.addEntity(e3);
		engine.addEntity(e4);
		engine.addEntity(e5);
		engine.addEntity(e6);
		
		while(!manager.isCloseRequested()) {
			transform.rotation.y +=0.1f;
			Camera.MAIN_CAMERA.move();
			engine.update(manager.deltaTime());
			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
