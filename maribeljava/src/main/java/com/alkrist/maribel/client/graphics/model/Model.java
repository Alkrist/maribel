package com.alkrist.maribel.client.graphics.model;

import com.alkrist.maribel.common.ecs.Component;

public class Model implements Component{

	public ModelComposite model;
	
	public Model(ModelComposite model) {
		this.model = model;
	}
}
