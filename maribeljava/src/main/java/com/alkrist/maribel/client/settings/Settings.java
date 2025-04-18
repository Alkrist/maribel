package com.alkrist.maribel.client.settings;

import com.alkrist.maribel.client.core.ClientConfig;
import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.core.VideoConfig;

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
	
	public static final ClientConfig CORE = Context.getClientConfig();
	public static final VideoConfig GRAPHICS = Context.getVideoConfig();
	
	public static void load() {
		CORE.load("core");
		//GRAPHICS.load("video");
	}
	
	public static void save() {
		CORE.save("core");
		//GRAPHICS.save("video");
	}
}
