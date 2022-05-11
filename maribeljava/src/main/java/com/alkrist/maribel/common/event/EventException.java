package com.alkrist.maribel.common.event;

/**
 * Represents an event type exception
 */
@SuppressWarnings("serial")
public class EventException extends Exception{
	
	private Throwable cause;
	
	/**
	 * Constructs a new Event Exception
	 */
	public EventException() {
		cause = null;
	}
	
	/**
	 * Constructs a new Event Exception with a given message
	 * @param message The message
	 */
	public EventException(String message) {
		super(message);
		cause = null;
	}
	
	/**
	 * Constructs a new Event Exception with a given message
	 * @param message The message
	 * @param cause The exception that caused this one.
	 */
	public EventException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}
	
	/**
	 * Constructs an exception based on the given exception
	 * @param cause The exception that caused this one.
	 */
	public EventException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * If possible, returns the exception that caused this one.
	 */
	@Override
	public Throwable getCause() {
		return cause;
	}
}
