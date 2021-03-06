package com.alkrist.maribel.common.loader;

import java.util.logging.Level;

import com.alkrist.maribel.api.MaribelRegistry;
import com.alkrist.maribel.client.Updater;
import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.common.event.events.InitializationEvent;
import com.alkrist.maribel.common.event.events.PostinitializationEvent;
import com.alkrist.maribel.common.event.events.PreinitializationEvent;
import com.alkrist.maribel.utils.Logging;


/**
 * The Master Loader class maintains the whole application initialization process, should not be modified.
 * @author Mikhail
 */
public final class MasterLoader {
	
	/** Preinit is used for loading:
	 * a) Display
	 * b) Master Engine
	 * c) Loaders
	 * d) Loading screen for loading
	 */
	public void preInit() {
		Logging.getLogger().log(Level.INFO, "PreInit stage...");
		
		//TODO: here go essential registration things like Display init etc.
		//TODO: also set the loading screen
		MaribelRegistry.registerPackets();
		MaribelRegistry.registerListeners();
		EventManager.callEvent(new PreinitializationEvent());
		
		//TODO: fix the loading process so it can be adjusted from api.
		//Updater.setActiveElement(new TestGenericBackground());
		Updater.enable();
	}
	
	/**
	 * Load Thread has 2 parts:
	 * 1) Init - main initialization, utilized for initializing game components:
	 * a) ECS/Events (Systems)
	 * b) Scenes
	 * c) Engines
	 * 
	 * 2) PostInit - fired after the main init method, utilized for game data registry:
	 * a) Meshes
	 * b) Textures
	 * c) Sounds
	 * d) Item registry
	 * 
	 * After PostInit the loading is completed and the menu scene is being activated, after what
	 * the loading thread stops.
	 */
	public void load() {
		new Thread() {
			@Override
			public void run() {
				Thread.currentThread().setName("Loading");
				
				EventManager.callEvent(new InitializationEvent());
				EventManager.callEvent(new PostinitializationEvent());
				//TODO: end loading
			}
		}.start();
	}
}
