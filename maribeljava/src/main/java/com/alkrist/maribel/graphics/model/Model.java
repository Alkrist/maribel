package com.alkrist.maribel.graphics.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private Mesh mesh;
	private Material material;
	private String name;
	
	private Model parent;
	private List<Model> children;
	
	public Model(Mesh mesh, Material material, String name) {
		this.mesh = mesh;
		this.material = material;
		this.name = name;
	}

	public Model(String name) {
		this.name = name;
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public Model getParent() {
		return parent;
	}

	public List<Model> getChildren() {
		return children;
	}
	
	public Model getChild(String name) {
		if(this.name.equals(name)) {
			return this;
		}else if(children != null){
			for(Model child: children) {
				Model result = child.getChild(name);
				if(result != null) {
					return result;
				}
			}
		}return null;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setParent(Model parent) {
		this.parent = parent;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void addChild(Model child) {
		if(child != null) {
			if(children == null)
				children = new ArrayList<Model>();
			children.add(child);
		}
	}
	
	@Override
	public String toString() {
		String childrenStr = "";
		if(children != null) {
			for(Model m: children) {
				childrenStr+=m.toString();
			}			
		}
		return "[name: " + name
				+", mesh: "+ mesh.toString()
				+", material: "+material.getName()
				+"\nchildern: "+childrenStr+"\n";
	}
}
