package com.alkrist.maribel.common.connection.packet.packets;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.client.Updater;
import com.alkrist.maribel.client.updatable.scene.GameScene;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ClientSide;

public class PacketLoginReply extends Packet{

	public PacketLoginReply() {
		super("LOGIN_REPLY");
	}
	public PacketLoginReply(byte id) {
		super(id, "LOGIN_REPLY");
	}
	
	private short state;
	
	public PacketLoginReply(byte id, short state) {
		super(id, "LOGIN_REPLY");
		this.state = state;
	}
	
	@Override
	public void process(ClientSide client) {
		if(state == 1) {
			System.out.println("Client connected!");
			if(!(Updater.getActiveElement() instanceof GameScene))
				Updater.setActiveElement(Client.getScene(GameScene.class));
			else Client.startTimers();
		}
			
		else System.out.println("Connection corrupted");
	}
	
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		buffer.writeShort(state);
		return true;
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		state = buffer.readShort();
		return true;
	}
}
