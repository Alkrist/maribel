package com.alkrist.maribel.client;

import java.util.logging.Level;

import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.common.event.events.CleanupEvent;
import com.alkrist.maribel.common.loader.MasterLoader;
import com.alkrist.maribel.utils.Logging;

public class Maribel {

	private static MasterLoader loader;
	
	public static void main(String[] args) {
		loader = new MasterLoader();
		Logging.initLogger();
		
		try {
			
			Settings.CURRENT.load();
			loader.preInit(); //Setup some components needed for loading
			loader.load(); //Background game loading process
			Updater.update(0); //Actual game loop TODO: add deltatime from rendering
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			Logging.getLogger().log(Level.INFO, "Cleanup started.");
			EventManager.callEvent(new CleanupEvent());
			System.gc();
		}
	}
	
}
