package com.alkrist.maribel.client.updatable.scene;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.proxy.WorldProxy;

public class GameScene extends SceneBase{

	public GameScene() {
		super();
	}
	
	@Override
	public void create() {
		
	}

	@Override
	public void enable() {
		System.out.println("Game scene enabled!");
		Client.world = new WorldProxy(this.engine);
		Client.startTimers();
	}

	@Override
	public void disable() {
		Client.stopTimers();
		Client.world = null;
	}

}
