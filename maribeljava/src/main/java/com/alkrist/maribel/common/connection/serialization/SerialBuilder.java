package com.alkrist.maribel.common.connection.serialization;


/**
 * SerialBuilder helps to identify the object type by the data from
 * buffer given (i.e. packet ID).
 * 
 * @author Mikhail
 *
 */
public interface SerialBuilder {

	/**
	 * Reads the buffer data and identifies the object type.
	 * 
	 * @param buffer - the buffer to get data from
	 * @return {@link connection.serialization.Serializable} object
	 */
	public Serializable build(SerialBuffer buffer);
}
