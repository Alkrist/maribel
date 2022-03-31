package com.alkrist.maribel.utils;

/**
 * A simple interface which is an implementation of the Command Pattern
 * (Gang of Four) which can be used further for some delayed tasks, i.e. adding/removing elements
 * to the list while looping through it.
 *
 * @author Mikhail
 *
 */
public interface Command {
	/**
	 * Do the specified task when called.
	 */
	public void execute();
}
