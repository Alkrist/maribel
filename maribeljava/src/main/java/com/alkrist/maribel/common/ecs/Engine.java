package com.alkrist.maribel.common.ecs;

import java.util.ArrayList;
import java.util.logging.Level;

import com.alkrist.maribel.utils.Command;
import com.alkrist.maribel.utils.ImmutableArrayList;
import com.alkrist.maribel.utils.Logging;


/**
 * Engine is the central class for the whole ECS. Engine, in simple words, is the updater-manager for all of the
 * Entities, Components, Systems and Families.
 * 
 * Every data/system-related action can be performed only through the Engine.
 * 
 * Engine also has the Update method, where the systems are being updated in the order defined by their priority.
 * 
 * @author Mikhail
 *
 */
public class Engine {

	private EntityManager entityManager = new EntityManager(this);
	private SystemManager systemManager = new SystemManager(this);
	private FamilyManager familyManager = new FamilyManager(entityManager.getAllEntities());
	
	private ArrayList<Command> componentOperations = new ArrayList<Command>();
	
	private boolean updating = false; //Current engine state
	
	/**
	 * Create a new instance of Entity. Called from engine side in memory optimization purposes.
	 * @return new empty Entity object
	 */
	public Entity createEntity() {
		return new Entity(this);
	}
	
	/**
	 * Create a new instance of component. Called from engine side in memory optimization purposes.
	 * To use that method components must have a public no-arg constructor.
	 * 
	 * @param <T> - component class, which implements Component
	 * @param componentClass - component class argument
	 * @return new Component object
	 */
	@SuppressWarnings("deprecation")
	public <T extends Component> T createComponent(Class<T> componentClass) {
		try {
			return componentClass.newInstance();
		}catch(Exception e) {
			Logging.getLogger().log(Level.WARNING, "Invalid call to component class", e);
			return null;
		}
	}
	
	
	// ******* ENTITY MANAGEMENT SECTION *******//
	
	/**
	 * Adds an Entity to this engine.
	 * @param entity - entity to add
	 */
	public void addEntity(Entity entity) {
		entityManager.addEntity(entity, updating);
	}
	
	/**
	 * Removes an entity from this engine.
	 * @param entity - entity to remove
	 */
	public void removeEntity(Entity entity) {
		entityManager.removeEntity(entity, updating);
	}
	
	/**
	 * Removes all entities from this engine.
	 */
	public void removeAllEntities() {
		entityManager.removeAllEntities(updating);
	}
	
	/**
	 * Removes all entities of a specific family.
	 * @param family - family which entities will be removed
	 */
	public void removeAllEntities(Family family) {
		entityManager.removeAllEntities(getEntitiesOf(family), updating);
	}
	
	/**
	 * Get an immutable list of entities that belong to a specific family.
	 * @param family - family which entities belong to
	 * @return ImmutableArrayList of family entities
	 */
	public ImmutableArrayList<Entity> getEntitiesOf(Family family){
		return familyManager.getEntitiesOf(family);
	}
	
	/**
	 * @return ImmutableArrayList of all entities on this engine
	 */
	public ImmutableArrayList<Entity> getAllEntities(){
		return entityManager.getAllEntities();
	}

	
	// ******* SYSTEM MANAGEMENT SECTION *******//
	
	/**
	 * Adds a new system to this engine.
	 * @param system - SystemBase object to add
	 */
	public void addSystem(SystemBase system) {
		systemManager.addSystem(system);
	}
	
	/**
	 * Removes a system from this engine.
	 * @param system - SystemBase object to remove
	 */
	public void removeSystem(SystemBase system) {
		systemManager.removeSystem(system);
	}
	
	/**
	 * Removes all systems from this engine.
	 */
	public void removeAllSystems() {
		systemManager.removeAll();
	}
	
	/**
	 * Get a system from this engine of a specific type.
	 * @param <T> - System class, extends SystemBase
	 * @param systemType - system type argument
	 * @return system object of the system type T
	 */
	public <T extends SystemBase> T getSystem(Class<T> systemType) {
		return systemManager.getSystem(systemType);
	}
	
	/**
	 * @return ImmutableArrayList of all systems on this engine
	 */
	public ImmutableArrayList<SystemBase> getAllSystems(){
		return systemManager.getAllSystems();
	}
	
	
	// ******* UPDATE LOOP *******//
	
	/**
	 * The method which is executed every tick in updater.
	 * 
	 * WARNING: the Update method isn't synchronized by itself, so it's NOT thread-safe, though
	 * some system parts can be synchronized.
	 * 
	 * Here's it's task order:
	 * 1) Check if it's not updated in somewhere else, otherwise throw an error.
	 * 2) Set as updating, so all of the things inside are locked for adding/removing
	 * 3) Update every enabled system inside this engine
	 * 4) Check for pending operations that possibly appeared while processing systems.
	 * 5) Execute pending operations (add/remove entity, add/remove component).
	 * 6) Set updating to false, so the engine is unlocked.
	 * 
	 * @param dt - delta time between last frame and this frame
	 */
	public void update(double dt){
		if(updating)
			throw new IllegalStateException("Can't call update() on updating engine");
		
		updating = true;
		ImmutableArrayList<SystemBase> systems = systemManager.getAllSystems();
		try {
			for(int i=0; i< systems.size(); i++) {
				SystemBase system = systems.get(i);
				if(system.isEnabled())
					system.update(dt);
			}
			
			//Pending commands execute loop
			if(entityManager.hasPendingCommands())
				entityManager.processPendingCommands();
			
			if(!componentOperations.isEmpty()) {
				for(Command command: componentOperations) {
					command.execute();
				}
				componentOperations.clear();
			}
			
		}catch(Exception e) {
			Logging.getLogger().log(Level.WARNING, "An error occured in system update", e);
			e.printStackTrace();
		}finally {updating = false;}	
	}
	
	
	// ******* ENGINE-OPERATIONAL METHODS *******//
	
	//Called when a component was added to the entity. Updates family for this entity
	protected void componentOperation(Entity entity) {
		familyManager.updateFamilyMembership(entity);
	}
	
	//Called when an entity was added/removed. Updates family for this entity
	protected void entityOperation(Entity entity) {
		familyManager.updateFamilyMembership(entity);
	}
	
	public boolean isUpdating() {
		return updating;
	}
	
	public void addComponentOperation(Command command) {
		componentOperations.add(command);
	}
}
