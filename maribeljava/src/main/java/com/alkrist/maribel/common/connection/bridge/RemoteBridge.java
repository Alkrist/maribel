package com.alkrist.maribel.common.connection.bridge;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginRequest;
import com.alkrist.maribel.common.connection.sides.ClientSide;
import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.utils.ByteConverter;
import com.alkrist.maribel.utils.Logging;

/**
 * <pre>
 * The Remote bridge is a connection through Internet, via the UDP protocol. It has two different
 * implementations, for server (socket is opened on a specific port) and client (socket is opened on
 * random port). The implementations vary via the different init methods.
 * 
 * The logic of Remote Bridge is like that: 
 * packet -> serialize -> send over UDP on specific address & port (stored in ClientSide for client and in ProxyClient
 * for server) -> (on the other side) -> check for any data received on socket -> read the length of the data ->
 * read the data (parsePacket) -> process the packet.
 * </pre>
 * @author Mikhail
 *
 */
public class RemoteBridge extends Bridge{

	private byte[] socketBuffer; //The received data, the max length equals the value from Bridge class
	private DatagramSocket socket; //Socket of this side
	
	/**
	 * Initializer for client side only.
	 * @param client - Client side
	 */
	public void init(ClientSide client) {
		this.side = client;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			Logging.getLogger().log(Level.SEVERE, "UDP connection clientside init failure:", e);
		}
	}
	
	/**
	 * Initializer for server side only.
	 * @param server - Server side
	 * @param port - server port
	 */
	public void init(ServerSide server, int port) {
		this.side = server;
		
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			Logging.getLogger().log(Level.SEVERE, "UDP connection serverside init failure:", e);
		}
	}
	
	/*
	 * Process the last received packet. Mind that Login Request packet is processed
	 * it's own way, using InetAddress and port of the sender (client in this case).
	 */
	private void process(byte[] data, InetAddress address, int port) {
		Packet packet = (Packet) coder.decode(data, Client.packetRegistry);
		
		if(packet instanceof PacketLoginRequest) 
			((PacketLoginRequest)packet).doLogin((ServerSide) side, address, port);		
		else
		packet.process(side);
	}
	
	/*
	 * Summary:
	 * 1) refresh the socket buffer
	 * 2) try to receive the packet and store it in socket buffer
	 * 3) read the received packet
	 */
	public void run() {
		side.open();
		while(true) {
			socketBuffer = new byte[Bridge.MAX_DATAGRAM_LENGTH];
			DatagramPacket packet = new DatagramPacket(socketBuffer, socketBuffer.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				Logging.getLogger().log(Level.SEVERE, "UDP connection runtime failure:", e);
			}
			
			parsePacket(packet);
		}
	}
	
	/*
	 * Reads the packet of the given length,
	 * removes also the length value and processes the packet.
	 */
	private void parsePacket(DatagramPacket packet) {
		byte[] len = readAll(packet.getData(), 0, 4);
		int packetLength = ByteConverter.getInt32(len, 0);
		byte[] data = readAll(packet.getData(), 4, packetLength);
		process(data, packet.getAddress(), packet.getPort());
	}

	/**
	 * Called after the loop is discontinued, when the bridge is closed. 
	 * Closes the socket.
	 */
	@SuppressWarnings("deprecation")
	public void close() {
		this.stop();
		socket.close();
	}

	private byte[] packetLength = new byte[4]; //This array temporary stores packet length in bytes
	/**
	 * <pre>
	 * Sends a packet to specified address and port. 
	 * 
	 * Packet right before dispatch should be formatted like that:
	 *         
	 *   4 bytes   | 1 byte |  N (N<=MAX_PACKET_LENGTH) bytes
	 * --------------------------------------------------------
	 *  length(=N) |   id   |              data
	 *  </pre>
	 * @param data - serialized packet
	 * @param address - the address of receiver
	 * @param port - the port of receiver
	 */
	public void send(byte[] data, InetAddress address, int port) {
		
		//step I: form a byte array
		ByteConverter.setInt32(packetLength, 0, data.length);
		byte[] packetData = new byte[4+data.length]; //Formatted packet array - to send!
		System.arraycopy(packetLength, 0, packetData, 0, 4);
		System.arraycopy(data, 0, packetData, 4, data.length);
		
		//step II: check whether the array size isn't bigger than receive buffer capacity.
		if(packetData.length > Bridge.MAX_DATAGRAM_LENGTH) {
			Logging.getLogger().log(Level.SEVERE, "packet is overloaded, max length = "+Bridge.MAX_DATAGRAM_LENGTH+
					", current = "+packetData.length);
			return;
		}
		
		//step III: create a datagram packet and send it.
		DatagramPacket packet = new DatagramPacket(packetData, packetData.length, address, port);	
		try {
			socket.send(packet);
		} catch (IOException e) {
			Logging.getLogger().log(Level.SEVERE, "UDP connection packet send failure:", e);
		}
	}
	
	/**
	 * @return whether or not the socket is closed and not currently available.
	 */
	public boolean isSocketClosed() {
		return socket.isClosed();
	}
}

