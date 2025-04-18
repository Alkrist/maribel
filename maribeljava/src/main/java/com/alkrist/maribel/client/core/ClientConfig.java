package com.alkrist.maribel.client.core;

import java.util.Properties;

import com.alkrist.maribel.common.context.Config;

public class ClientConfig extends Config{

	public String username;
	public int port;
	
	protected ClientConfig() {
		load("client");
	}
	
	public void save() {
		super.save("client");
	}
	
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
