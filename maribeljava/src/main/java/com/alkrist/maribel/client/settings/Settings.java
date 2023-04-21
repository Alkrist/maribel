package com.alkrist.maribel.client.settings;

import com.alkrist.maribel.graphics.context.GraphicsConfig;

/**
 * Singleton class.
 * Loads the parameters for the game engine, if they don't exist, loads defaults.
 * 
 * The configuration handling is performed as follows:
 * Load Settings/Store Defaults -> Run Game -> Save Current Settings
 * 
 * @author Mikhail
 *
 */
public class Settings {
	
	public static final CoreConfig CORE = new CoreConfig();
	public static final GraphicsConfig GRAPHICS = new GraphicsConfig();
	
	public static void load() {
		CORE.load("core");
		//GRAPHICS.load("video");
	}
	
	public static void save() {
		CORE.save("core");
		//GRAPHICS.save("video");
	}
}
