package com.alkrist.maribel.graphics.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.json.JSONObject;

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
 * @author Alkrist
 *
 */
public class ModelComposite {
	
	private String name;
	private Map<String, ModelNode> nodes;
	
	private ModelComposite(String name) {
		this.name = name;
		nodes = new HashMap<String, ModelNode>();
	}
	
	public ModelNode getNode(String nodeName) {
		if(nodes.keySet().size() > 0)
			return nodes.get(nodeName);
		return null;
	}
	
	/**
	 * 
	 * @return a list of node names of this model
	 */
	public Set<String> getNodeNames() {
		return nodes.keySet();
	}
	
	/**
	 * 
	 * @return name of this model
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return amount of nodes of this model
	 */
	public int nodeCount() {
		return nodes.keySet().size();
	}
	
	/**
	 * Adds a new node to this model
	 * 
	 * @param node - new node
	 */
	public void setNode(ModelNode node) {
		nodes.put(node.getName(), node);
	}
	
	/**
	 * Removes a node from this model
	 * 
	 * @param nodeName - node to remove
	 * @return true if node was removed, false if node wasn't found
	 */
	public boolean removeNode(String nodeName) {
		ModelNode node = getNode(nodeName);
		if(node != null) {
			nodes.remove(nodeName);
			return true;
		}return false;
	}
	
	/**
	 * Creates a new Model Composite from existing parts
	 * 
	 * @param name - model name
	 * @param nodes - nodes to add
	 * @return new Model Composite
	 */
	public static ModelComposite create(String name, ModelNode ... nodes) {
		ModelComposite model = new ModelComposite(name);
		for(ModelNode node: nodes) {
			model.setNode(node);
		}
		return model;
	}
	
	/**
	 * Creates a new Model Composite from JSON file
	 * 
	 * @param filename - JSON file path
	 * @param loader - loader to read JSON
	 * @return new Model Composite
	 */
	public static ModelComposite loadFromJson(String filename) {
		
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
			          
			          String materialPath = jsonObject.getJSONObject(key).getString("material");
			          Material material = getMaterial(materialPath);
			          
			          Mesh mesh = OBJLoader.loadOBJmodel(modelPath);
			          
			          if(mesh == null) {
			        	  Logging.getLogger().log(Level.WARNING, "Failed to load model, mesh null");
			        	  return null;
			          }

			         mc.setNode(new ModelNode(mesh, material, key));
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
	
	/**
	 * Creates a new Model Composite from MMC file
	 * 
	 * @param filename - MMC file path
	 * @param loader - loader to read MMC file
	 * @return new Model Composite
	 */
	public static ModelComposite loadFromMMC(String filename) {
		FileReader fr = null;
		
		try {
			fr  = new FileReader(new File(FileUtil.getModelsPath()+filename+".mmc"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace(); //TODO: logging
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		String nodeName = "autho_generated_node_name";
		ModelComposite model = null;
		
		//Material properties
		String materialPath = "generic"; //tx
		Material material = null;
		
		ModelNode node;
		boolean isFirst = true;
		
		boolean indicesRead = false;
		boolean verticesRead = false;
		boolean texturesRead = false;
		boolean normalsRead = false;
		
		String line;		
		List<Float>vertexPoints = new ArrayList<Float>();
		List<Float>texturePoints = new ArrayList<Float>();
		List<Float>normalPoints = new ArrayList<Float>();
		List<Integer>indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] texturesArray = null;
		int[] indicesArray = null;
		int j=0;
		
		try {
			while(true) {
				line = reader.readLine();
				if(line==null) break;
				String[] currentLine = line.split(" ");
				if(line.startsWith("nm ")) {
					model = new ModelComposite(currentLine[1]);
				}else if(line.startsWith("o ")) {
					
					if(!isFirst) { //If it's not a first object
						//Init arrays
						verticesArray = new float[vertexPoints.size()];
						texturesArray = new float[texturePoints.size()];
						normalsArray = new float[normalPoints.size()];
						indicesArray = new int[indices.size()];
						
						//Fill arrays
						j=0;
						for(float point: vertexPoints) {
							verticesArray[j] = point;
							j++;
						}j=0;
						for(float point: texturePoints) {
							texturesArray[j] = point;
							j++;
						}j=0;
						for(float point: normalPoints) {
							normalsArray[j] = point;
							j++;
						}j=0;
						for(int point: indices) {
							indicesArray[j] = point;
							j++;
						}j=0;
						
						//Clear lists
						vertexPoints.clear();
						texturePoints.clear();
						normalPoints.clear();
						indices.clear();
						
						//Create node
						node = makeNode(verticesArray,
								texturesArray, 
								normalsArray, 
								indicesArray, 
								nodeName, 
								material);
						
						//Set node in current model
						model.setNode(node);
						
						//Reset default params
						materialPath = "generic";
						
					}else isFirst = false; //If it was first object, ignore it for now
					
					nodeName = currentLine[1]; //Set new node name
					
				}else if(line.startsWith("mt ")) {
					materialPath = currentLine[1];
					material = getMaterial(materialPath);
				}else if(line.startsWith("vc ")) {
					verticesRead = true;
					indicesRead = false;
					texturesRead = false;
					normalsRead = false;
				}else if(line.startsWith("vn ")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = false;
					normalsRead = true;
				}else if(line.startsWith("vt ")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = true;
					normalsRead = false;
				}else if(line.startsWith("in ")) {
					verticesRead = false;
					indicesRead = true;
					texturesRead = false;
					normalsRead = false;
				}else if(verticesRead) {
					for(int i=0;i<currentLine.length;i++) {
						vertexPoints.add(Float.parseFloat(currentLine[i]));
					}
				}else if(texturesRead) {
					for(int i=0;i<currentLine.length;i++) {
						texturePoints.add(Float.parseFloat(currentLine[i]));
					}
				}else if(normalsRead) {
					for(int i=0;i<currentLine.length;i++) {
						normalPoints.add(Float.parseFloat(currentLine[i]));
					}
				}else if(indicesRead) {
					for(int i=0;i<currentLine.length;i++) {
						indices.add(Integer.parseInt(currentLine[i]));
					}
				}
				
			}
			reader.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			//TODO: logging
		}
		
		//Init arrays
		verticesArray = new float[vertexPoints.size()];
		texturesArray = new float[texturePoints.size()];
		normalsArray = new float[normalPoints.size()];
		indicesArray = new int[indices.size()];
		
		//Fill arrays
		j=0;
		for(float point: vertexPoints) {
			verticesArray[j] = point;
			j++;
		}j=0;
		for(float point: texturePoints) {
			texturesArray[j] = point;
			j++;
		}j=0;
		for(float point: normalPoints) {
			normalsArray[j] = point;
			j++;
		}j=0;
		for(int point: indices) {
			indicesArray[j] = point;
			j++;
		}
		
		//Create node
		node = makeNode(verticesArray,
				texturesArray, 
				normalsArray, 
				indicesArray, 
				nodeName, 
				material);
		
		//Set node in current model
		model.setNode(node);
		
		return model;
	}
	
	private static String readFileAsString(String file)throws Exception{
        return new String(Files.readAllBytes(Paths.get(file)));
    }
	
	private static ModelNode makeNode(float[] vertices, float[] textureCoords, float[] normals, int[] indices, String name,
			Material material) {
		Mesh mesh = ResourceLoader.loadToVAO(vertices, textureCoords, normals, indices);
		
		if(mesh!=null)
			return new ModelNode(mesh, material, name);
		else {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, mesh null");
      	  	return null;
		}
	}
	
	private static Material getMaterial(String name) {
		return Material.loadMaterial(name);
	}
}