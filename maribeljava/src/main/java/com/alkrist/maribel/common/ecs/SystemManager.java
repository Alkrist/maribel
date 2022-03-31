package com.alkrist.maribel.common.ecs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.utils.ImmutableArrayList;


/**
 * Performs all of the system management operations within the engine.
 * 
 * @author Mikhail
 *
 */
public class SystemManager {

	private Engine engine;
	
	private SystemComparator systemComparator = new SystemComparator();
	//Actual system storage which can be modified
	private ArrayList<SystemBase> systems = new ArrayList<SystemBase>();
	//Unmodifiable snapshot of systems: used for get all
	private ImmutableArrayList<SystemBase> immutableSystems = new ImmutableArrayList<SystemBase>(systems);
	//Hash map which maps every system to it's class - used for quicker access to specific system object
	private Map<Class<?>, SystemBase> systemsByClass = new HashMap<Class<?>, SystemBase>();
	
	public SystemManager(Engine engine) {
		this.engine = engine;
	}
	
	/**
	 * Add a specified system to the manager.
	 * 
	 * @param system - system to add
	 */
	public void addSystem(SystemBase system) {
		Class<? extends SystemBase> systemClass = system.getClass();
		SystemBase oldSystem = getSystem(systemClass);
		
		if(oldSystem != null) removeSystem(oldSystem);
		
		systems.add(system);
		systemsByClass.put(systemClass, system);
		systems.sort(systemComparator);
		
		system.setEngine(engine);
		system.addedToEngine(engine);
	}
	
	/**
	 * Remove a specified system from the manager.
	 * 
	 * @param system - system to remove
	 */
	public void removeSystem(SystemBase system) {
		if(systems.remove(system)) {
			systemsByClass.remove(system.getClass());
			
			system.setEngine(null);
			system.removedFromEngine(engine);
		}	
	}
	
	/**
	 * Remove all systems from the manager.
	 */
	public void removeAll() {
		while(systems.size() > 0) {
			removeSystem(systems.get(0));		
		}
	}
	
	/**
	 * @param <T> - system type (extends SystemBase)
	 * @param systemType - system class
	 * @return system object
	 */
	@SuppressWarnings("unchecked")
	public <T extends SystemBase> T getSystem(Class<T> systemType) {
		return (T) systemsByClass.get(systemType);
	}
	
	/**
	 * @return immutable list of all systems
	 */
	public ImmutableArrayList<SystemBase> getAllSystems(){
		return immutableSystems;
	}
	
	//Used to sort systems by their priority: bigger priority goes upwards.
	private static class SystemComparator implements Comparator<SystemBase>{	
		@Override
		public int compare(SystemBase a, SystemBase b) {
			return a.getPriority() > b.getPriority() ? 1 : (a.getPriority() == b.getPriority()) ? 0 : -1;
		}
	}
}
