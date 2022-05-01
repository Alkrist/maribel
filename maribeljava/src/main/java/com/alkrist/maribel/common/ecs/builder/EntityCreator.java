package com.alkrist.maribel.common.ecs.builder;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.ComponentUID;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.ObjectPool;

/**
 * A component that can manipulate with entity creation for a specific engine
 * and register their unique IDs.
 * 
 * It has a pool for entity IDs so when the entity gets deleted, it stores its
 * id here so it can be further reconsumed.
 * 
 * @author Mikhail
 *
 */
public class EntityCreator implements Component {

	private Engine engine;
	private EIDPool pool;

	/**
	 * The constructor needs an engine on which all of the entity operations are
	 * performed.
	 * 
	 * @param engine - Engine
	 */
	public EntityCreator(Engine engine) {
		this.engine = engine;
		pool = new EIDPool();
	}

	/**
	 * Creates an entity of a specific Game Object type (remember that game object
	 * is anything that implements {@link EntityBuilder}.
	 * 
	 * An entity will be attached to the engine and will contain {@link EntityID}.
	 * 
	 * @param goid - Game Object ID
	 * @return new Entity
	 */
	public Entity createEntity(int goid) {
		Entity e = EntityFactory.MANAGER.getFactoryFor(goid).createEntity();
		if (e != null) {
			e.addComponent(pool.consume());
			engine.addEntity(e);
			return e;
		} else
			return null;
	}

	/**
	 * Creates an empty entity without Game Object. Will include only
	 * {@link EntityID}.
	 * 
	 * Entity is attacked to the engine.
	 * 
	 * @return new Entity
	 */
	public Entity createEntity() {
		Entity e = engine.createEntity();
		e.addComponent(pool.consume());
		engine.addEntity(e);
		return e;
	}

	/**
	 * Removes a specitic entity from the engine, if it has {@link EntityID}, it
	 * will be reused in {@link ObjectPool}.
	 * 
	 * @param entity - entity to delete from engine.
	 */
	public void removeEntity(Entity entity) {
		EntityID component = entity.getComponent(ComponentUID.getFor(EntityID.class));
		if (component != null) {
			pool.free(component);
		}
		engine.removeEntity(entity);
	}

	/* A local implementation for entity ID pool */
	private static class EIDPool extends ObjectPool<EntityID> {

		private static int generalID = 1;

		@Override
		public EntityID create() {
			return new EntityID(generalID++);
		}

	}
}
