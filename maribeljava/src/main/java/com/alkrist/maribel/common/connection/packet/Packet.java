package com.alkrist.maribel.common.connection.packet;

import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.serialization.Serializable;
import com.alkrist.maribel.common.connection.sides.ClientSide;
import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.common.connection.sides.Side;

/**
 * <pre>
 * A parent class for all packets. 
 * 
 * Subclasses must have 3 constructors:
 * 1) Empty constructor with no args - for registry.
 * 	Example:
 * 	public MyPacket(){}
 * 2) A constructor with 1 arg - byte ID, whuch should be passed to super.
 * 	Example:
 * 	public MyPacket(byte id){
 * 		super(id);
 * 	}
 * 3) A custom constructor with custom args (if needed), but also must have byte ID arg.
 * 	Example:
 * 	public myPacket(byte id, int value){
 * 		super(id);
 * 		this.value = value;
 * 	}
 * 
 * Also the Packet class contains several process methods for different sides, which should be 
 * essentially overridden for the appropriate side. Do not override process(Side side) method.
 * 
 * Also the Packet class contains "read" and "write" methods inherited from {@link com.alkrist.maribel.common.connection.serialization.Serializable}.
 * Their return state depends on how successful was the "read" and "write" process.
 * When overridden, both methods must use similar methods from superclass (this class).
 * 	Example:
 * 	public boolean read(SerialBuffer buffer){
 * 		super.read(buffer);
 * 		//bla bla bla...
 * 		return true;
 * 	}
 * Same for "write" method. It's required to identify the packet type in {@link com.alkrist.maribel.common.connection.serialization.SerialBuilder}.
 * </pre>
 * @author Mikhail
 *
 */
public class Packet implements Serializable{

	public byte id;
	
	private String NAME;
	
	/**
	 * A simple constructor for packet registry, when the ID isn't yet assigned.
	 */
	public Packet(String NAME) {
		this.NAME = NAME;
	}
	
	/**
	 * A default packet constructor. Used in specific constructors.
	 * @param id - packet ID
	 */
	public Packet(byte id, String NAME) {
		this.id = id;
		this.NAME = NAME;
	}
	
	public String getName() {
		return NAME;
	}
	
	/**
	 * Called from the bridge's process method. Checks on what side the packet process
	 * was initiated. Do not override.
	 * @param side - the side on which the packet was received.
	 */
	public void process(Side side) {
		if(side instanceof ServerSide)
			process((ServerSide)side);
		else process((ClientSide)side);
	}
	
	/**
	 * Process this packet on server side.
	 * If this method is called from wrong side will cause an error.
	 * @param server - the server side on which the packet will be processed.
	 */
	protected void process(ServerSide server) {
		System.err.println("Wrong packet call on server side!");
	}
	
	/**
	 * Process this packet on client side.
	 * If this method is called from wrong side will cause an error.
	 * @param client - the client side on which the packet will be processed.
	 */
	protected void process(ClientSide client) {
		System.err.println("Wrong packet call on client side!");
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		id = buffer.readByte();
		return true;
	}

	@Override
	public boolean write(SerialBuffer buffer) {
		buffer.writeByte(id);
		return true;
	}
}

