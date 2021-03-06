package com.alkrist.maribel.common.connection.packet.packets;

import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ClientSide;
import com.alkrist.maribel.utils.Logging;

public class PacketTick extends Packet{

	/*
	 * Write - only server side
	 * Read,Process - only client side
	 */
	public PacketTick() {
		super("TICK");
	}
	public PacketTick(byte id) {
		super(id, "TICK");
	}
	
	Packet snapshot[];
	
	public PacketTick(byte id, Packet[] updates) {
		super(id, "TICK");
		this.snapshot = updates;
	}
	
	@Override
	public void process(ClientSide client) {
		//Client.world.resetSnapshotBits();
		//TODO: do something with it
		int snapshot_len = snapshot.length;
		for(int i=0; i<snapshot_len; i++)
			snapshot[i].process(client);
		
		//Client.world.syncEntityCache();
	}
	
	//Client-side only
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		
		int snapshot_len = buffer.readShort();
		snapshot = new Packet[snapshot_len];
		for(int i=0; i<snapshot_len; i++) {
			Packet packet = (Packet) Client.packetRegistry.build(buffer);
			if(packet == null) {
				Logging.getLogger().log(Level.WARNING,"[PacketTick]: Unknown packet received with id: "+buffer.peekByte());
				return false;
			}
			packet.read(buffer);
			snapshot[i] = packet;	
		}
		return true;
	}
	
	//Server-side only
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		
		int snapshot_len = snapshot.length;
		buffer.writeShort((short) snapshot_len);
		
		for(int i=0; i<snapshot_len; i++) {
			snapshot[i].write(buffer);
		}
		
		return true;
	}
}
