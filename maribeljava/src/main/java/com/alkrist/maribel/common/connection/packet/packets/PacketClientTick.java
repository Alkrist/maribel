package com.alkrist.maribel.common.connection.packet.packets;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ServerSide;

public class PacketClientTick extends Packet{

	public PacketClientTick() {}
	public PacketClientTick(byte id) {
		super(id);
	}
	
	//TODO: add unique data
	
	@Override
	public void process(ServerSide server) {
		System.out.println("clientTick.");
	}
	
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		
		return true;
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		
		return true;
	}
}
