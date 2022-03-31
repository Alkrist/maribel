package com.alkrist.maribel.common.event;
/**
 * Represents an event.
 */
public abstract class Event {

	protected String name;
	protected boolean cancelled = false;
	
	public abstract HandlerList getHandlers();
	
	/**
	 * @return the event name
	 */
	public String getEventName() {
		if(name==null)
			name = getClass().getSimpleName();
		return name;
	}
	
	//TODO: add result set: DENY, DEFAULT, ALLOW
}
