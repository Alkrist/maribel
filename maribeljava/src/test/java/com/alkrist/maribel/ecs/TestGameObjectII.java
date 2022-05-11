package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.ecs.ComponentUID;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.builder.EntityBuilder;
import com.alkrist.maribel.common.ecs.builder.EntityCreator;

public class TestGameObjectII implements EntityBuilder{

	private Engine clientEngine;
	
	public TestGameObjectII(Engine cEngine) {
		clientEngine = cEngine;
	}
	
	@Override
	public Entity createEntity(Entity e) {
		e.addComponent(new TestComponentI(1,2,3,"Maribel"));
		e.addComponent(new TestComponentII(1234, 2.5f));
		return e;
	}

	@Override
	public Entity reproduceEntity(SerialBuffer buffer) {
		Entity e = clientEngine.createEntity();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int strlen = buffer.readInt();
		String data = buffer.readString(strlen);
		e.addComponent(new TestComponentI(x,y,z,data));
		int val = buffer.readInt();
		float fval = buffer.readFloat();
		e.addComponent(new TestComponentII(val,fval));
		return e;
	}

	@Override
	public Entity updateEntity(Entity entity, SerialBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int strlen = buffer.readInt();
		String data = buffer.readString(strlen);
		int val = buffer.readInt();
		float fval = buffer.readFloat();
		TestComponentI cmpI;
		TestComponentII cmpII;
		
		if(entity.hasComponent(ComponentUID.getFor(TestComponentI.class))) {
			cmpI = entity.getComponent(ComponentUID.getFor(TestComponentI.class));
			cmpI.x = x;
			cmpI.y = y;
			cmpI.z = z;
			cmpI.data = data;
		}else {
			cmpI = new TestComponentI(x,y,z,data);
			entity.addComponent(cmpI);
		}
		
		if(entity.hasComponent(ComponentUID.getFor(TestComponentII.class))) {
			cmpII = entity.getComponent(ComponentUID.getFor(TestComponentII.class));
			cmpII.number = val;
			cmpII.floatNumber = fval;
			
		}else {
			cmpII = new TestComponentII(val, fval);
			entity.addComponent(cmpII);
		}
		
		return entity;
	}

	@Override
	public void serializeEntity(Entity entity, SerialBuffer buffer) {
		TestComponentI cmpI;
		TestComponentII cmpII;
		if(entity.hasComponent(ComponentUID.getFor(TestComponentI.class))) {
			cmpI = entity.getComponent(ComponentUID.getFor(TestComponentI.class));
			buffer.writeInt(cmpI.x);
			buffer.writeInt(cmpI.y);
			buffer.writeInt(cmpI.z);
			buffer.writeInt(cmpI.data.length());
			buffer.writeString(cmpI.data);
		}
		
		if(entity.hasComponent(ComponentUID.getFor(TestComponentII.class))) {
			cmpII = entity.getComponent(ComponentUID.getFor(TestComponentII.class));
			buffer.writeInt(cmpII.number);
			buffer.writeFloat(cmpII.floatNumber);
		}
	}

}
