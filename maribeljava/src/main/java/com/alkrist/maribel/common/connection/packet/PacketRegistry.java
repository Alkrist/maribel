package com.alkrist.maribel.common.connection.packet;

import java.util.HashMap;

import com.alkrist.maribel.common.connection.packet.packets.PacketClientTick;
import com.alkrist.maribel.common.connection.packet.packets.PacketEntity;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginReply;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginRequest;
import com.alkrist.maribel.common.connection.packet.packets.PacketLogout;
import com.alkrist.maribel.common.connection.packet.packets.PacketTick;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.serialization.SerialBuilder;
import com.alkrist.maribel.common.connection.serialization.Serializable;

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
	public static void registerPacket(Packet packet, String NAME) {
		if(nextID < 0) {
			System.err.println("[PacketRegistry]: Stack overflow. Too many packets registered!");
			System.exit(1);
		}
		packet.id = nextID++;
		packets[packet.id] = packet;
		ids.put(NAME, packet.id);
	}
	
	/**
	 * Register all packets. Mind the order of registry.
	 * WARNING: The order and amount of registered packets should be 100% similar on both sides,
	 * otherwise it will cause malfunction!
	 */
	public void registerDefaultPackets() {
		registerPacket(new PacketLoginRequest(), "LOGIN_REQUEST");
		registerPacket(new PacketLoginReply(), "LOGIN_REPLY");
		registerPacket(new PacketLogout(), "LOGOUT");
		registerPacket(new PacketTick(), "TICK");
		registerPacket(new PacketClientTick(), "CLIENT_TICK");
		registerPacket(new PacketEntity(), "ENTITY");
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
			System.err.println("[PacketRegistry]: An error occured in packet builder, packet isn't recognized.");
			e.printStackTrace();
			return null;
		}	
	}
}
