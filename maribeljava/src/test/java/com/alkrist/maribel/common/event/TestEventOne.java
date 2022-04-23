package com.alkrist.maribel.common.event;

public class TestEventOne extends Event{

	private static final HandlerList handlers = new HandlerList();

	public String getMessage() {
		return this.getClass().getCanonicalName();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
