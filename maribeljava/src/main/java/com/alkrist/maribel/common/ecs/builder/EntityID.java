package com.alkrist.maribel.common.ecs.builder;

import com.alkrist.maribel.common.ecs.Component;

/**
 * An entity ID component, stores only one integer value.
 * @author Mikhail
 */
public class EntityID implements Component{
	private int ID;
	
	public EntityID(int id) {
		this.ID = id;
	}
	
	public int getID() {
		return ID;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == EntityID.class) {
			if(((EntityID)o).getID() == ID)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = (this.ID + hash) * 31;
		return hash;
	}
}
