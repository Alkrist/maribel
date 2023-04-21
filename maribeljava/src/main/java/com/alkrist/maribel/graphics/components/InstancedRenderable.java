package com.alkrist.maribel.graphics.components;

import java.util.ArrayList;
import java.util.List;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.model.Material;
import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.render.InstancedObject;

public class InstancedRenderable implements Component{

	public Mesh mesh;
	public Material material;
	
	public List<InstancedObject> instances;

	public InstancedRenderable(Mesh mesh, Material material, List<InstancedObject> instances) {
		this.mesh = mesh;
		this.material = material;
		this.instances = instances;
	}
	
	public InstancedRenderable(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
		this.instances = new ArrayList<InstancedObject>();
	}
}
