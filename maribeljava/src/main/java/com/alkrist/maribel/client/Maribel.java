package com.alkrist.maribel.client;

import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.common.event.events.CleanupEvent;
import com.alkrist.maribel.common.loader.MasterLoader;

public class Maribel {

	private static MasterLoader loader;
	
	public static void main(String[] args) {
		loader = new MasterLoader();
		
		try {
			
			Settings.CURRENT.load();
			loader.preInit(); //Setup some components needed for loading
			loader.load(); //Background game loading process
			Updater.update(); //Actual game loop
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("cleanup");
			EventManager.callEvent(new CleanupEvent());
		}
	}
	
}
