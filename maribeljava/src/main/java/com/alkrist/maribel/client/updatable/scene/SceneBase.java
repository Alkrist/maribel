package com.alkrist.maribel.client.updatable.scene;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.client.updatable.Updatable;
import com.alkrist.maribel.common.ecs.Engine;



public abstract class SceneBase implements Updatable{

	/******* CLASS OWNED INSTANCES *******/
	private static int globalID = 0;
	private static Map<Integer, Class<? extends SceneBase>> sceneKeys = 
			new HashMap<Integer, Class<? extends SceneBase>>();
	
	public static int getIDbyClass(Class<? extends SceneBase> clazz) {
		for(int id: sceneKeys.keySet()) {
			if(sceneKeys.get(id)==clazz)
				return id;
		}
		return -1;
	}
	
	/******* OBJECT OWNED INSTANCES *******/	
	private int ID;
	protected Engine engine;
	
	protected SceneBase() {
		this.setID();
		engine = new Engine();
		create();
	}
	
	public int getID() {
		return ID;
	}
	
	private void setID() {
		for(int id: sceneKeys.keySet()) {
			if(sceneKeys.get(id)==this.getClass())
				return;
		}
		sceneKeys.put(globalID, this.getClass());
		this.ID = globalID;
		globalID++;
	}
	
	public void update(double dt) {
		engine.update(dt);
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	/******* ABSTRACT METHODS FOR FURTHER SUBCLASS IMPLEMENTATION *******/
	public abstract void create();
	public abstract void enable();
	public abstract void disable();
}
