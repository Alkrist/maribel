package com.alkrist.maribel.client.graphics;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.client.Settings;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.Logging;

public class RenderSystemTest {

	private static DisplayManager manager;
	private static BufferObjectLoader loader;
	private static Engine engine;
	
	@BeforeAll
	public static void initMaribel() {
		Logging.initLogger();
		Settings.CURRENT.load();
		manager = new DisplayManager();
		manager.createWindow("RenderSysTest");
		loader = new BufferObjectLoader();
		engine = new Engine();
		engine.addSystem(new RenderSystem(manager));
		Entity camera = engine.createEntity();
		camera.addComponent(new Camera(0,0,0,0,0,0));
		engine.addEntity(camera);
	}
	
	@Test
	public void testNoEntities() {
		for(int i=0; i<1000; i++) {
			engine.update(manager.deltaTime());
		}
	}
	
	@AfterAll
	public static void unloadMaribel() {
		manager.destroyWindow();
		Settings.CURRENT.save();
	}
}
