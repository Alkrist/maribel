package com.alkrist.maribel.common.ecs;

import java.util.ArrayList;

import com.alkrist.maribel.utils.Bag;
import com.alkrist.maribel.utils.Bits;
import com.alkrist.maribel.utils.ImmutableArrayList;


/**
 * Entity class is a container for components and some data related to them.
 * Entity is a base form for the whole game structure, there can exist the concrete classes for game objects,
 * but on the core level game engine can work with entities ONLY, regardless of the game object type, entity can
 * vary only with their component sets and TODO their unique IDs. 
 * 
 * Entities use IDs only due to the sided objects recognition issue, in order to simplify understanding of which
 * object to update.
 * 
 * Components are stored in the {@link utils.Bag}, the whole set can be passed as {@link utils.ImmutableArrayList}.
 * Also contains two bitsets, one for the components, and another one for the families this entity refers to.
 * 
 * @author Mikhail
 *
 */
public class Entity {

	//TODO: add entity ID.
	private Engine engine; //new concept
	
	private Bag<Component> components;	
	private ArrayList<Component> componentsList;
	private ImmutableArrayList<Component> immutableComponents;
	
	private Bits componentBits;
	private Bits familyBits;
	
	private boolean scheduledForRemoval = false;
	
	//Constructor can be called only from the engine in memory allocation purposes.
	protected Entity(Engine engine) {
		engine = null; //new concept
		components = new Bag<Component>();
		
		componentsList = new ArrayList<Component>();
		immutableComponents = new ImmutableArrayList<Component>(componentsList);
		
		componentBits = new Bits();
		familyBits = new Bits();
	}
	
	protected void addComponentInternal(Component component) {
		Class<? extends Component> componentClass = component.getClass();
		Component existing = getComponent(componentClass);
		
		if(component != existing) {
			if(existing != null)
				removeComponentInternal(componentClass);
			
			int UID = ComponentUID.getUIDFor(componentClass);
			components.set(UID, component);
			componentsList.add(component);
			componentBits.set(UID); 
			if(engine!=null) engine.componentOperation(this); //new concept
		}
		
	}
	
	/**
	 * Adds a new component object to this entity.
	 * 
	 * @param component - component object
	 */
	public void addComponent(Component component) {
		if(engine!=null) {
			if(engine.isUpdating()) {
				engine.addComponentOperation(() -> {addComponentInternal(component);});
			}else addComponentInternal(component);
		}else {
			addComponentInternal(component);
		}
	}
	
	protected Component removeComponentInternal(Class<? extends Component> componentClass) {
		int UID = ComponentUID.getUIDFor(componentClass);
		System.out.println(componentClass.getSimpleName()+" (test line in removeComponentInternal)");
		if(components.isIndexWithinBounds(UID)) {
			Component compToRemove = components.get(UID);
			
			if(compToRemove != null) {
				components.set(UID, null); //Preserves the order in Component bag
				componentsList.remove(compToRemove);
				componentBits.clear(UID);
				if(engine!=null) engine.componentOperation(this); //new concept
				return compToRemove;
			}
			
		}
		
		return null;
	}
	
	/**
	 * Remove and return a component of a certain type from the entity.
	 * 
	 * @param componentClass - component type
	 * @return removed component
	 */
	public void removeComponent(Class<? extends Component> componentClass) {
		if(engine!= null) {
			if(engine.isUpdating()) {
				engine.addComponentOperation(() -> {removeComponentInternal(componentClass);});
			}else removeComponentInternal(componentClass);
		}else {
			removeComponentInternal(componentClass);
		}
	}
	
	/**
	 * Cleans up this entity's components set.
	 */
	public void removeAllComponents() {		
		if(engine!=null) {
			if(engine.isUpdating()) {
				engine.addComponentOperation(() -> {
					while(componentsList.size() > 0)
						removeComponent(componentsList.get(0).getClass());
				});		
			}else {
				while(componentsList.size() > 0)
					removeComponent(componentsList.get(0).getClass());	
			}
		}else {
			while(componentsList.size() > 0)
				removeComponent(componentsList.get(0).getClass());
		}
			
	}
	
	/**
	 * @param <T> - component's type
	 * @param - componentClass - searched component's class
	 * @return component of the class <T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T>  componentClass) {
		int UID = ComponentUID.getUIDFor(componentClass);
		
		if(UID < components.getCapacity()) {
			return (T) components.get(UID);
		}
		else return null;
	}
	
	/**
	 * 
	 * @param <T> - component's type
	 * @param compUID - searched component type's ID
	 * @return component of the class <T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(ComponentUID compUID) {
		int UID = compUID.getUID();
		if(UID < components.getCapacity())
			return (T) components.get(UID);
		else return null;
	}
	
	/**
	 * Check whether the entity has a component of specific type.
	 * 
	 * Keep in mind:
	 * Firstly tries to assign component's class with it's ID, hence this method is O(log(N)) complexity.
	 * 
	 * @param componentClass - target component's class
	 * @return this entity has component or not
	 */
	public boolean hasComponent(Class<? extends Component> componentClass) {
		return componentBits.get(ComponentUID.getUIDFor(componentClass));
	}
		
	/**
	 * Checks whether the entity has a component of specific type.
	 * 
	 * Keep in mind:
	 * Directly refers to the component type by the given ID, hence this method has O(1) complexity.
	 * 
	 * @param compUID - searched component type's ID
	 * @return this entity has component or not
	 */
	public boolean hasComponent(ComponentUID compUID) {
		return componentBits.get(compUID.getUID());
	}
	
	/**
	 * @return immutable list of this entity's component objects
	 */
	public ImmutableArrayList<Component> getComponents() {
		return immutableComponents;
	}
	
	/**
	 * @return a bitset assigned for this entity's component set.
	 */
	public Bits getComponentBits() {
		return componentBits;
	}
	
	/**
	 * @return a bitset for family relations of this entity
	 */
	public Bits getFamilyBits() {
		return familyBits;
	}

	/**
	 * @return whether this entity is waiting to be removed
	 */
	public boolean isScheduledForRemoval() {
		return scheduledForRemoval;
	}
	
	/**
	 * Marks the entity to be scheduled for removal.
	 * 
	 * @param scheduledForRemoval - state
	 */
	protected void setScheduledForRemoval(boolean scheduledForRemoval) {
		this.scheduledForRemoval = scheduledForRemoval;
	}

	public Engine getEngine() { //new concept
		return engine;
	}

	protected void setEngine(Engine engine) { //new concept
		this.engine = engine;
	}
}
