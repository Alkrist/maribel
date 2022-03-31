package com.alkrist.maribel.common.ecs.builder;

import com.alkrist.maribel.common.ecs.Component;

/**
 * This is a Game Object Identifier which made for recognition of different game objects types.
 * (i.e. Oak tree, Iron ingot, Chunk, just any possible type of game object).
 * @author Mikhail
 */
public class GameObjectID implements Component{
	private int GOID;
	
	/**
	 * @param id - Game object ID
	 */
	public GameObjectID(int id) {
		this.GOID = id;
	}
	
	/**
	 * @return game object ID
	 */
	public int getValue() {
		return GOID;
	}
}
