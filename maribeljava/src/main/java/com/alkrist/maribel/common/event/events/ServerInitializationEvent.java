package com.alkrist.maribel.common.event.events;

import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.event.Event;
import com.alkrist.maribel.common.event.HandlerList;


public class ServerInitializationEvent extends Event{

	private Engine engine;
	private ServerSide side;
	
	public ServerInitializationEvent(Engine engine, ServerSide side) {
		this.engine = engine;
		this.side = side;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public ServerSide getConnection() {
		return side;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
