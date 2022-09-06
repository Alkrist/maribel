package com.alkrist.maribel.client.graphics.model;

import com.alkrist.maribel.common.ecs.Component;

/**
 * Model Composite as a Component.
 * 
 * @author Alkrist
 *
 */
public class Model implements Component{

	public ModelComposite model;
	
	public Model(ModelComposite model) {
		this.model = model;
	}
}
