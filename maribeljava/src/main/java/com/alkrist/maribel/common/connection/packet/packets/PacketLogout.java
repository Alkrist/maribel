package com.alkrist.maribel.common.connection.packet.packets;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ServerSide;

public class PacketLogout extends Packet{

	public PacketLogout() {}
	public PacketLogout(byte id) {
		super(id);
	}
	
	String name;
	int name_len;
	
	public PacketLogout(byte id, String name) {
		super(id);
		this.name = name;
		this.name_len = name.length();
	}
	
	@Override
	public void process(ServerSide server) {
		server.removeClient(server.getClient(name));
		System.out.println("logout attempt: "+name);
	}
	
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		buffer.writeInt(name_len);
		buffer.writeString(name);
		return true;
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		name_len = buffer.readInt();
		name = buffer.readString(name_len);
		return true;
	}
}

