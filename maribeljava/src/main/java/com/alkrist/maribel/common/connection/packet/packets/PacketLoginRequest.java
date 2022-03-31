package com.alkrist.maribel.common.connection.packet.packets;

import java.net.InetAddress;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.common.connection.sides.Side;

public class PacketLoginRequest extends Packet{

	public PacketLoginRequest() {}
	public PacketLoginRequest(byte id) {
		super(id);
	}
	
	private String name;
	private int name_len;
	
	public PacketLoginRequest(byte id, String name) {
		super(id);
		this.name = name;
		this.name_len = name.length();
	}
	
	@Override
	public void process(Side side) {
		System.err.println("Wrong call for the Login Packet!");
	}
	
	public void doLogin(ServerSide server, InetAddress address, int port) {
		server.login(name, address, port);
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
		this.name_len = buffer.readInt();
		this.name = buffer.readString(name_len);
		return true;
	}
}

