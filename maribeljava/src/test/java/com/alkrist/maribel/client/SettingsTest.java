package com.alkrist.maribel.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

public class SettingsTest {

	@BeforeAll
	public static void init() {
		Logging.initLogger();
	}
	
	@AfterEach
	public void removePropertiesFile() {
		File file = new File(FileUtil.getConfigPath()+"maribel.properties");
		if(file.exists() && !file.isDirectory()) file.delete();
	}
	
	@Test
	public void settingsLoadTest() {
		Settings.CURRENT.load();
		assertEquals(Settings.CURRENT.port, 1331);
		assertEquals(Settings.CURRENT.username, "Maribel");
		
		Settings.CURRENT.port = 2222;
		Settings.CURRENT.username = "test";
		
		Settings.CURRENT.save();
		
		Settings.CURRENT.load();
		assertEquals(Settings.CURRENT.port, 2222);
		assertEquals(Settings.CURRENT.username, "test");
		
		Settings.CURRENT.save();
	}
}
