package com.alkrist.maribel.common.event;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

/**
 * Represents a master class for the event system.
 * Events are registered and called through the event manager.
 * 
 * @author Mikhail
 */
public class EventManager {
	private EventManager() {}
	
	/**
	 * Calls the event
	 * 
	 * @param event event object
	 */
	public static void callEvent(Event event){
		HandlerList handlers = event.getHandlers();
		RegisteredListener[] listeners = handlers.getRegisteredListeners();
		
		if(listeners != null) {
			for(RegisteredListener listener: listeners) {
				try {
					listener.callEvent(event);
				}catch(Throwable ex) {
					Logging.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName()+" to "
							+listener.getOwner().getClass().getName(), ex);
				}
			}
		}
	}
	
	/**
	 * Register all of the events in a given listener class
	 * 
	 * @param listener listener to register
	 * @param owner the owner object to register
	 */
	public static void registerEvents(Listener listener, Object owner) {
		for(Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry: createRegisteredListeners(listener, owner).entrySet()) {
			getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
		}
	}
	
	/**
	 * Registers the given event to the specified listener using a directly
     * passed EventExecutor
     * 
	 * @param event Event class to register
	 * @param listener A listener implementation to register
	 * @param priority Priority of this event
	 * @param executor EventExecutor to register
	 * @param owner owner object to register
	 * @param ignoreCancelled Do not call executor if event was already cancelled    
	 */
	public static void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor,
			Object owner, boolean ignoreCancelled) {
		getEventListeners(event).register(new RegisteredListener(listener, priority, executor, owner, ignoreCancelled));
	}
	
	/**
	 * Gets the handler list from current event implementation
	 * @param type Event type
	 * @return handler list of this event
	 */
	private static HandlerList getEventListeners(Class <? extends Event> type) {
		try {
			Method method  = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
			method.setAccessible(true);
			return (HandlerList) method.invoke(null); //get the handler list from the event class
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
	}
	
	/**
	 * Get the class object of the given event
	 * @param clazz the event
	 * @return the event's class
	 */
	private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz){
		try {
			clazz.getDeclaredMethod("getHandlerList");
			return clazz;
		}catch(NoSuchMethodException e) {
			if(clazz.getSuperclass()!=null //If for some reason we don't see getHandlerMethod in this class,
					&& !clazz.getSuperclass().equals(Event.class) //We ensure this Event class is inherited from event base class
					&& Event.class.isAssignableFrom(clazz.getSuperclass())) //then we return it as event's subclass
				return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
			else throw new IllegalArgumentException("Unable to find handler list for event "+clazz.getName());
		}			
	}
	
	/**
     * Creates and returns registered listeners for the event classes used in
     * this listener
     *
     * @param listener The object that will handle the eventual call back
     * @param owner the owner object to use when creating registered listeners
     * @return The registered listeners.
     */
	public static Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, final Object owner){
		
		 //A map to return
		 Map<Class<? extends Event>, Set<RegisteredListener>> returnable = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();		 
		 
		 Set<Method> methods;	//Prepare a set of methods	 
		 try {
			 Method[] publicMethods = listener.getClass().getMethods(); //Get all public methods of the listener
			 methods = new HashSet<Method>(publicMethods.length, Float.MAX_VALUE); //Init methods set
			 for(Method method: publicMethods)
				 methods.add(method); // feed the hash set with methods from the listener and it's superclass
			 for (Method method : listener.getClass().getDeclaredMethods())
	             methods.add(method);
		 }catch(NoClassDefFoundError e) { //In case the class does not exist
			 Logging.getLogger().log(Level.SEVERE, "Failed to register events to " 
					 + listener.getClass() + " because " + e.getMessage() + " does not exist.");
			 return returnable;
		 }
		 
		 for(final Method method: methods) {
			 final EventHandler evh = method.getAnnotation(EventHandler.class); 
			 if(evh == null) //If the method is not marked with @EventHandler annotation
				 continue; 
			 Class<?> checkClass; //That'll be a class to check method's parameter type
			 if(method.getParameterTypes().length != 1 //Method should have only one parameter
					 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) { //Parameter type should be instance of Event class
				 Logging.getLogger().log(Level.SEVERE, "Attempted to register an invalid EventHandler method signature \"" 
					 + method.toGenericString() + "\" in " + listener.getClass()); 
				 continue;
			 }
			 final Class <? extends Event> eventClass = checkClass.asSubclass(Event.class); //We use this to find a proper key in the hash set to return
			 method.setAccessible(true);
			 
			 Set<RegisteredListener> eventSet = returnable.get(eventClass);
			 if(eventSet == null) { //If there're no listeners registered yet with this event, add a new one
				 eventSet = new HashSet<RegisteredListener>();
				 returnable.put(eventClass, eventSet);
			 }
			 
			 //**** ADDING A NEW REGISTERED LISTENER TO THE FINAL MAP ****//
			 EventExecutor executor = new EventExecutor() { //Create an instance of event executor to use in Registered listener as a parameter
				 public void execute(Listener listener, Event event) throws EventException{ //Implement the execute method here
					try { //That's why we can keep EventExecutor only as an interface and implement it every time we make a new Registered Listener
						if(!eventClass.isAssignableFrom(event.getClass()))
							return; //If for some reason eventClass is not really inherited from Event
						method.invoke(listener, event); //Fire a method if everything is OK;
					}catch(Throwable t) {
						throw new EventException(t);
					}				
				 }
			 }; //Add a new registeredListener to the current eventSet in the Map to return
			 eventSet.add(new RegisteredListener(listener, evh.priority(), executor, owner, evh.ignoreCancelled()));
		 }
		 return returnable;
	}
}
