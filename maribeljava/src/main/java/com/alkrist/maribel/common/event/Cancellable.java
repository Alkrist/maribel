package com.alkrist.maribel.common.event;
/**
 * An interface for events that can be cancelled.
 */
public interface Cancellable {

	/**
	 * Sets the cancellation state of this event. Cancelled events will not
	 * be executed, but will be passed to listeners.
	 * 
	 * @param cancelled true if you wish to cancel this event.
	 */
	public void setCancelled(boolean cancelled);
	
	/**
	 * An event cancelled state.
	 * A cancelled event will not be executed, but will be passed to listeners.
	 * 
	 * @return whether the event still be propagating or not.
	 */
	public boolean isCancelled();
}
