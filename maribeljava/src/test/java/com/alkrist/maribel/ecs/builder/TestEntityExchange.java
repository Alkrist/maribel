package com.alkrist.maribel.ecs.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.api.MaribelRegistry;
import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketEntity;
import com.alkrist.maribel.common.connection.serialization.Serializer;
import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.ComponentMapper;
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
import com.alkrist.maribel.ecs.TestComponentIII;
import com.alkrist.maribel.ecs.TestGameObjectI;
import com.alkrist.maribel.ecs.TestGameObjectII;

public class TestEntityExchange {

	
	private static Engine serverEngine;
	private static Engine clientEngine;
	
	private static TestGameObjectII gameObject;
	private static TestGameObjectI otherGameObject;
	
	private static Entity world;
	private static Entity proxy;
	private static EntityCreator creator;
	private static Serializer serializer;
	
	private static int goid, otherGoid;
	@SuppressWarnings("rawtypes")
	private static final ComponentMapper UID_MAPPER = ComponentMapper.getFor(EntityID.class);
	@SuppressWarnings("rawtypes")
	private static final ComponentMapper GOID_MAPPER = ComponentMapper.getFor(GameObjectID.class);
	
	@BeforeAll
	public static void init() {
		serverEngine = new Engine();
		clientEngine = new Engine();

		world = serverEngine.createEntity();
		world.addComponent(new EntityCreator(serverEngine));
		creator = world.getComponent(ComponentUID.getFor(EntityCreator.class));
		
		gameObject = new TestGameObjectII(clientEngine);
		otherGameObject = new TestGameObjectI(clientEngine);
		
		proxy = clientEngine.createEntity();
		proxy.addComponent(new EntityProxy(clientEngine));
		
		serverEngine.addEntity(world);
		clientEngine.addEntity(proxy);
		
		serializer = new Serializer();
		
		EntityFactory.MANAGER.init(proxy.getComponent(ComponentUID.getFor(EntityProxy.class)), creator);
		try {
			
			EntityFactory.MANAGER.registerEntityFactoryFor(gameObject);
			EntityFactory.MANAGER.registerEntityFactoryFor(otherGameObject);
			goid = EntityFactory.MANAGER.getGOIDfor(TestGameObjectII.class);
			otherGoid = EntityFactory.MANAGER.getGOIDfor(TestGameObjectI.class);
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		MaribelRegistry.registerPackets();
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
	
	@Test
	public void proxyBehaviourTest() {
		creator.clear();
		int k = 0;
		List<Entity> entities = new ArrayList<Entity>();
		EntityProxy eProxy = proxy.getComponent(ComponentUID.getFor(EntityProxy.class));
		eProxy.clear();
		assertEquals(0, eProxy.numOfEntities());
		
		for(int i=0;i<10; i++) {
			k++;
			Entity e = EntityFactory.MANAGER.getFactoryFor(goid).createEntity();
			entities.add(e);
			PacketEntity packet = new PacketEntity(PacketRegistry.getIDFor("ENTITY"), e);
			byte[] data = serializer.encode(packet);
			Packet back = (Packet) serializer.decode(data, Client.packetRegistry);
		}
		assertNotNull(eProxy);
		assertEquals(k, eProxy.numOfEntities());
		
		creator.removeEntity(entities.get(0));
		creator.removeEntity(entities.get(1));
		
		eProxy.resetSnapshotBits();
		for(int i=2; i<10; i++) {
			PacketEntity packet = new PacketEntity(PacketRegistry.getIDFor("ENTITY"), entities.get(i));
			byte[] data = serializer.encode(packet);
			Packet back = (Packet) serializer.decode(data, Client.packetRegistry);
			EntityID uid = (EntityID) UID_MAPPER.getComponent(((PacketEntity)back).entity);
			eProxy.setSnapshotBit(uid.getID());
		}
		eProxy.syncEntityCache();
		assertEquals(k-2, eProxy.numOfEntities());
		
		EntityID id0 = (EntityID) UID_MAPPER.getComponent(entities.get(0));
		EntityID id1 = (EntityID) UID_MAPPER.getComponent(entities.get(1));
		EntityID id2 = (EntityID) UID_MAPPER.getComponent(entities.get(2));
		
		creator.removeEntity(entities.get(2));
		for(int i=0;i<3;i++) {
			Entity e = EntityFactory.MANAGER.getFactoryFor(otherGoid).createEntity();
			entities.set(i, e);
		}
		assertEquals(id0.getID(), ((EntityID) UID_MAPPER.getComponent(entities.get(2))).getID());
		assertEquals(id1.getID(), ((EntityID) UID_MAPPER.getComponent(entities.get(1))).getID());
		assertEquals(id2.getID(), ((EntityID) UID_MAPPER.getComponent(entities.get(0))).getID());
		
		eProxy.resetSnapshotBits();
		for(int i=0; i<10; i++) {
			PacketEntity packet = new PacketEntity(PacketRegistry.getIDFor("ENTITY"), entities.get(i));
			byte[] data = serializer.encode(packet);
			Packet back = (Packet) serializer.decode(data, Client.packetRegistry);
			EntityID uid = (EntityID) UID_MAPPER.getComponent(((PacketEntity)back).entity);
			eProxy.setSnapshotBit(uid.getID());
		}
		eProxy.syncEntityCache();
		assertEquals(k, eProxy.numOfEntities());
		
		int actualGOID0 = ((GameObjectID) GOID_MAPPER.getComponent(eProxy.getEntity(id0.getID()))).getID();
		int actualGOID1 = ((GameObjectID) GOID_MAPPER.getComponent(eProxy.getEntity(id1.getID()))).getID();
		int actualGOID2 = ((GameObjectID) GOID_MAPPER.getComponent(eProxy.getEntity(id2.getID()))).getID();
		
		assertEquals(actualGOID0, otherGoid);
		assertEquals(actualGOID1, otherGoid);
		assertEquals(actualGOID2, otherGoid);
		
		assertTrue(eProxy.getEntity(id0.getID()).hasComponent(ComponentUID.getFor(TestComponentI.class)));
		assertTrue(eProxy.getEntity(id0.getID()).hasComponent(ComponentUID.getFor(TestComponentIII.class)));
		
		assertTrue(eProxy.getEntity(id1.getID()).hasComponent(ComponentUID.getFor(TestComponentI.class)));
		assertTrue(eProxy.getEntity(id1.getID()).hasComponent(ComponentUID.getFor(TestComponentIII.class)));
		
		assertTrue(eProxy.getEntity(id2.getID()).hasComponent(ComponentUID.getFor(TestComponentI.class)));
		assertTrue(eProxy.getEntity(id2.getID()).hasComponent(ComponentUID.getFor(TestComponentIII.class)));
		assertTrue(!eProxy.getEntity(id2.getID()).hasComponent(ComponentUID.getFor(TestComponentII.class)));
	}
}
