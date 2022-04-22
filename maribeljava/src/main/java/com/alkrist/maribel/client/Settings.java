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
 * This class is WIP, holds the data that is red from the properties file upon loading, it has
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
