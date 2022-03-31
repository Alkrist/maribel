package com.alkrist.maribel.api;

import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketClientTick;
import com.alkrist.maribel.common.connection.packet.packets.PacketEntity;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginReply;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginRequest;
import com.alkrist.maribel.common.connection.packet.packets.PacketLogout;
import com.alkrist.maribel.common.connection.packet.packets.PacketTick;

/**
 * A class to register packets and listeners on PreInit stage.
 * 
 * Why?
 * We have to register packets hardcode because the order matters.
 * We have to register listeners hardcode because we can't access their classes.
 * 
 * @author Mikhail
 * 
 */
public class MaribelRegistry {

	/**
	 * Register all packets. Mind the order of registry.
	 * WARNING: The order and amount of registered packets should be 100% similar on both sides,
	 * otherwise it will cause malfunction!
	 * 
	 * RightSyntax is PacketRegistry.registerPacket(new PacketSubclass());
	 */
	public static void registerPackets() {
		PacketRegistry.registerPacket(new PacketLoginRequest());
		PacketRegistry.registerPacket(new PacketLoginReply());
		PacketRegistry.registerPacket(new PacketLogout());
		PacketRegistry.registerPacket(new PacketTick());
		PacketRegistry.registerPacket(new PacketClientTick());
		PacketRegistry.registerPacket(new PacketEntity());
	}
	
	/**
	 * Register all listeners.
	 * 
	 * Right syntax is EventManager.registerEvents(new EventSubclass(), Owner.class);
	 */
	public static void registerListeners() {
		//TODO: add listeners
	}
}
