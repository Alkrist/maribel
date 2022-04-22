package com.alkrist.maribel.common.connection.sides;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;

import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.bridge.RemoteBridge;
import com.alkrist.maribel.common.connection.proxy.ProxyClient;
import com.alkrist.maribel.common.connection.proxy.RemoteClient;
import com.alkrist.maribel.utils.Logging;


/**
 * <pre>
 * A Server Side is a side server implementation. Used to hook up all of the connected clients.
 * Has several related things:
 * 1) List of Proxy Clients - connected clients.
 * 2) client's bridge (for local connection).
 * </pre>
 * @author Mikhail
 *
 */
public class ServerSide extends Side{
	//TODO: close
	//TODO: remove debug-only methods
	
	private ArrayList<ProxyClient> clients;
	
	private LocalBridge clientBridge = null;
	
	public Object clientLock = new Object();
	
	/**
	 * Initializes the local connection bridge
	 * @param clientBridge - {@link connection.bridges.LocalBridge} of the client
	 */
	public void init(LocalBridge myBridge, LocalBridge clientBridge) {
		initLocal(myBridge);
		clients = new ArrayList<ProxyClient>();
		this.clientBridge = clientBridge;
		((LocalBridge)bridge).init(this, clientBridge);
	}
	
	/**
	 * Initializes the connection bridge over UDP (remote)
	 * @param port - server port
	 */
	public void init(int port) {
		initRemote();
		
		clients = new ArrayList<ProxyClient>();
		((RemoteBridge)bridge).init(this, port);
		
		if(clientBridge != null) clientBridge = null;
	}
	
	/**
	 * Called from Login Request packet. Used to check whether this client isn't already logged in or something
	 * else, if everything is OK, then add a new client and send the Login Reply packet to that client.
	 * @param name - client's name
	 * @param address - client's inet address
	 * @param port - client's port
	 */
	public void login(String name, InetAddress address, int port) {
		Logging.getLogger().log(Level.INFO, "Attempt to connect from: "+address.getHostName()+":"+port);
		RemoteClient client = new RemoteClient(name, this, address, port);
		
		if(addClient(client))
			client.loginReply((short) 1);
		else 
			client.loginReply((short) 2);
	}
	
	/**
	 * Checks whether the client already exists, if not adds it. (check is arranged by client's name)
	 * @param client - a pending client.
	 * @return true - if the client was added, false - if the client already exists
	 */
	public boolean addClient(ProxyClient client) {
		boolean isAdded = false;	
		synchronized(clientLock) {
			for(ProxyClient c: clients) {
				if(c.getName().equalsIgnoreCase(client.getName())) {
					isAdded = true;
					break; //may actually return, break is in case some log will be needed
				}			
			}
			if(!isAdded) {
				clients.add(client);
				return true;
			}
			return false;			
		}
	}
	
	/**
	 * Remove the client.
	 * @param client - client to remove
	 * @return whether or not the client was removed
	 */
	public boolean removeClient(ProxyClient client) {
		synchronized(clientLock) {
			if(clients.remove(client))
				return true;
			else return false;
		}
	}
	
	/**
	 * DEBUG PURPOSE ONLY
	 * List all of the clients
	 */
	public void listClients() {
		synchronized(clientLock) {
			System.out.println("Listing clients:");
			for(ProxyClient client: clients)
				System.out.println(client.getName());
		}
	}
	
	/**
	 * @return clients list
	 */
	public ArrayList<ProxyClient> getClients(){
		synchronized(clientLock) {
			return clients;
		}	
	}
	
	/**
	 * Get a specific client by name.
	 * @param name - client's name
	 * @return client with this name
	 */
	public ProxyClient getClient(String name) {
		synchronized(clientLock) {
			for(ProxyClient client: clients) {
				if(client.getName().equalsIgnoreCase(name))
					return client;
			}
			return null;
		}
	}

	/**
	 * Called every server tick, dispatches tick updates per every client
	 */
	public void tick() {
		synchronized(clientLock) {
			for(ProxyClient client: clients)
				client.tick();
		}	
	}
	
	/**
	 * Close the bridge and toggle this inactive.
	 */
	public void close() {
		while(clients.size()>0) {
			clients.get(0).logout();
			clients.remove(0);
		}
		super.close();
		bridge.close();
	}
}
