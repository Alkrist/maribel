package com.alkrist.maribel.client;

import com.alkrist.maribel.client.graphics.DisplayManager;
import com.alkrist.maribel.utils.Logging;

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
		
		while(!manager.isCloseRequested()) {
			manager.updateWindow();
			System.out.println("works");
		}manager.destroyWindow();
		
		Settings.CURRENT.save();
	}
}
