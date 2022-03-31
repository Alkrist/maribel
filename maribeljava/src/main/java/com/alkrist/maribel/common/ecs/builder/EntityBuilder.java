package com.alkrist.maribel.common.ecs.builder;

import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.ecs.Entity;


/**
 * <pre>
 * This interface is applied to any possible game object that needs to be once created or reproduced, or sent 
 * from side to another side. Every Game Object is just a set of default values and methods to create and 
 * operate with entity, which is the only product can be recognized by the Engine.
 * Game Objects are easy to describe, hence it never depends on some side methods like "update" or "render",
 * but keep in mind that Game Object is a data storage only.
 * 
 * Entity Builder itself is a set of 3 methods which stand for 3 entity lifetime stages:
 * 1) create entity (define it) [SERVERSIDE]
 * 2) write entity (serialize it for some purpose) [SERVERSIDE]
 * 3) reproduce entity (read the entity from byte stream) [CLIENTSIDE]
 * 
 * Every Game Object has a unique ID (GOID), that represents it's type.
 * WARNING: DO NOT DEFINE GOID IN ENTITY BUILDER, IT'S POINTLESS!!!
 * </pre>
 * @author Mikhail
 *
 */
public interface EntityBuilder {

	/**
	 * Create a new entity with specific components.
	 * DON'T attach GOID manually!
	 * @return created entity
	 */
	public Entity createEntity();
	
	/**
	 * Create a new entity on Client Side from serial buffer.
	 * DON'T attach GOID manually!
	 * @return created entity
	 */
	public Entity reproduceEntity(SerialBuffer buffer);
	
	/**
	 * Update the existing entity components within the client proxy.
	 * @param entity - an entity to update
	 */
	public Entity updateEntity(Entity entity, SerialBuffer buffer);
	/**
	 * Write an entity to the buffer.
	 * DON'T write it's GOID manually!
	 * @param entity - an entity to serialize
	 * @param buffer - serial buffer to write entity to
	 */
	public void serializeEntity(Entity entity, SerialBuffer buffer);
}
