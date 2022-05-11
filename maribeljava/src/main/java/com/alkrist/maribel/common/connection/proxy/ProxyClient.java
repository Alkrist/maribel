package com.alkrist.maribel.common.connection.proxy;

import java.util.ArrayList;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginReply;
import com.alkrist.maribel.common.connection.packet.packets.PacketLogout;
import com.alkrist.maribel.common.connection.packet.packets.PacketTick;
import com.alkrist.maribel.common.connection.sides.ServerSide;

/**
 * An abstract class that represents a client instance. The identifier of the client is name,
 * main purpouse of this class is to accumulate updates durning the server update and dispatch them in 
 * the end. Also it handles client-repated events from the server side. Can be thought as the Client Side
 * representation on the server side.
 * @author Mikhail
 *
 */
public abstract class ProxyClient {

	//TODO: remove debug-only methods
	
	private String name;
	protected ServerSide serverSide;
	
	public ArrayList<Packet> updates;
	
	/**
	 * Abstract constructor for Proxy Client
	 * @param name - client's unique name
	 * @param server - server side (mind that the proxy client is SERVER-ONLY instance!
	 */
	public ProxyClient(String name, ServerSide server) {
		this.name = name;
		this.serverSide = server;
		this.updates = new ArrayList<Packet>();
	}
	
	/**
	 * @return client's unique name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Send the packet to the client side.
	 * @param packet - packet to be sent
	 */
	public abstract void send(Packet packet);
	
	//Next go the send methods
	public void loginReply(short state) {
		Packet packet = new PacketLoginReply(PacketRegistry.getIDFor("LOGIN_REPLY"), state);
		send(packet);
	}
	
	/**
	 * Send the logout responce to the client.
	 */
	public void logout() {
		Packet packet = new PacketLogout(PacketRegistry.getIDFor("LOGOUT"));
		send(packet);
	}
	
	/**
	 * Add an update packet. Updates are dispatched all in one every tick.
	 * @param update - packet that will be included to update list
	 */
	public void addUpdate(Packet update) {
		updates.add(update);
	}
	
	/**
	 * Dispatch all updates.
	 */
	public void tick() {
		Packet[] packets = updates.toArray(new Packet[0]);
		Packet packet = new PacketTick(PacketRegistry.getIDFor("TICK"), packets);
		send(packet);
		updates.clear();
	}
	
	/*DEBUG-ONLY*/
	/*public void test(String name, String message) {
		Packet packet = new TestPacket(PacketRegistry.getIDFor("TEST"), name, message);
		send(packet);
	}*/
}
