package com.alkrist.maribel.ecs.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketEntity;
import com.alkrist.maribel.common.connection.serialization.Serializer;
import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.ComponentUID;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.builder.EntityCreator;
import com.alkrist.maribel.common.ecs.builder.EntityFactory;
import com.alkrist.maribel.common.ecs.builder.EntityID;
import com.alkrist.maribel.common.ecs.builder.EntityProxy;
import com.alkrist.maribel.common.ecs.builder.GameObjectID;
import com.alkrist.maribel.ecs.TestComponentI;
import com.alkrist.maribel.ecs.TestComponentII;
import com.alkrist.maribel.ecs.TestGameObjectII;

public class TestEntityExchange {

	
	private static Engine serverEngine;
	private static Engine clientEngine;
	
	private static TestGameObjectII gameObject;
	
	private static Entity world;
	private static Entity proxy;
	private static EntityCreator creator;
	private static Serializer serializer;
	
	private static int goid;
	
	@BeforeAll
	public static void init() {
		serverEngine = new Engine();
		clientEngine = new Engine();

		world = serverEngine.createEntity();
		world.addComponent(new EntityCreator(serverEngine));
		creator = world.getComponent(ComponentUID.getFor(EntityCreator.class));
		
		gameObject = new TestGameObjectII(clientEngine);
		
		proxy = clientEngine.createEntity();
		proxy.addComponent(new EntityProxy(clientEngine));
		
		serverEngine.addEntity(world);
		clientEngine.addEntity(proxy);
		
		serializer = new Serializer();
		
		EntityFactory.MANAGER.init(proxy.getComponent(ComponentUID.getFor(EntityProxy.class)), creator);
		try {
			
			EntityFactory.MANAGER.registerEntityFactoryFor(gameObject);
			goid = EntityFactory.MANAGER.getGOIDfor(TestGameObjectII.class);
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		PacketRegistry.registerPacket(new PacketEntity());
	}
	
	@Test
	public void testBuilderMethods() {
		assertNotNull(EntityFactory.MANAGER.getCreator());
		assertNotNull(EntityFactory.MANAGER.getProxy());
		assertNotNull(EntityFactory.MANAGER.getFactoryFor(goid));
	}
	
	@Test
	public void testEntityRecreation() {
		Entity created = EntityFactory.MANAGER.getFactoryFor(goid).createEntity();
		Component c1 = created.getComponent(ComponentUID.getFor(EntityID.class));
		Component c2 = created.getComponent(ComponentUID.getFor(GameObjectID.class));
		Component c3 = created.getComponent(ComponentUID.getFor(TestComponentI.class));
		Component c4 = created.getComponent(ComponentUID.getFor(TestComponentII.class));
		
		assertNotNull(c1);
		assertNotNull(c2);
		assertNotNull(c3);
		assertNotNull(c4);
		
		PacketEntity packet = new PacketEntity(PacketRegistry.getIDFor("ENTITY"), created);
		byte[] data = serializer.encode(packet);
		Packet back = (Packet) serializer.decode(data, Client.packetRegistry);
		
		assertTrue(back instanceof PacketEntity);
		
		Entity recreated = ((PacketEntity)back).entity;
		
		assertNotNull(recreated);
		
		Component r1 = recreated.getComponent(ComponentUID.getFor(EntityID.class));
		Component r2 = recreated.getComponent(ComponentUID.getFor(GameObjectID.class));
		Component r3 = recreated.getComponent(ComponentUID.getFor(TestComponentI.class));
		Component r4 = recreated.getComponent(ComponentUID.getFor(TestComponentII.class));
		
		assertNotNull(r1);
		assertNotNull(r2);
		assertNotNull(r3);
		assertNotNull(r4);
		
	}
}
