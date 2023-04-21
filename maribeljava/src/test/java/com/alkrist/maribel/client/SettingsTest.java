package com.alkrist.maribel.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.client.settings.Settings;
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
		Settings.load();
		assertEquals(Settings.CORE.port, 1331);
		assertEquals(Settings.CORE.username, "Maribel");
		
		Settings.CORE.port = 2222;
		Settings.CORE.username = "test";
		
		Settings.save();
		
		Settings.load();
		assertEquals(Settings.CORE.port, 2222);
		assertEquals(Settings.CORE.username, "test");
		
		Settings.save();
	}
	
	@AfterAll
	public static void createDamnConfigFile() {
		Settings.load();
		Settings.save();
	}
}
