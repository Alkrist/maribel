package com.alkrist.maribel.client;

import java.util.logging.Level;
import com.alkrist.maribel.utils.Logging;

/**
 * This class is WIP, holds the data that is red from the config file upon loading, it has
 * the default values for the application
 * @author Mikhail
 *
 */
public class Settings {
	public static final Settings CURRENT = new Settings();
	
	private Settings() {}
	
	public String username;
	public int port;
	
	public void load() {

		username = "test";
		port = 2222;
		Logging.getLogger().log(Level.INFO, "Settings loaded.");
	}
}
