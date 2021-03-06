package com.alkrist.maribel.common.connection.packet.packets;

import java.util.logging.Level;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.utils.Logging;

public class PacketLogout extends Packet{

	public PacketLogout() {
		super("LOGOUT");
	}
	public PacketLogout(byte id) {
		super(id, "LOGOUT");
	}
	
	String name;
	int name_len;
	
	public PacketLogout(byte id, String name) {
		super(id, "LOGOUT");
		this.name = name;
		this.name_len = name.length();
	}
	
	@Override
	public void process(ServerSide server) {
		server.removeClient(server.getClient(name));
		Logging.getLogger().log(Level.INFO, "logout attempt: "+name);
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

