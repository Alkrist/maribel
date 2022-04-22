package com.alkrist.maribel.client.updatable.scene;

import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.proxy.WorldProxy;
import com.alkrist.maribel.utils.Logging;

public class GameScene extends SceneBase{

	public GameScene() {
		super();
	}
	
	@Override
	public void create() {
		
	}

	@Override
	public void enable() {
		Logging.getLogger().log(Level.INFO, "Game scee was enabled.");
		Client.world = new WorldProxy(this.engine);
		Client.startTimers();
	}

	@Override
	public void disable() {
		Client.stopTimers();
		Client.world = null;
	}

}
