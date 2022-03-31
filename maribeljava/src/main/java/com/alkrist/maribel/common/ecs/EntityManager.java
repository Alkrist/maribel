package com.alkrist.maribel.common.ecs;

import java.util.ArrayList;

import com.alkrist.maribel.utils.Command;
import com.alkrist.maribel.utils.ImmutableArrayList;


/**
 * Entity Manager is in charge for operating with all entities: getting, adding and removing them.
 * 
 * @author Mikhail
 *
 */
public class EntityManager {

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ImmutableArrayList<Entity> immutableEntities = new ImmutableArrayList<Entity>(entities);
	
	private ArrayList<Command> commands = new ArrayList<Command>();
	
	private Engine engine;
	
	/**
	 * @param engine - an Engine this Manager is attached to
	 */
	public EntityManager(Engine engine) {
		this.engine = engine;
	}
	
	/**
	 * @return all entities' immutable list
	 */
	public ImmutableArrayList<Entity> getAllEntities(){
		return immutableEntities;
	}
	
	/**
	 * Adds an entity to this engine.
	 * 
	 * @param entity - an entity to add
	 * @param delayed - the engine state: delayed means that the Engine is processing a loop at the moment
	 */
	public void addEntity(Entity entity, boolean delayed) {
		if(entity.getEngine()!=null) //TODO: new concept
			throw new IllegalArgumentException("Pending entity is attached to another engine!");
		
		if(delayed)
			commands.add(() -> { addEntityInternal(entity); });
			
		else 
			addEntityInternal(entity);	
	}
	
	/**
	 * Removes the entity from this engine.
	 * 
	 * @param entity - an entity to remove
	 * @param delayed - the engine state: delayed means that the Engine is processing a loop at the moment
	 */
	public void removeEntity(Entity entity, boolean delayed) {
		if(entity.isScheduledForRemoval()) return;
		
		entity.setScheduledForRemoval(true);
		
		if(delayed)
			commands.add(() -> { removeEntityInternal(entity); });		
		
		else 
			removeEntityInternal(entity);		
	}
	
	/**
	 * Remove all entities in the list from the engine.
	 * 
	 * @param batch - entity list which should be removed
	 * @param delayed - the engine state: delayed means that the Engine is processing a loop at the moment
	 */
	public void removeAllEntities(ImmutableArrayList<Entity> batch, boolean delayed) {
		if(delayed) {
			for(Entity entity: batch) {
				if(!entity.isScheduledForRemoval()) { //If entity is NOT scheduled for removal yet
					entity.setScheduledForRemoval(true);
					commands.add(() -> { removeEntityInternal(entity); });
				}
			}
		}
		else {
			while(batch.size() > 0)
				removeEntity(batch.get(0), false);
		}
	}
	
	/**
	 * Remove just all entities from the engine.
	 * 
	 * @param delayed - the engine state: delayed means that the Engine is processing a loop at the moment
	 */
	public void removeAllEntities(boolean delayed) {
		if(delayed) {
			for(Entity entity: entities) {
				if(!entity.isScheduledForRemoval()) { //If entity is NOT scheduled for removal yet
					entity.setScheduledForRemoval(true);
					commands.add(() -> { removeEntityInternal(entity); });
				}
			}
		}
		else {
			while(entities.size() > 0)
				removeEntity(entities.get(0), false);
		}
	}
	
	/**
	 * @return whether this manager has any pending entity operation commands to do
	 */
	public boolean hasPendingCommands() {
		return commands.size() > 0;
	}
	
	/**
	 * Execute all of the pending commands one by one. Typically called in the end of update loop,
	 * after all of the systems were updated.
	 */
	public void processPendingCommands() {
		for(Command command: commands) {
			command.execute();
		}
		commands.clear();
	}
	
	//Add entity on the fly
	protected void addEntityInternal(Entity entity) {
		if(entities.contains(entity))
			throw new IllegalArgumentException("Pending entity already exists!");
		entity.setScheduledForRemoval(false);
		entities.add(entity);
		entity.setEngine(engine); //TODO: new concept
		engine.entityOperation(entity);
	}
	
	//Remove entity on the fly
	protected void removeEntityInternal(Entity entity) {
		if(entities.contains(entity)) {
			entities.remove(entity);
			entity.setEngine(null); //TODO: new concept
			engine.entityOperation(entity);
		}
				
		else throw new IllegalArgumentException("Pending entity does not exist!");
	}
	
	/**
	 * @return the engine this manager is attached to
	 */
	public Engine getEngine() {
		return engine;
	}
}
