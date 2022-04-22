package com.alkrist.maribel.common.connection.sides;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.bridge.RemoteBridge;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketLoginRequest;
import com.alkrist.maribel.common.connection.packet.packets.PacketLogout;
import com.alkrist.maribel.utils.Logging;

/**
 * Client side is a side implementation for Client, it has server's address and port (for remote)
 * and server's bridge (for local). Here are all of the client's send methods which involve bridge.
 * @author Mikhail
 */
public class ClientSide extends Side{
	//TODO: close
	//TODO: remove test func
	private String username;
	
	private InetAddress serverAddress = null;
	private int serverPort = -1;
	
	private LocalBridge serverBridge;
	
	/**
	 * Client Side constructor.
	 * @param username - Client's username
	 */
	public ClientSide(String username) {
		if(username.length() <= 16)
			this.username = username;
		else {
			Logging.getLogger().log(Level.SEVERE, "wrong username length, name must be <16 characters!");
			System.exit(1);
		}
	}
	
	/**
	 * Initializes the local connection bridge
	 * @param serverBridge - {@link connection.bridges.LocalBridge} of the server
	 */
	public void init(LocalBridge myBridge, LocalBridge serverBridge) {
		initLocal(myBridge);
		
		this.serverBridge = serverBridge;
		((LocalBridge)bridge).init(this, serverBridge);
	}
	
	/**
	 * Initializes the connection bridge over UDP (remote)
	 * @param host - string that represents server host or IP address
	 * @param port - server port
	 */
	public void init(String host, int port) {
		initRemote();
		
		this.serverPort = port;
		
		try {
			this.serverAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		((RemoteBridge)bridge).init(this);
		
		if(serverBridge!=null) serverBridge = null;
	}
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sends the packet via this side's bridge. Defines the type of the connection and uses the proper bridge.
	 * @param packet - packet that should be transmitted
	 */
	public void send(Packet packet) {
		if(super.isActive()) {
			if(super.isLocal())
				((LocalBridge)bridge).send(bridge.coder.encode(packet));
			else
				((RemoteBridge)bridge).send(bridge.coder.encode(packet), serverAddress, serverPort);
		}		
	}
	
	/**
	 * Close the bridge and toggle this side's state inactive.
	 */
	public void close() {
		super.close();
		bridge.close();
	}
	
	/**
	 * Used <b>ONLY</b> on remote connection. Sends the login packet and awaits for the responce.
	 */
	public void login() {	
		if(super.isLocal()) return;
		Packet packet = new PacketLoginRequest(PacketRegistry.getIDFor("LOGIN_REQUEST"), username);
		send(packet);
	}
	
	/**
	 * A logout request, sent to server.
	 */
	public void logout() {
		Packet packet = new PacketLogout(PacketRegistry.getIDFor("LOGOUT"), username);
		send(packet);
	}
	
	//test
	/*public void test(String message) {
		Packet packet = new TestPacket(PacketRegistry.getIDFor("TEST"), username, message);
		if(super.isLocal()) {
			((LocalBridge)bridge).send(bridge.coder.encode(packet));
			System.out.println("sent");
		}
			
		else
			((RemoteBridge)bridge).send(bridge.coder.encode(packet), serverAddress, serverPort);
	}*/
}

