package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.ecs.ComponentUID;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.builder.EntityBuilder;

public class TestGameObjectI implements EntityBuilder{

	private Engine clientEngine;
	
	public TestGameObjectI(Engine cEngine) {
		clientEngine = cEngine;
	}
	
	@Override
	public Entity createEntity(Entity e) {
		e.addComponent(new TestComponentI(1,2,3,"Maribel"));
		e.addComponent(new TestComponentIII("test", 1.3f));
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
		
		int strlen2 = buffer.readInt();
		String data2 = buffer.readString(strlen2);
		float fval = buffer.readFloat();
		e.addComponent(new TestComponentIII(data2,fval));
		return e;
	}

	@Override
	public Entity updateEntity(Entity entity, SerialBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int strlen = buffer.readInt();
		String data = buffer.readString(strlen);
		int strlen2 = buffer.readInt();
		String data2 = buffer.readString(strlen2);
		float fval = buffer.readFloat();
		TestComponentI cmpI;
		TestComponentIII cmpIII;
		
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
		
		if(entity.hasComponent(ComponentUID.getFor(TestComponentIII.class))) {
			cmpIII = entity.getComponent(ComponentUID.getFor(TestComponentII.class));
			cmpIII.data = data2;
			cmpIII.floatNumber = fval;
			
		}else {
			cmpIII = new TestComponentIII(data2, fval);
			entity.addComponent(cmpIII);
		}
		
		return entity;
	}

	@Override
	public void serializeEntity(Entity entity, SerialBuffer buffer) {
		TestComponentI cmpI;
		TestComponentIII cmpIII;
		if(entity.hasComponent(ComponentUID.getFor(TestComponentI.class))) {
			cmpI = entity.getComponent(ComponentUID.getFor(TestComponentI.class));
			buffer.writeInt(cmpI.x);
			buffer.writeInt(cmpI.y);
			buffer.writeInt(cmpI.z);
			buffer.writeInt(cmpI.data.length());
			buffer.writeString(cmpI.data);
		}
		
		if(entity.hasComponent(ComponentUID.getFor(TestComponentIII.class))) {
			cmpIII = entity.getComponent(ComponentUID.getFor(TestComponentIII.class));
			buffer.writeInt(cmpIII.data.length());
			buffer.writeString(cmpIII.data);
			buffer.writeFloat(cmpIII.floatNumber);
		}
	}

}
