package com.alkrist.maribel.common.connection.packet;

import java.util.HashMap;
import java.util.logging.Level;

import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.serialization.SerialBuilder;
import com.alkrist.maribel.common.connection.serialization.Serializable;
import com.alkrist.maribel.utils.Logging;

/**
 * <pre>
 * Packet Registry is the static class that handles 2 main packet operations:
 * 1) register new packet type (used on initialization stage at the very beginning)
 * 2) rebuild a received packet when needed (implements {@link com.alkrist.maribel.common.connection.serialization.SerialBuffer}).
 * </pre>
 * @author Mikhail
 *
 */
public class PacketRegistry implements SerialBuilder{
	
	private static Packet[] packets = new Packet[128]; //Max 128 packets, otherwise the ID should be > 1 byte
	private static HashMap<String, Byte> ids = new HashMap<String, Byte>();
	
	private static byte nextID = 0;
	
	/**
	 * Register a packet.
	 * NOTE: packet ID variable should be here, as well as the case clause in setID method.
	 * @param packet
	 * @param NAME
	 */
	public static void registerPacket(Packet packet) {
		if(nextID < 0) {
			Logging.getLogger().log(Level.SEVERE, "[PacketRegistry]: Stack overflow. Too many packets registered!");
			System.exit(1);
		}
		packet.id = nextID++;
		packets[packet.id] = packet;
		ids.put(packet.getName(), packet.id);
	}
	
	/**
	 * @param NAME - packet name IN_UPPERCASE
	 * @return packet id associated with this name
	 */
	public static byte getIDFor(String NAME) {
		return ids.get(NAME);
	}
	
	@SuppressWarnings("deprecation")
	public Serializable build(SerialBuffer buffer) {
		byte id = buffer.peekByte();
		try {	
			return packets[id].getClass().newInstance();		
		}catch(Exception e) {
			Logging.getLogger().log(Level.SEVERE, "[PacketRegistry]: An error occured in packet builder, packet isn't recognized.", e);
			return null;
		}	
	}
}
