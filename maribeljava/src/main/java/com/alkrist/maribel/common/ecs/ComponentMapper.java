package com.alkrist.maribel.common.ecs;

/**
 * The component mapper works as a filter for components which can get the componnet  it's bound to
 * from the abstract entity object.
 * 
 * @author Mikhail
 *
 * @param <T> Component type to which this mapper is bound
 */
public class ComponentMapper <T extends Component>{
	
	private final ComponentUID UID; //mapped id
	
	private ComponentMapper(Class<T> componentClass) {
		UID = ComponentUID.getFor(componentClass);
	}
	
	/**
	 * A factory for new component mapper, creates a new mapper and returns it.
	 * 
	 * @param <T> - Component mapper with a specified component class bound to it
	 * @param componentClass - component class to bind to mapper
	 * @return component mapper with a specified class
	 */
	public static <T extends Component> ComponentMapper<T> getFor(Class<T> componentClass){
		return new ComponentMapper<T>(componentClass);
	}
	
	/**
	 * Retrieves a component object from the entity by it's ID. Typically used
	 * for quicker access from a system.
	 * 
	 * @param entity - container of components
	 * @return component object
	 */
	public T getComponent(Entity entity) {
		return entity.getComponent(UID);
	}
	
	/**
	 * Checks whether the entity has or has not a specified component bound to this mapper.
	 * Typically used for quicker access from a system.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean hasComponent(Entity entity) {
		return entity.hasComponent(UID);
	}
}
