package com.alkrist.maribel.common.ecs.builder;

import java.util.ArrayList;
import java.util.List;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;

public class EntityNode implements Component{

	private static final ComponentMapper<EntityNode> NODE_MAPPER = ComponentMapper.getFor(EntityNode.class);
	
	private Entity parent;
	private List<Entity> children;
	
	public EntityNode(Entity parent, List<Entity> children) {
		this.parent = parent;
		this.children = children;
	}
	
	public EntityNode(Entity parent) {
		this.parent = parent;
	}
	
	public List<Entity> getChildren(){
		return children;
	}
	
	public Entity getParent() {
		return parent;
	}
	
	public void addChild(Entity child) {
		if(children == null)
			children = new ArrayList<Entity>();
		children.add(child);
	}
	
	public boolean removeChild(Entity child) {
		if(children != null) {
			if(children.contains(child)) {
				children.remove(child);
				if(children.isEmpty())
					children = null;
				return true;
			}
		}
		return false;
	}
	
	public void removeAllChildren() {
		if(children != null) {
			for(Entity e: children) {
				EntityNode node = NODE_MAPPER.getComponent(e);
				if(node != null)
					node.removeAllChildren();
			}
			children.clear();
			children = null;
		}
	}
}
