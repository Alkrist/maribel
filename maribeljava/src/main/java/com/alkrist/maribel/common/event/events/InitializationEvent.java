package com.alkrist.maribel.common.event.events;

import com.alkrist.maribel.common.event.Event;
import com.alkrist.maribel.common.event.HandlerList;

public class InitializationEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
