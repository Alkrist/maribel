package com.alkrist.maribel.client.graphics;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.utils.Logging;

public class RenderSystemTest {

	private static GLWindow manager;
	private static ResourceLoader loader;
	private static Engine engine;
	
	@BeforeAll
	public static void initMaribel() {
		Logging.initLogger();
		Settings.load();
		manager = new GLWindow();
		manager.init("RenderSysTest", "system\\icon32");
		loader = new ResourceLoader();
		engine = new Engine();
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
		Settings.save();
	}
}
