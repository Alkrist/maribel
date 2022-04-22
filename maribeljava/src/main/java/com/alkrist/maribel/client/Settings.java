package com.alkrist.maribel.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

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
	public static final Settings CURRENT = new Settings();
	
	private Settings() {}
	
	public String username;
	public int port;
	
	/**
	 * Init singleton, load values from properties file or use defaults.
	 */
	public void load() {
		
		File file = new File(FileUtil.getConfigPath()+"maribel.properties");
		if(!file.exists() || file.isDirectory()) {
			try {
				
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				Logging.getLogger().log(Level.SEVERE, "Properties file not found", e);
			}
		}
		
		try {
			
			FileInputStream fis = new FileInputStream(file);
			Properties prop = new Properties();
			prop.load(fis);
			
			username = prop.getProperty("username", "Maribel");
			port = Integer.valueOf(prop.getProperty("port", "1331"));
			
			fis.close();
			
		} catch (FileNotFoundException e) {
			Logging.getLogger().log(Level.SEVERE, "Properties file not found", e);
		} catch (IOException e) {
			Logging.getLogger().log(Level.SEVERE, "An error occured durning reading from properties file", e);
		}
		
		Logging.getLogger().log(Level.INFO, "Settings loaded.");
	}
	
	/**
	 * Save values to properties file.
	 */
	public void save() {
		File file = new File(FileUtil.getConfigPath()+"maribel.properties");
		if(!file.exists() || file.isDirectory()) {
			try {
				
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				Logging.getLogger().log(Level.SEVERE, "An error occured durning creation of properties file", e);
			}
		}
		
		try {
			
			FileOutputStream fos = new FileOutputStream(file);
			Properties prop = new Properties();
			
			prop.setProperty("username", username);
			prop.setProperty("port", String.valueOf(port));
			
			prop.store(fos, null);
			fos.close();
			
		} catch (FileNotFoundException e) {
			Logging.getLogger().log(Level.SEVERE, "Properties file not found", e);
		} catch (IOException e) {
			Logging.getLogger().log(Level.SEVERE, "An error occured durning wrighting to properties file", e);
		}
		
		Logging.getLogger().log(Level.INFO, "Settings saved.");
	}
}
