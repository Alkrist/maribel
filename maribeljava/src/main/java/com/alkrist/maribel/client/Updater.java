package com.alkrist.maribel.client;

import com.alkrist.maribel.client.updatable.Updatable;
import com.alkrist.maribel.client.updatable.scene.SceneBase;

/**
 * Updater is a client side looper. It operates with one target object of {@link engine.client.updatables.Updatable}
 * and updates it every frame, what means as frequent as possible. Client Updater is used for clientside operations
 * that rely on real time and computer's power rather than some fixed timestep (which is used on server side).
 * @author Mikhail
 *
 */
public final class Updater {

	private static boolean running = false;
	private static boolean updating = false;
	
	
	private static Updatable activeElement = null;
	private static Updatable pending = null;
	
	private Updater() {}
	
	/**
	 * Set the current active element
	 * @param updatable - new active element
	 */
	public static synchronized void setActiveElement(Updatable updatable) {
		if(updating)
			pending = updatable;
		else
			setActiveElementInternal(updatable);	
	}
	
	/*Called when the loop is free and the element can be changed*/
	private static void setActiveElementInternal(Updatable updatable) {
		if(activeElement instanceof SceneBase)
			((SceneBase)activeElement).disable();
		
		activeElement = updatable;
		if(updatable instanceof SceneBase) {
			/*if(!scenes.containsValue(updatable)) {
				scenes.put(((SceneBase)updatable).getID(), (SceneBase) updatable);
			}*/
			((SceneBase)updatable).enable();
		}
			
	}
	
	/**
	 * @return current active element
	 */
	public static Updatable getActiveElement() {
		return activeElement;
	}
	
	/**
	 * Enable the Updater so it CAN be updated, but to update it it should be called.
	 */
	public static synchronized void enable() {
		running = true;
	}
	
	/**
	 * Disable the updater so if it works, it shuts down immediately.
	 * So don't mess around with this method somewhere during the runtime
	 */
	public static synchronized void disable() {
		running = false;
	}
	
	private static long deltaTime = 0; //in milliseconds
	
	/**
	 * The actual update loop. it will work only if the Updater is enabled.
	 */
	public static void update() {
		while(running) {
			//Debug purposes 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updating = true;
			if(activeElement!=null) {
				long start = System.currentTimeMillis();
				activeElement.update(deltaTime);
				deltaTime = (System.currentTimeMillis() - start);
			}
				
			updating = false;
			
			
			if(pending != null) {
				setActiveElementInternal(pending);
				pending = null;
			}
		}
	}
}
