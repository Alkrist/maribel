package com.alkrist.maribel.client.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.Logging;

public abstract class ConfigBase {

public void load(String filename) {
		
		File file = new File(FileUtils.getConfigLocation(filename+".properties"));
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
			
			loadProperties(prop);
			
			fis.close();
			
		} catch (FileNotFoundException e) {
			Logging.getLogger().log(Level.SEVERE, "Properties file not found", e);
		} catch (IOException e) {
			Logging.getLogger().log(Level.SEVERE, "An error occured durning reading from properties file", e);
		}
		
		Logging.getLogger().log(Level.INFO, "Settings loaded.");
	}

public void save(String filename) {
	File file = new File(FileUtils.getConfigLocation(filename+".properties"));
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
		
		saveProperties(prop);
		
		prop.store(fos, null);
		fos.close();
		
	} catch (FileNotFoundException e) {
		Logging.getLogger().log(Level.SEVERE, "Properties file not found", e);
	} catch (IOException e) {
		Logging.getLogger().log(Level.SEVERE, "An error occured durning wrighting to properties file", e);
	}
	
	Logging.getLogger().log(Level.INFO, "Settings saved.");
}

	protected abstract void loadProperties(Properties prop);
	
	protected abstract void saveProperties(Properties prop);
}
