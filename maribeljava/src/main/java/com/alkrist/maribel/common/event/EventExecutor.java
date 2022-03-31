package com.alkrist.maribel.common.event;

/**
 * An interface which defines the class for event calls
 * This interface is implemented per Registered Listener hence it uses all different methods to invoke.
 */
public interface EventExecutor {

	/**
	 * This method has the code to execute one method declared under EventHandler annotation
	 * 
	 * @param listener the specific listener the method is invoked for
	 * @param event the parameter of method
	 * @throws EventException
	 */
	public void execute(Listener listener, Event event)throws EventException;
}
