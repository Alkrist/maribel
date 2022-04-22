package com.alkrist.maribel.client.updatable.scene;

import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

public class MenuScene extends SceneBase{

	public MenuScene() {
		super();
	}
	
	@Override
	public void create() {
		
	}

	@Override
	public void enable() {
		Logging.getLogger().log(Level.INFO, "Menu scene enabled!");
	}

	@Override
	public void disable() {
		
	}

}
