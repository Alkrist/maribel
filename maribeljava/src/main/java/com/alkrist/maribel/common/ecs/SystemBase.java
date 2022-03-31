package com.alkrist.maribel.common.ecs;

/**
 * A base class for all Systems. System is a set of methods which contain no data and are being processed
 * cyclically. Systems main functionality is to process data stored in components which are stored in entities,
 * though systems can be used for some other data-unrelated actions which occurs cyclically.
 * 
 * This class has several methods to override:
 * 
 * 1) addedToEngine(Engine engine)
 * 2) removedFromEngine(Engine engine)
 * 3) update(double deltaTime)
 * 
 * @author Mikhail
 *
 */
public abstract class SystemBase {

	private boolean enabled;
	protected Engine engine = null;
	private int priority; //Lower = executed first
	
	/**
	 * A default constructor.
	 */
	public SystemBase() {
		this.priority = 0;
		this.enabled = true;
	}
	
	/**
	 * A constructor with the System Priority argument.
	 * @param priority - system priority: lower value means the system is executed first
	 */
	public SystemBase(int priority) {
		this.priority = priority;
		this.enabled = true;
	}
	
	/**
	 * Bind the engine to this system.
	 * 
	 * @param engine engine to bind
	 */
	protected final void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	/**
	 * Enable this system. Used only within the ECS core.
	 */
	protected final void enable() {
		this.enabled = true;
	}
	
	/**
	 * Disable this system. Used only within the ECS core.
	 */
	protected final void disable() {
		this.enabled = false;
	}
	
	/**
	 * @return this system's activity state (enabled/disabled).
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return the engine object this system is bound to.
	 */
	public final Engine getEngine() {
		return engine;
	}
	
	/**
	 * @return this system's priority
	 */
	public final int getPriority() {
		return priority;
	}

	// ******* ABSTARCT/OVERRIDE METHODS *******//
	
	/**
	 * Do the system's update loop. This method is called cyclically.
	 * 
	 * @param deltaTime Maybe delete??? :-/
	 */
	public abstract void update(double deltaTime);
	
	/**
	 * Some action which occurs when this system is added to the engine. 
	 * By default it's empty.
	 * 
	 * @param engine
	 */
	public void addedToEngine(Engine engine) {}

	/**
	 * Some action which occurs when this system is removed from the engine.
	 * By default it's empty.
	 * 
	 * @param engine
	 */
	public void removedFromEngine(Engine engine) {}
}
