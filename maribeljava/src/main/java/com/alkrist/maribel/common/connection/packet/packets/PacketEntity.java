package com.alkrist.maribel.common.connection.packet.packets;

import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.connection.sides.ClientSide;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.builder.EntityFactory;
import com.alkrist.maribel.common.ecs.builder.EntityID;
import com.alkrist.maribel.common.ecs.builder.GameObjectID;
import com.alkrist.maribel.utils.Logging;

public class PacketEntity extends Packet{

	@SuppressWarnings("rawtypes")
	private static final ComponentMapper GOID_MAPPER = ComponentMapper.getFor(GameObjectID.class);
	@SuppressWarnings("rawtypes")
	private static final ComponentMapper UID_MAPPER = ComponentMapper.getFor(EntityID.class);
	
	public PacketEntity() {
		super("ENTITY");
	}
	public PacketEntity(byte id) {
		super(id, "ENTITY");
	}
	
	Entity entity;
	
	public PacketEntity(byte id, Entity entity) {
		super(id, "ENTITY");
		this.entity = entity;
	}
	
	@Override
	public void process(ClientSide client) {
		EntityID uid = (EntityID) UID_MAPPER.getComponent(entity);
		Client.world.setSnapshotBit(uid.getID());
	}
	
	@Override
	public boolean read(SerialBuffer buffer) {
		super.read(buffer);
		int goid = buffer.readInt();
		entity = EntityFactory.MANAGER.getFactoryFor(goid).readEntity(buffer);	
		return true;
	}
	
	@Override
	public boolean write(SerialBuffer buffer) {
		super.write(buffer);
		if(GOID_MAPPER.hasComponent(entity)) {
			GameObjectID goidComponent = (GameObjectID) GOID_MAPPER.getComponent(entity);
			EntityFactory.MANAGER.getFactoryFor(goidComponent.getID()).writeEntity(entity, buffer);
			return true;
		}
		else {
			Logging.getLogger().log(Level.WARNING, "[PacketEntity]: entity has no game object id bound to it!"); 
			//TODO: add default entity with error goid
			return false;
		}
	}
}
