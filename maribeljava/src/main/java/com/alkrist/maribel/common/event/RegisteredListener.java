package com.alkrist.maribel.common.event;

/**
 * Stores the data for one specific listener
 */
public class RegisteredListener {

	private final Listener listener;
	private final EventPriority priority;
	private final EventExecutor executor;
	private final boolean ignoreCancelled;
	private final Object owner;
	
	public RegisteredListener(Listener listener, EventPriority priority, EventExecutor executor,
			Object owner, boolean ignoreCancelled) {
		this.listener = listener;
		this.priority = priority;
		this.executor = executor;
		this.owner = owner;
		this.ignoreCancelled = ignoreCancelled;
	}

	/**
	 * Gets the listener attached to this registration
	 * 
	 * @return listener
	 */
	public Listener getListener() {
		return listener;
	}

	/**
	 * Gets the priority of this registration
	 * 
	 * @return priority
	 */
	public EventPriority getPriority() {
		return priority;
	}

	/**
	 * Check whether this listener accepts cancelled events
	 * 
	 * @return true when ignoring cancelled events
	 */
	public boolean isIgnoringCancelled() {
		return ignoreCancelled;
	}
	
	/**
     * Calls the event executor
     *
     * @param event The event
     * @throws EventException If an event handler throws an exception.
     */
	public void callEvent(final Event event) throws EventException{
		//If event can be cancelled and this registration is ignoring
		//cancelled events then terminate this call.
		if (event instanceof Cancellable) {
			if(((Cancellable)event).isCancelled() && isIgnoringCancelled())
				return;
		}
		executor.execute(listener, event);
	}

	/**
     * Gets the object for this registration linked to
     *
     * @return Registered owner
     */
	public Object getOwner() {
		return owner;
	}
}
