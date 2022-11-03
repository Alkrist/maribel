package com.alkrist.maribel.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.client.audio.AudioManager;
import com.alkrist.maribel.client.audio.AudioSource;
import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.client.graphics.InputHandler;
import com.alkrist.maribel.client.graphics.Light;
import com.alkrist.maribel.client.graphics.RenderSystem;
import com.alkrist.maribel.client.graphics.Transform;
import com.alkrist.maribel.client.graphics.gui.GUIFrame;
import com.alkrist.maribel.client.graphics.gui.GUIRenderer;
import com.alkrist.maribel.client.graphics.model.Model;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.particles.ParticleEffect;
import com.alkrist.maribel.client.graphics.particles.ParticleSystem;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.Logging;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;
import com.alkrist.maribel.utils.math.Vector4f;

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
		engine.addSystem(new ParticleSystem());
		engine.addSystem(new RenderSystem(manager, loader));
		Entity e1 = engine.createEntity();
		Entity e2 = engine.createEntity();
		//ModelComposite dragon = ModelComposite.loadFromMMC("dragon", loader);
		//ModelComposite tent = ModelComposite.loadFromMMC("tent", loader);
		ModelComposite dog = ModelComposite.loadFromJson("doxie", loader);
		ModelComposite boulder = ModelComposite.loadFromJson("boulder", loader);
		//ModelComposite fern1 = ModelComposite.loadFromJson("fern", loader);
		//fern1.getNode("fern").getTexture().setNumberOfRows(2);
		//fern1.getNode("fern").setTextureOffsetIndex(1);
		//ModelComposite fern2 = ModelComposite.loadFromJson("fern", loader);
		//fern2.getNode("fern").setTextureOffsetIndex(0);
		Transform transform = new Transform(new Vector3f(0,-10,-20), new Vector3f(0,0,0), 1);
		Transform transform2 = new Transform(new Vector3f(20,-10,-20), new Vector3f(0,90,0), 1);
		e1.addComponent(new Model(dog));
		e1.addComponent(transform);
		//e2.addComponent(new Model(fern1));
		e2.addComponent(transform2);
		
		//******* PARTICLE TEST *******//
		ParticleEffect pEffect = new ParticleEffect(Texture.loadTexture("particles\\sparks", 8), 50, 10, 0, 1, new Vector3f(20, 0, 0), 1);
		Entity e7 = engine.createEntity();
		e7.addComponent(pEffect);
		//*****************************//
		
		//Light light1 = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		Light light1 = new Light(new Vector3f(0,10000,0), new Vector3f(1,1,1));
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
		//engine.addEntity(e2);
		engine.addEntity(e3);
		//engine.addEntity(e4);
		//engine.addEntity(e5);
		//engine.addEntity(e6);
		engine.addEntity(e7);
		
		
		// ***** GUI render alpha test ***** //
		GUIRenderer guiRenderer = new GUIRenderer(loader, manager);
		GUIFrame testGui = new GUIFrame(new Vector4f(0,1,0,1), new Vector2f(0.7f,0.7f), new Vector2f(0.2f, 0.2f), 0.5f, new Vector4f(0,0,0,1), 0.1f);
		List<GUIFrame> frameList = new ArrayList<GUIFrame>();
		frameList.add(testGui);
		
		while(!manager.isCloseRequested()) {
			
			//transform.rotation.y +=0.02f;
			Camera.MAIN_CAMERA.move();
			engine.update(manager.deltaTime());
			guiRenderer.render(frameList);
			manager.updateWindow();
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
