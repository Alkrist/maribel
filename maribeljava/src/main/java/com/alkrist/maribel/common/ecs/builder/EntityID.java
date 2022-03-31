package com.alkrist.maribel.common.ecs.builder;

import com.alkrist.maribel.common.ecs.Component;

/**
 * An entity ID component, stores only one integer value.
 * @author Mikhail
 */
public class EntityID implements Component{
	public int ID;
	
	public EntityID(int id) {
		this.ID = id;
	}
}
