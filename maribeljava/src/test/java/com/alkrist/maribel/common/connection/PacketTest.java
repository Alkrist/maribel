package com.alkrist.maribel.common.connection;

import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ServerSide;

public class PacketTest extends Packet{

	public PacketTest(byte id) {
		super(id, "TEST");
	}

	public PacketTest() {
		super("TEST");
	}
	
	private String message;
	private String user;
	
	public String getMessage() {
		return message;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public PacketTest(byte id, String user, String message) {
		super(id, "TEST");
		this.message = message;
		this.user = user;
	}
	
	@Override
	public void process(ServerSide server) {
		System.out.println(user+": "+message);
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		int strlen = buffer.readInt();
		this.message = buffer.readString(strlen);
		strlen = buffer.readInt();
		this.user = buffer.readString(strlen);
		return true;
	}
	
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		buffer.writeInt(message.length());
		buffer.writeString(message);
		buffer.writeInt(user.length());
		buffer.writeString(user);
		return true;
	}
}
