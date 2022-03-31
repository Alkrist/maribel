package com.alkrist.maribel.common.connection.serialization;

/**
 * An interface to identify objects that can be serialized and converted to byte array
 * 
 * The process of conversion is handled via the read and write methods, where you can describe
 * which values should write to the {@link connection.serialization.SerialBuffer} and read from it.
 * 
 * WARNING! Mind the process order in read and write methods: The order should be EXACTLY THE SAME in both
 * methods!!!
 * 
 * @author Mikhail
 *
 */
public interface Serializable {

	/**
	 * Read the data from {@link connection.serialization.SerialBuffer}
	 * WARNING! MIND THE PROCESS ORDER IN THIS METHOD! (Should be similar with read method)
	 * 
	 * @param buffer - used buffer
	 * @return the read state
	 */
	public boolean read(SerialBuffer buffer);
	
	/**
	 * Write the data to {@link connection.serialization.SerialBuffer}
	 * WARNING! MIND THE PROCESS ORDER IN THIS METHOD! (Should be similar with write method)
	 * 
	 * @param buffer - used buffer
	 * @return the write state
	 */
	public boolean write(SerialBuffer buffer);
	
}

