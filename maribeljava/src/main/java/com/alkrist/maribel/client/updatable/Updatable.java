package com.alkrist.maribel.client.updatable;

/**
 * A simple interface to tag all elements that can be updated in client updater.
 * @author Mikhail
 */
public interface Updatable {
	
	/**
	 * A method that is called every tick in client updater.
	 * @param dt - delta time elapsed for last tick
	 */
	public void update(double dt);
}
