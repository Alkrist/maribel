package com.alkrist.maribel.common.ecs.builder;

import java.util.ArrayList;
import java.util.HashMap;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.Bits;

/**
 * <pre>
 * This class performs all operations on CLIENT game-environment entities, such as:
 * -add
 * -remove
 * -get immutable
 * -update
 * 
 * The World Proxy is fully Client-sided and can NEVER create own entities (all of the client-owned entities
 * are unsigned and created in other classes). 
 * <b>World Proxy stores only copies (proxies) of the server game-environment entities!</b>
 * All add-remove entity operations should be done through this class, outside only unsigned entities should be 
 * controlled. Actually, on client no game-environment entities should be manually manipulated.
 * </pre>
 * @author Mikhail
 *
 */
public class EntityProxy implements Component{

	//TODO: remove entity chains
	
	private HashMap<Integer, Entity> entities; //Links with their IDs
	
	private Bits proxyBits; //entity ids stored on client's world proxy
	private Bits snapshotBits; //entity ids received from server last tick
	
	private Engine engine; //engine this world proxy operates with.
	
	/**
	 * @param engine - client side engine on which this world proxy is currently attached
	 */
	public EntityProxy(Engine engine) {
		entities = new HashMap<Integer, Entity>();
		proxyBits = new Bits();
		snapshotBits = new Bits();
		this.engine = engine;
	}
	
	/**
	 * Add a new entity to the world proxy and engine.
	 * @param entity
	 * @param UID
	 */
	public void addEntity(Entity entity, int UID) {
		synchronized(entities) {
			entities.put(UID, entity);
			proxyBits.set(UID);
			engine.addEntity(entity);
		}		
	}
	
	/**
	 * Remove the entity by it's UID from the world proxy and engine.
	 * @param UID - entity UID
	 */
	private void removeEntity(int UID) {
		synchronized(entities) {
			engine.removeEntity(entities.get(UID));
			entities.remove(UID);
			proxyBits.clear(UID);
		}		
	}
	
	/**
	 * Set the snapshot bit true for this entity's UID.
	 * @param index - entity UID
	 */
	public void setSnapshotBit(int index) {
		synchronized(snapshotBits) {
			snapshotBits.set(index);
		}
	}
	
	/**
	 * Reset all snapshot bits.
	 */
	public void resetSnapshotBits() {
		synchronized(snapshotBits) {
			snapshotBits.clear();
		}	
	}
	
	/**
	 * Check whether this world proxy contains the entity with a specified ID or not.
	 * @param UID - entity UID
	 * @return whether this proxy already has this entity or not
	 */
	public boolean hasEntity(int UID) {
		synchronized(proxyBits) {
			return proxyBits.get(UID);
		}
	}
	
	/**
	 * @param UID - entity UID
	 * @return entity that matches given UID. If there's no such entity, will result in null.
	 */
	public Entity getEntity(int UID) {
		synchronized(entities) {
			return entities.get(UID);
		}		
	}
	
	/**
	 * Loop through proxy entities, match them with last snapshot entities and remove unused ones
	 */
	public void syncEntityCache() {
		ArrayList<Integer> unused = new ArrayList<Integer>();
		for(int id: entities.keySet()) {
			if(!snapshotBits.get(id))
				unused.add(id);
		}
		for(int id: unused)
			removeEntity(id);
	}
	
	public int numOfEntities() {
		return entities.keySet().size();
	}
	
	public void clear() {
		synchronized(entities) {
			for(Entity e: entities.values())
				engine.removeEntity(e);
			entities.clear();
			resetSnapshotBits();
			proxyBits.clear();
		}
	}
}
