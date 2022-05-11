package com.alkrist.maribel.common.connection.proxy;

import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.sides.ServerSide;


/**
 * A representation of local proxy client instance for singleplayer-only. Is easy to work with,
 * has only 1 method for sending data through local bridge.
 * 
 * @author Mikhail
 *
 */
public class LocalClient extends ProxyClient{

	/**
	 * A local proxy client constructor
	 * @param name - clinet's unique name
	 * @param server - server side link.
	 */
	public LocalClient(String name, ServerSide server) {
		super(name, server);
	}

	public void send(Packet packet) {
		if(serverSide.isActive()) {
			((LocalBridge)serverSide.getBridge()).send(serverSide.getBridge().coder.encode(packet));
		}	
	}
}
