package com.alkrist.maribel.graphics.components;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.model.Material;
import com.alkrist.maribel.graphics.model.Mesh;

public class Renderable implements Component{

	public Mesh mesh;
	public Material material;
	
	public Renderable(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}
}
