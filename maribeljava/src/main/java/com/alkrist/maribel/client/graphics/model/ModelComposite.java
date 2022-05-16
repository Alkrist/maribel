package com.alkrist.maribel.client.graphics.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maribel Model Composite.
 * 
 * Multipart model represented as a graph of nodes - parts, each part contains one mesh, one texture and one
 * set of material properties.
 * 
 * Each node is linked by it's name.
 * Nodes can be added and removed.
 * Names of nodes must be unique
 * 
 * @author Mikhail
 *
 */
public class ModelComposite {

	//TODO: ways to create an MMC object
	//TODO: read mmc file
	//TODO: read obj and json
	private String name;
	private Map<String, MCPart> nodes;
	
	private ModelComposite(String name) {
		nodes = new HashMap<String, MCPart>();
	}
	
	public MCPart getNode(String nodeName) {
		if(nodes.keySet().size() > 0)
			return nodes.get(nodeName);
		return null;
	}
	
	public Set<String> getNodeNames() {
		return nodes.keySet();
	}
	
	public String getName() {
		return name;
	}
	
	public int nodeCount() {
		return nodes.keySet().size();
	}
	
	public void setNode(MCPart node) {
		nodes.put(node.getName(), node);
	}
	
	public boolean removeNode(String nodeName) {
		MCPart node = getNode(nodeName);
		if(node != null) {
			nodes.remove(nodeName);
			return true;
		}return false;
	}
	
	public static ModelComposite create(String name, MCPart ... nodes) {
		ModelComposite model = new ModelComposite(name);
		for(MCPart node: nodes) {
			model.setNode(node);
		}
		return model;
	}
	
	public static ModelComposite loadFromJson(String filename) {
		//TODO: implement
		return null;
	}
	
	public static ModelComposite loadFromMMC(String filename) {
		//TODO: implement
		return null;
	}
}
