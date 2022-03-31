package com.alkrist.maribel.common.event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

/**
 * A list of event handlers, stored per-event. Based on lahwran's fevents.
 */
public class HandlerList {

	/**
     * List of all HandlerLists which have been created, for use in bakeAll()
     */
	private static ArrayList<HandlerList> ALL_LISTS = new ArrayList<HandlerList>();
	
	/**
     * Handler array. This field being an array is the key to this system's
     * speed.
     */
	private RegisteredListener[] handlers = null;
	
	/**
     * Dynamic handler lists. These are changed using register() and
     * unregister() and are automatically baked to the handlers array any time
     * they have changed.
     */
	private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerSlots;
	
	/**
	 * Creates a new handler list and initializes it using the event priory order
	 */
	public HandlerList() {
		handlerSlots = new EnumMap<EventPriority, ArrayList<RegisteredListener>>(EventPriority.class);
		for(EventPriority value: EventPriority.values()) { //The key for baking and array properly is
			handlerSlots.put(value, new ArrayList<RegisteredListener>()); //preinitialized order of entries
		}
		ALL_LISTS.add(this);
	}
	
	/**
	 * Bake all handler lists. Best used right after the normal registration is complete.
	 */
	public static void bakeAll() {
		for(HandlerList list: ALL_LISTS) {
			list.bake();
		}
	}
	
	/**
	 * Unregister all listeners from all handler lists.
	 */
	public void unregisterAll() {
		for(HandlerList h: ALL_LISTS) {
			for(List<RegisteredListener> list: h.handlerSlots.values()) {
				list.clear();
			}
			h.handlers = null;
		}
	}
	
	/**
     * Unregister a specific owner's listeners from all handler lists.
     *
     * @param owner owner object to unregister
     */
	public void unregisterAll(Object owner) {
		for(HandlerList h: ALL_LISTS) {
			h.unregister(owner);
		}
	}
	
	/**
	 * Register a new listener in this handler list.
	 * 
	 * @param listener A listener to register
	 */
	public void register(RegisteredListener listener) {
		if(handlerSlots.get(listener.getPriority()).contains(listener))
			throw new IllegalStateException("This Listener is already registered to that priority: "
					+listener.getPriority().toString());
		handlers = null; //Must be done to bake handlers afterwards
		handlerSlots.get(listener.getPriority()).add(listener);
	}
	
	/**
     * Register a collection of new listeners in this handler list.
     *
     * @param listeners listeners to register
     */
	public void registerAll(Collection<RegisteredListener> listeners) {
		for(RegisteredListener listener: listeners) {
			register(listener);
		}
	}
	
	/**
     * Remove a listener from a specific order slot
     *
     * @param listener listener to remove
     */
	public void unregister(RegisteredListener listener) {
		if(handlerSlots.get(listener.getPriority()).remove(listener))
			handlers = null; //Must be done to bake handlers afterwards
	}
	
	/**
     * Remove a specific object's owned listeners from this handler
     *
     * @param owner owner to remove
     */
	public void unregister(Object owner) {
		boolean changed = false;
		for(List<RegisteredListener> list: handlerSlots.values()) {
			for(ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
				if(i.next().getOwner().equals(owner)) {
					i.remove();
					changed = true;
				}					
			}
		}
		if (changed) handlers = null;
	}
	
	/**
     * Remove a specific listener from this handler
     *
     * @param listener listener to remove
     */
	public void unregister(Listener listener) {
		boolean changed = false; //Will control whether any changes were applied to the list or not.
		for(List<RegisteredListener> list: handlerSlots.values()) {
			for(ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
				if(i.next().getListener().equals(listener)) {
					i.remove();
					changed = true;
				}
			}
		}
		if(changed) handlers = null; ////Must be done to bake handlers afterwards
	}
	
	/**
	 * Get the baked registered listeners associated with this handler list
	 * 
	 * @return handlers as an array
	 */
	public RegisteredListener[] getRegisteredListeners() {
		RegisteredListener[] handlers;
		while((handlers = this.handlers) == null)
			bake();
		return handlers;
	}
	
	/**
	 * Get a specific owner's registered listeners associated with this
     * handler list
	 * 
	 * @param owner owner object to get the listeners of
	 * @return the list of registered listeners
	 */
	public static ArrayList<RegisteredListener> getRegisteredListeners(Object owner){
		 ArrayList<RegisteredListener> listeners = new ArrayList<RegisteredListener>();
		 for(HandlerList h: ALL_LISTS) {
			 for (List<RegisteredListener> list : h.handlerSlots.values()) {
				 for (RegisteredListener listener : list) {
					 if(listener.getOwner().equals(owner))
						 listeners.add(listener);
				 }
			 }
		 }
		 return listeners;
	}
	
	/**
	 * Bake HashMap handlerSlots to an array, does nothing if not necessary.
	 */
	public void bake() {
		if(handlers != null)  //Do not re-bake when still be valid
			return;
		
		ArrayList<RegisteredListener> entries = new ArrayList<RegisteredListener>();
		for(Entry<EventPriority, ArrayList<RegisteredListener>> entry: handlerSlots.entrySet())
			entries.addAll(entry.getValue());
		
		handlers = entries.toArray(new RegisteredListener[entries.size()]);
	}
}
