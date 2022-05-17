package com.alkrist.maribel.client.graphics.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.json.JSONObject;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

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
	
	public static ModelComposite loadFromJson(String filename, BufferObjectLoader loader) {
		
		try {
			
			String contents = readFileAsString(FileUtil.getModelsPath()+filename+".json");
			JSONObject jsonObject = new JSONObject(contents);
			
			String name = jsonObject.getString("name");
			if(name == null) {
				Logging.getLogger().log(Level.WARNING, "Failed to load model, no name");
	        	return null;
			}
			ModelComposite mc = new ModelComposite(name);
			
			Iterator<String> keys = jsonObject.keys();
			
			while(keys.hasNext()) {
				String key = keys.next();
				if (jsonObject.get(key) instanceof JSONObject) {
					  
			          String modelPath = jsonObject.getJSONObject(key).getString("mesh");
			          String texturePath = jsonObject.getJSONObject(key).getString("texture");
			          
			          Mesh mesh = OBJLoader.loadObjModel(modelPath, loader);
			          if(mesh == null) {
			        	  Logging.getLogger().log(Level.WARNING, "Failed to load model, mesh null");
			        	  return null;
			          }
			          
			          Texture texture = Texture.loadTexture(texturePath);
			          if (texture == null) {
			        	  Logging.getLogger().log(Level.WARNING, "Failed to load model, texture null");
			        	  return null;
			          }
			          
			          mc.setNode(new MCPart(mesh, texture, key));
			    }
				
			}
			if(mc.getNodeNames().size() == 0) {
					Logging.getLogger().log(Level.WARNING, "Failed to load model, no nodes found");
		        	return null;
			}
			return mc;
		}catch (Exception e) {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, no contents loaded",e);
        }
		return null;
	}
	
	public static ModelComposite loadFromMMC(String filename) {
		//TODO: implement
		return null;
	}
	
	private static String readFileAsString(String file)throws Exception{
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
