package com.alkrist.maribel.graphics.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONObject;

import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

public class ModelCompositeLoader {

	/*
	 * TODO: implement me later, when you're fucking ready
	 */
	
	public static Model loadFromJson(String fileNoFormat) {
		try {
			
			String contents = readFileAsString(FileUtil.getModelsPath()+fileNoFormat+".json");
			JSONObject jsonObject = new JSONObject(contents);
			
			Iterator<String> keys = jsonObject.keys();
			String key = keys.next();
			if (jsonObject.get(key) instanceof JSONObject) {
				return loadNode(key, jsonObject.getJSONObject(key), null);
			}else {
				Logging.getLogger().log(Level.WARNING, "Failed to load model, json structure corrupt.");
			}
			
		}catch(Exception e) {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, no contents loaded.",e);
		}
		
		return null;
	}
	
	private static Model loadNode(String name, JSONObject jsonObject, Model parent) {
		Iterator<String> keys = jsonObject.keys();
		
		Material material;
		Mesh mesh = null;
		
		String meshPath = jsonObject.getString("mesh");
		if(meshPath != null)
			mesh = OBJLoader.loadOBJmodel(meshPath);
		if(mesh == null) {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, mesh not loaded.");
			return null;
		}
		String materialName = jsonObject.getString("material");
		material = getMaterial(materialName);
		
		Model model = new Model(mesh, material, name);
		
		while(keys.hasNext()) {
			String key = keys.next();
			if (jsonObject.get(key) instanceof JSONObject) {
				model.addChild(loadNode(key, jsonObject.getJSONObject(key), model));
			}
		}
		
		return model;
	}
	
	private static String readFileAsString(String file)throws Exception{
        return new String(Files.readAllBytes(Paths.get(file)));
    }
	
	public static Model loadFromMMC(String fileNoFormat) {
		FileReader fr = null;
		
		try {
			fr  = new FileReader(new File(FileUtil.getModelsPath()+fileNoFormat+".mmc"));
		} catch (FileNotFoundException e) {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, no contents loaded.",e);
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		Model parent = null;
		Model current = null;
		
		boolean indicesRead = false;
		boolean verticesRead = false;
		boolean texturesRead = false;
		boolean normalsRead = false;
		
		String line;
		List<Float>vertexPoints = new ArrayList<Float>();
		List<Float>texturePoints = new ArrayList<Float>();
		List<Float>normalPoints = new ArrayList<Float>();
		List<String>indexPoints = new ArrayList<String>();
		
		List<VertexNM> verticesNM = null;
		List<Vector2f> textureVertices = null;
		List<Vector3f> normalVertices = null;
		List<Integer> indices = null;
		
		try {
			
			while(true) {
				line = reader.readLine();
				if(line==null) break;
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("o ")) {
					current = new Model(currentLine[1]);
					if(parent != null) {
						parent.addChild(current);
						current.setParent(parent);
					}
						
				}else if(line.startsWith("m ")) {
					current.setMaterial(getMaterial(currentLine[1]));
				}
				else if(line.startsWith("{")) {
					parent = current;
				}
				else if(line.startsWith("}")) {
					if(parent != null) {
						current = parent;
						parent = parent.getParent();
					}	
				}
				else if(line.startsWith("vc ")) {
					verticesRead = true;
					indicesRead = false;
					texturesRead = false;
					normalsRead = false;
				}else if(line.startsWith("vn ")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = false;
					normalsRead = true;
				}else if(line.startsWith("uv ")) {
					verticesRead = false;
					indicesRead = false;
					texturesRead = true;
					normalsRead = false;
				}else if(line.startsWith("in ")) {
					verticesRead = false;
					indicesRead = true;
					texturesRead = false;
					normalsRead = false;
				}
				if(verticesRead) {
					for(int i=1;i<currentLine.length;i++) {
						vertexPoints.add(Float.parseFloat(currentLine[i]));
					}
					if(vertexPoints.size() % 3 != 0) {
						Logging.getLogger().log(Level.WARNING, "Failed to load model, mmc structure corrupt: wrong vertices.");
						reader.close();
						fr.close();
						return null;
					}
					verticesNM = new ArrayList<VertexNM>();
					convertVertexNM(vertexPoints, verticesNM);
					vertexPoints.clear();
					verticesRead = false;
				}
				else if(texturesRead) {
					
					for(int i=1;i<currentLine.length;i++) {
						texturePoints.add(Float.parseFloat(currentLine[i]));
					}
					if(texturePoints.size() % 2 != 0) {
						Logging.getLogger().log(Level.WARNING, "Failed to load model, mmc structure corrupt: wrong UVs.");
						reader.close();
						fr.close();
						return null;
					}
					textureVertices = new ArrayList<Vector2f>();
					convertVector2f(texturePoints, textureVertices);
					texturePoints.clear();
					texturesRead = false;
				}
				else if(normalsRead) {
					
					for(int i=1;i<currentLine.length;i++) {
						normalPoints.add(Float.parseFloat(currentLine[i]));
					}
					if(normalPoints.size() % 3 != 0) {
						Logging.getLogger().log(Level.WARNING, "Failed to load model, mmc structure corrupt: wrong normal vectors.");
						reader.close();
						fr.close();
						return null;
					}
					normalVertices = new ArrayList<Vector3f>();
					convertVector3f(normalPoints, normalVertices);
					normalPoints.clear();
					normalsRead = false;
				}
				else if(indicesRead) { //assume that indices read the last
					for(int i=1;i<currentLine.length;i++) {
						indexPoints.add(currentLine[i]);
					}
					indicesRead = false;
				}
				if(verticesNM != null && textureVertices != null && normalVertices != null && current != null && !indexPoints.isEmpty()) {
					if(indexPoints.isEmpty()) {
						Logging.getLogger().log(Level.WARNING, "Failed to load model, mmc structure corrupt: no indices found.");
						reader.close();
						fr.close();
						return null;
					}
					indices = new ArrayList<Integer>();
					processIndicesAndTangents(indexPoints, verticesNM, textureVertices, indices);
					indexPoints.clear();
					removeUnusedVertices(verticesNM);
					
					float[] verticesArray = new float[verticesNM.size() * 3];
					float[] texturesArray = new float[verticesNM.size() * 2];
					float[] normalsArray = new float[verticesNM.size() * 3];
					float[] tangentsArray = new float[verticesNM.size() * 3];
					
					float boundingRadius = convertDataToArrays(verticesNM, textureVertices, normalVertices, verticesArray, texturesArray, normalsArray, tangentsArray);
				
					int[] indicesArray = convertIndicesListToArray(indices);
					
					Mesh mesh = ResourceLoader.loadToVAO(verticesArray, tangentsArray, normalsArray, indicesArray, boundingRadius);
					verticesNM = null;
					textureVertices = null;
					normalVertices = null;
					indices = null;
					if(mesh != null) {
						current.setMesh(mesh);
					}	
					else {
						Logging.getLogger().log(Level.WARNING, "Failed to load model, mesh not loaded.");
					}
				}
			}
			reader.close();
			fr.close();
			return current;
			
		}catch(Exception e) {
			Logging.getLogger().log(Level.WARNING, "Failed to load model, mmc structure corrupt.", e);
		}
		return null;
	}
	
	private static void convertVertexNM(List<Float> vertexList, List<VertexNM> vertexNMList) {
		for(int i=0; i<vertexList.size()-2; i+=3) {
			Vector3f vertex = new Vector3f(vertexList.get(i), vertexList.get(i+1), vertexList.get(i+2));
			vertexNMList.add(new VertexNM(vertexNMList.size(), vertex));
		}
	}
	
	private static void convertVector2f(List<Float> vertexList, List<Vector2f> vectorList) {
		for(int i=0; i<vertexList.size()-1; i+=2)
			vectorList.add(new Vector2f(vertexList.get(i), vertexList.get(i+1)));
	}
	
	private static void convertVector3f(List<Float> vertexList, List<Vector3f> vectorList) {
		for(int i=0; i<vertexList.size()-2; i+=3)
			vectorList.add(new Vector3f(vertexList.get(i), vertexList.get(i+1), vertexList.get(i+2)));
	}
	
	private static float convertDataToArrays(List<VertexNM> vertices, List<Vector2f> textures, List<Vector3f> normals,
			float[] verticesArray, float[] texturesArray, float[] normalsArray, float[] tangentsArray) {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			VertexNM currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			Vector3f tangent = currentVertex.getAverageTangent();
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			tangentsArray[i * 3] = tangent.x;
			tangentsArray[i * 3 + 1] = tangent.y;
			tangentsArray[i * 3 + 2] = tangent.z;

		}
		return furthestPoint;
	}
	
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}
	
	private static void processIndicesAndTangents(List<String> indexPoints, List<VertexNM> verticesNM, List<Vector2f> textureVertices, List<Integer> indices) {
		for(int i=0; i< indexPoints.size()-2; i+=3) {
			String[] vertex1 = indexPoints.get(i).split("/");
			String[] vertex2 = indexPoints.get(i+1).split("/");
			String[] vertex3 = indexPoints.get(i+2).split("/");
			
			VertexNM v0 = processVertex(vertex1, verticesNM, indices);
			VertexNM v1 = processVertex(vertex2, verticesNM, indices);
			VertexNM v2 = processVertex(vertex3, verticesNM, indices);
			calculateTangents(v0, v1, v2, textureVertices);// NEW
		}
	}
	
	// TODO: modify code to allocate less memory
	private static void calculateTangents(VertexNM v0, VertexNM v1, VertexNM v2, List<Vector2f> textures) {
		Vector3f deltaPos1 = new Vector3f();
		Vector3f deltaPos2 = new Vector3f();
		v1.getPosition().sub(v0.getPosition(), deltaPos1);
		v2.getPosition().sub(v0.getPosition(), deltaPos2);

		Vector2f uv0 = textures.get(v0.getTextureIndex());
		Vector2f uv1 = textures.get(v1.getTextureIndex());
		Vector2f uv2 = textures.get(v2.getTextureIndex());
		Vector2f deltaUv1 = new Vector2f();
		Vector2f deltaUv2 = new Vector2f();
		
		uv1.sub(uv0, deltaUv1);
		uv2.sub(uv0, deltaUv2);

		float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
		
		deltaPos1.normalize(deltaUv2.y);
		deltaPos2.normalize(deltaUv1.y);
		
		Vector3f tangent = new Vector3f();
		deltaPos1.sub(deltaPos2, tangent);

		tangent.normalize(r);
		v0.addTangent(tangent);
		v1.addTangent(tangent);
		v2.addTangent(tangent);
	}
	
	private static VertexNM processVertex(String[] vertex, List<VertexNM> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		VertexNM currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
		}
	}
	
	private static VertexNM dealWithAlreadyProcessedVertex(VertexNM previousVertex, int newTextureIndex,
			int newNormalIndex, List<Integer> indices, List<VertexNM> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			VertexNM anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices,
						vertices);
			} else {
				VertexNM duplicateVertex = previousVertex.duplicate(vertices.size());// NEW
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}
		}
	}
	
	private static void removeUnusedVertices(List<VertexNM> vertices) {
		for (VertexNM vertex : vertices) {
			vertex.averageTangents();
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
	
	private static Material getMaterial(String name) {
		return Material.loadMaterial(name.toUpperCase());
	}
}