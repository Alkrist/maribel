package com.alkrist.maribel.common.ecs;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.utils.Bits;



/**
 * Component UID represents an integer ID value of a component and a set of static methods to register it.
 * 
 * @author Mikhail
 *
 */
public class ComponentUID {

	private static 	Map<Class <? extends Component>, ComponentUID> assignedUIDs = 
			new HashMap<Class<? extends Component>, ComponentUID>(); //All component classes assigned with their ids
	
	private static int currentUID = 0;
	
	private int UID;
	
	private ComponentUID() { //Make a new UID for a new component when registrated
		UID = currentUID++;
	}
	
	/**
	 * @return this component class's id value
	 */
	public int getUID() {
		return UID;
	}
	
	/**
	 * get specified component's UID object or create a new one if doesn't exist.
	 * 
	 * @param componentType - component class for desired UID
	 * @return UID object
	 */
	public static ComponentUID getFor(Class<? extends Component> componentType) {
		ComponentUID compUID = assignedUIDs.get(componentType);
		
		if(compUID == null) {
			compUID = new ComponentUID();
			assignedUIDs.put(componentType, compUID);
		}
		
		return compUID;
	}
	
	/**
	 * get specified component's id value from it's UID object or create a new one if doesn't exist.
	 * 
	 * @param componentType - component class for desired id.
	 * @return id value
	 */
	public static int getUIDFor(Class<? extends Component> componentType) {
		return getFor(componentType).getUID();
	}
	
	/**
	 * Creates a bitset for a set of components.
	 * 
	 * Example:
	 * params: comp1 (id=1), comp2 (id=3), comp3 (id=5);
	 * output bitset: [false, true, false, true, false, true]
	 * 
	 * @param componentTypes - set of components
	 * @return bitset of the set of components
	 */
	@SuppressWarnings("unchecked")
	public static Bits getBitsetFor(Class<? extends Component>... componentTypes) {
		Bits bits = new Bits();
		
		int length = componentTypes.length;
		for(int i=0; i<length; i++)
			bits.set(ComponentUID.getUIDFor(componentTypes[i]));
		
		return bits;
	}
	
	
	// ******* OVERRIDEN OBJECT CLASS METHODS *******//
	
	@Override
	public int hashCode() {
		return UID;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) return true;
		if(object == null) return false;	
		if(this.getClass() != object.getClass()) return false;
		ComponentUID uidObject = (ComponentUID) object;
		return this.UID == uidObject.UID;
	}
}
