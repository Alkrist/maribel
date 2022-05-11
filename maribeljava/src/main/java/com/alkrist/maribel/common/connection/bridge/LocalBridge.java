package com.alkrist.maribel.common.connection.bridge;

import java.util.ArrayList;
import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.sides.Side;
import com.alkrist.maribel.utils.Logging;

/**
 * <pre>
 * The Local Bridge is the connection within one application. Local connection uses Shared Memory as
 * the bus for the packets. Local Server or Local Client are technically similar, because both of them can 
 * be connected to only one other bridge at the time, hence there's similar implementation for them.
 * 
 * The logic of Local Bridge is like that: 
 * packet -> serialize -> send: add to other bridge's queue -> (on the other side) -> check for any input in
 * queue -> deserialize -> process packet.
 * </pre>
 * @author Mikhail
 *
 */
public class LocalBridge extends Bridge{

	public Object lock = new Object();
	private ArrayList<byte[]> inputQueue;
	private LocalBridge otherBridge;
	
	/**
	 * Initialize the Local Bridge
	 * @param side - the owner's side (ClientSide or ServerSide)
	 * @param otherBridge - the Local Bridge of the other side.
	 */
	public void init(Side side, LocalBridge otherBridge) {
		this.side = side;
		this.otherBridge = otherBridge;		
		this.inputQueue = new ArrayList<byte[]>();
	}
	
	/*
	 * Process the last packet in the packet in the inpute queue.
	 */
	private void process(byte[] data) {
		Packet packet = (Packet) coder.decode(data, Client.packetRegistry);
		packet.process(side);
	}
	
	/*
	 * Summary:
	 * every tick it checks whether there's a data in the input queue, if it is,
	 * process the first element of the queue and removes it from the queue, if the list
	 * is empty just skip till another tick.
	 */
	public void run() {
		side.open();
		byte[] data;
		while(true) {
			synchronized(lock) {
				if(inputQueue.isEmpty()) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						Logging.getLogger().log(Level.SEVERE, Thread.currentThread().getName()+" was interrupted", e);
					}
				}
				if(inputQueue.isEmpty())
					continue;			
				data = inputQueue.remove(0);
			}
			process(data);
		}
	}

	@SuppressWarnings("deprecation")
	public void close() {
		this.stop();
		synchronized(lock) {lock.notify();}	
	}
	
	/*
	 * Receive the data incoming from another bridge.
	 * Called from this class only.
	 */
	protected void addToQueue(byte[] data) {
		synchronized(lock) {
			inputQueue.add(data);
			lock.notify();
		}
	}
	
	/**
	 * Send data to the other side
	 * @param data the serialized and ready-to-go packet as byte array
	 */
	public void send(byte[] data) {
		otherBridge.addToQueue(data);
	}

}
