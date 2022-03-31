package com.alkrist.maribel.server;

import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.builder.EntityFactory;
import com.alkrist.maribel.common.ecs.builder.EntityID;

public class World {

	private static int globalEntityID = 1;
	private Server server;
	
	public World(Server server) {
		this.server = server;
	}
	
	//Create an entity that is not a game object but has a unique ID (is even needed???)
	public Entity createEntityWithUID() {
		Entity e = server.getEngine().createEntity();
		e.addComponent(new EntityID(globalEntityID));
		globalEntityID++;
		return e;
	}
	
	//Create NEW entity of some specific game object
	public Entity createEntityWithUID(int goid) {
		Entity e = EntityFactory.MANAGER.getFactoryFor(goid).createEntity();
		if(e!=null) {
			e.addComponent(new EntityID(globalEntityID));
			globalEntityID++;
			//System.out.println("Entity created successfuly.");
			return e;
		}else return null;	
	}
}
