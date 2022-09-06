package com.alkrist.maribel.client.graphics.model;

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
 * @author Alkrist
 *
 */
public class ModelComposite {
	
	private String name;
	private Map<String, MCPart> nodes;
	
	private ModelComposite(String name) {
		this.name = name;
		nodes = new HashMap<String, MCPart>();
	}
	
	public MCPart getNode(String nodeName) {
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
	public void setNode(MCPart node) {
		nodes.put(node.getName(), node);
	}
	
	/**
	 * Removes a node from this model
	 * 
	 * @param nodeName - node to remove
	 * @return true if node was removed, false if node wasn't found
	 */
	public boolean removeNode(String nodeName) {
		MCPart node = getNode(nodeName);
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
	public static ModelComposite create(String name, MCPart ... nodes) {
		ModelComposite model = new ModelComposite(name);
		for(MCPart node: nodes) {
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
					  
					  float shine = 1;
					  float reflex = 0;
					  boolean trans = false;
					  String material = "GENERIC";
					  
			          String modelPath = jsonObject.getJSONObject(key).getString("mesh");
			          String texturePath = jsonObject.getJSONObject(key).getString("texture");
			          
			          material = jsonObject.getJSONObject(key).getString("material");
			          trans = jsonObject.getJSONObject(key).getBoolean("transparency");
			          reflex = jsonObject.getJSONObject(key).getFloat("reflexivity");
			          shine = jsonObject.getJSONObject(key).getFloat("shineDamper");
			          
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
			          
			          mc.setNode(new MCPart(mesh, texture, key, shine, reflex, material, trans));
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
	public static ModelComposite loadFromMMC(String filename, BufferObjectLoader loader) {
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
		String textureName = null; //tx
		float shineDamper = 1; //sd
		float reflexivity = 0; //rf
		boolean isTransparent = false; //tr
		String material = "GENERIC"; //mt
		
		MCPart node;
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
								textureName, 
								shineDamper, 
								reflexivity, 
								isTransparent, 
								material, 
								loader);
						
						//Set node in current model
						model.setNode(node);
						
						//Reset default params
						shineDamper = 1;
						reflexivity = 0;
						material = "GENERIC";
						isTransparent = false;
						
					}else isFirst = false; //If it was first object, ignore it for now
					
					nodeName = currentLine[1]; //Set new node name
					
				}else if(line.startsWith("sd")) {
					shineDamper = Float.parseFloat(currentLine[1]);
				}else if(line.startsWith("rf ")) {
					reflexivity = Float.parseFloat(currentLine[1]);
				}else if(line.startsWith("tx ")) {
					textureName = currentLine[1];
				}else if(line.startsWith("tr ")) {
					if(currentLine[1].startsWith("1")) isTransparent = true;
				}else if(line.startsWith("mt ")) {
					material = currentLine[1].toUpperCase();
				}else if(line.startsWith("vc")) {
					verticesRead = true;
					indicesRead = false;
					texturesRead = false;
					normalsRead = false;
				}else if(line.startsWith("vn")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = false;
					normalsRead = true;
				}else if(line.startsWith("vt")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = true;
					normalsRead = false;
				}else if(line.startsWith("in")) {
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
				textureName, 
				shineDamper, 
				reflexivity, 
				isTransparent, 
				material, 
				loader);
		
		//Set node in current model
		model.setNode(node);
		
		return model;
	}
	
	private static String readFileAsString(String file)throws Exception{
        return new String(Files.readAllBytes(Paths.get(file)));
    }
	
	private static MCPart makeNode(float[] vertices, float[] textureCoords, float[] normals, int[] indices, String name,
			String texName, float shineDamper, float reflexivity, boolean transparency, String material, BufferObjectLoader loader) {
		Mesh mesh = loader.loadToVAO(vertices, textureCoords, normals, indices);
		Texture texture = Texture.loadTexture(texName);
		
		if(mesh!=null) {
			return new MCPart(mesh, texture, name, shineDamper, reflexivity, material, transparency);
		}else throw new IllegalArgumentException("mesh load failure");
	}
}
