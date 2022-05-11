package com.alkrist.maribel.common.connection.bridge;

import com.alkrist.maribel.common.connection.serialization.Serializer;
import com.alkrist.maribel.common.connection.sides.Side;

/**
 * The parent class for all bridge types, bridges can be local (data is transmitted through shared memory)
 * and remote (data is transmitted over UDP).
 * 
 * @author Mikhail
 */
public abstract class Bridge extends Thread{

	//The biggest possible length of Datagram Packet's data array
	public static final int MAX_DATAGRAM_LENGTH = 1024; //1024 bytes
	
	//The side this bridge is bind to
	protected Side side;
	
	//The Serializer object that can be accessed from multiple threads; Has no need to be synchronized
	//hence it's static and contains no data itself.
	public Serializer coder = new Serializer();
	
	/**
	 * Run method - implementation of Thread run method, used for
	 * listening for income data and processing it.
	 */
	public abstract void run();
	
	/**
	 * Close method is used to stop the thread loop and handle the termination.
	 */
	public abstract void close();
	
	/**
	 * Simply reads a byte data from one array to another
	 * @param data - source array
	 * @param pos - start position (included!!!)
	 * @param length - the length of result data from (including) start position
	 * @return result byte array
	 */
	public byte[] readAll(byte[] data, int pos, int length){
		byte[] result = new byte[length];
		int j = 0;
		
		for(int i=pos; i<length+pos; i++) {
			result[j++] = data[i];
		}
		return result;
	}
}