package com.alkrist.maribel.client.settings;

import java.util.Properties;

public class CoreConfig extends ConfigBase{

	public String username;
	public int port;
	
	protected CoreConfig() {}
	
	@Override
	protected void loadProperties(Properties prop) {
		username = prop.getProperty("username", "Maribel");
		port = Integer.valueOf(prop.getProperty("port", "1331"));
	}

	@Override
	protected void saveProperties(Properties prop) {
		prop.setProperty("username", username);
		prop.setProperty("port", String.valueOf(port));
	}

}
