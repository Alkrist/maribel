package com.alkrist.maribel.common.event;
/**
 * Represents the handler priority in the event execution. 
 */
public enum EventPriority {

	/**
	 * Event call of this priority is of very low importance and runs first
	 * to allow major handlers for further customize the outcome.
	 */
	LOWEST(0),
	
	/**
	 * Called after "LOWEST" handlers.
	 */
	LOW(1),
	
	/**
	 * For general purpose handlers, neither important nor unimportant.
	 */
	NORMAL(2),
	
	/**
	 * Called after "NORMAL" handlers.
	 */
	HIGH(3),
	
	/**
	 * This call is critical and must have the final say in the event's outcome.
	 */
	HIGHEST(4),
	
	/**
	 * Is used to purely monitor the outcome of the event, hence it's called the last,
	 * it can't modify the outcome.
	 * No modifications to the event should be made under this priority.
	 */
	MONITOR(5);
	
	private int slot;
	
	private EventPriority(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
}
