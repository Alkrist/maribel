package com.alkrist.maribel.common.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.utils.Bits;
import com.alkrist.maribel.utils.ImmutableArrayList;


/**
 * The Family Manager's whole purpose is to provide family entities for the corresponding family.
 * Entities are taken from the engine.
 * 
 * @author Mikhail
 *
 */
public class FamilyManager {

	ImmutableArrayList<Entity> entities; //All entities reference
	private Map<Family, ArrayList<Entity>> families = new HashMap<Family, ArrayList<Entity>>(); //Mutable lists
	private Map<Family, ImmutableArrayList<Entity>> immutableFamilies =
			new HashMap<Family, ImmutableArrayList<Entity>>(); //Immutable lists
	
	/**
	 * @param entities - all entities in the engine
	 */
	public FamilyManager(ImmutableArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	/**
	 * @param family - Family filter for entities
	 * @return immutable list of entities that match given family
	 */
	public ImmutableArrayList<Entity> getEntitiesOf(Family family){
		return checkFamily(family);
	}
	
	/* Checks the family lists and return it's entities.
	 * 1) try to look up the entity list in existing lists.
	 * 2) If there's no such list, create a new empty one, and per entity update it's family membership.
	 * 3) add the new list to other lists
	 * 4) return the list.
	 */
	private ImmutableArrayList<Entity> checkFamily(Family family){
		ImmutableArrayList<Entity> immutableFamilyEntities = immutableFamilies.get(family);
		
		if(immutableFamilyEntities == null) {
			ArrayList<Entity> familyEntities = new ArrayList<Entity>();
			immutableFamilyEntities = new ImmutableArrayList<Entity>(familyEntities);
			families.put(family,  familyEntities);
			immutableFamilies.put(family,  immutableFamilyEntities);
			
			for(Entity entity: entities) {
				updateFamilyMembership(entity);
			}
		}
		
		return immutableFamilyEntities;
	}
	
	/**
	 * Checks entity's components set and decides if this entity belongs to the specific family or not,
	 * then updates this manager's lists with new conditions.
	 * 
	 * @param entity - an entity to check
	 */
	public void updateFamilyMembership(Entity entity) {	
		for(Family family: families.keySet()) {
			final int familyUID = family.getUID();
			final Bits entityFamilyBits = entity.getFamilyBits();
			
			boolean matches = family.isMember(entity) && !entity.isScheduledForRemoval(); //Whether the entity can be a member of this family
			if(entityFamilyBits.get(familyUID) != matches) { //If entity matches, but not a member, OR doesn't match, but is a member
				
				final ArrayList<Entity> familyEntities = families.get(family);
				if(matches) {
					
					familyEntities.add(entity);
					entityFamilyBits.set(familyUID);
					
				}else {
					
					familyEntities.remove(entity);
					entityFamilyBits.clear(familyUID);
					
				}
			}
		}
	}
}
