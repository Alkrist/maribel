package com.alkrist.maribel.common.connection.proxy;

import java.net.InetAddress;

import com.alkrist.maribel.common.connection.bridge.RemoteBridge;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.sides.ServerSide;

/**
 * UDP implementtion of the Proxy Client.
 * 
 * @author Mikhail
 *
 */
public class RemoteClient extends ProxyClient{

	private InetAddress clientAddress;
	private int clientPort;
	
	/**
	 * A constructor for remote client to send dta over the UDP.
	 * @param name - client's unique name
	 * @param server - server link
	 * @param address - client's address
	 * @param port - client's port
	 */
	public RemoteClient(String name, ServerSide server, InetAddress address, int port) {
		super(name, server);
		this.clientAddress = address;
		this.clientPort = port;
	}

	/**
	 * @return client address
	 */
	public InetAddress getAddress() {
		return clientAddress;
	}
	
	/**
	 * @return client port
	 */
	public int getPort() {
		return clientPort;
	}
	
	public void send(Packet packet) {
		if(serverSide.isActive()) {
			((RemoteBridge)serverSide.getBridge())
			.send(serverSide.getBridge().coder.encode(packet), clientAddress, clientPort);
		}	
	}

}
