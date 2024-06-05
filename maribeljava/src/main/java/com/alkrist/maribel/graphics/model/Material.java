package com.alkrist.maribel.graphics.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.joml.Vector3f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.context.GraphicsConfig;
import com.alkrist.maribel.graphics.resources.ResourceCache;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.utils.FileUtils;

/**
 * Material class represents the material for renderable object.
 * 
 * @author Alkrist
 */
public class Material {

	private static final Material GENERIC_MATERIAL = getGenericMaterial();
	private static final GraphicsConfig config = GLContext.getConfig();
	
	private String name;
	
	private Texture2D diffuseMap;
	private Texture2D normalMap;
	private Texture2D ambientMap;
	private Texture2D specularMap;
	private Texture2D alphamap;
	
	private Vector3f color;
	
	private float heightScaling = 1;
	private float horizontalScaling = 1;
	private float emission = 0;
	private float shininess = 0;
	
	public Material(String name) {
		this.name = name;
	}

	private static Material getGenericMaterial() {
		Material m = new Material("GENERIC");
		
		m.setColor(new Vector3f(0.965f, 0, 1));
		m.setEmission(0.5f);
		ResourceCache.addMaterial(m);
		return m;
	}
	
	public Texture2D getAlphamap() {
		return alphamap;
	}

	public void setAlphamap(Texture2D alphamap) {
		this.alphamap = alphamap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Texture2D getDiffuseMap() {
		return diffuseMap;
	}

	public void setDiffuseMap(Texture2D diffuseMap) {
		this.diffuseMap = diffuseMap;
	}

	public Texture2D getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(Texture2D normalMap) {
		this.normalMap = normalMap;
	}

	public Texture2D getAmbientMap() {
		return ambientMap;
	}

	public void setAmbientMap(Texture2D ambientMap) {
		this.ambientMap = ambientMap;
	}

	public Texture2D getSpecularMap() {
		return specularMap;
	}

	public void setSpecularMap(Texture2D specularMap) {
		this.specularMap = specularMap;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getHeightScaling() {
		return heightScaling;
	}

	public void setHeightScaling(float heightScaling) {
		this.heightScaling = heightScaling;
	}

	public float getHorizontalScaling() {
		return horizontalScaling;
	}

	public void setHorizontalScaling(float horizontalScaling) {
		this.horizontalScaling = horizontalScaling;
	}

	public float getEmission() {
		return emission;
	}

	public void setEmission(float emission) {
		this.emission = emission;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	};

	
	public static Material loadMaterial(String fileName) {
		if(fileName == null)
			return GENERIC_MATERIAL;
		
		FileReader fr = null;
		
		try {
			fr  = new FileReader(new File(FileUtils.getResourceLocation(fileName)));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace(); //TODO: logging
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		String materialName = "";
		
		String diffusePath = "";
		String normalPath = "";
		String ambientPath = "";
		String specularPath = "";
		String alphamapPath = "";
		
		int numOfRows = 1;
		
		String wrapMode = "";
		
		String line;
		Material material = null;
		try {
			
			while(true) {
				line = reader.readLine();
				if(line==null) break;
				
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("n ")) {
					if(currentLine.length == 2) {
						materialName = currentLine[1].toUpperCase();
					}
					material = ResourceCache.getMaterial(materialName);
					if(material == null)
						material = new Material(materialName);
					else {
						fr.close();
						reader.close();
						return material;
					}
					
				}else if(line.startsWith("dm ")) {
					diffusePath = currentLine[1];
				}else if(line.startsWith("am ")) {
					ambientPath = currentLine[1];
				}else if(line.startsWith("nm ")) {
					normalPath = currentLine[1];
				}else if(line.startsWith("sm ")) {
					specularPath = currentLine[1];
				}else if(line.startsWith("al ")) {
					alphamapPath = currentLine[1];
				}else if(line.startsWith("at ")) {
					numOfRows = Integer.valueOf(currentLine[1]);
				}
				else if(line.startsWith("c ")) {
					if(currentLine.length == 4) {
						float r = Float.parseFloat(currentLine[1]);
						float g = Float.parseFloat(currentLine[2]);
						float b = Float.parseFloat(currentLine[3]);
						material.setColor(new Vector3f(r, g, b));
					}else
						material.setColor(new Vector3f(0.965f, 0, 1));
					
				}else if(line.startsWith("hs ")) {
					if(currentLine.length == 2) {
						material.setHeightScaling(Float.parseFloat(currentLine[1]));
					}else
						material.setHeightScaling(1);
					
				}else if(line.startsWith("ws ")) {
					if(currentLine.length == 2) {
						material.setHorizontalScaling(Float.parseFloat(currentLine[1]));
					}else
						material.setHorizontalScaling(1);
					
				}else if(line.startsWith("e ")) {
					if(currentLine.length == 2) {
						material.setEmission(Float.parseFloat(currentLine[1]));
					}else
						material.setEmission(0.5f);
					
				}else if(line.startsWith("s ")) {
					if(currentLine.length == 2) {
						material.setShininess(Float.parseFloat(currentLine[1]));
					}else
						material.setShininess(0.5f);
				}
				
				else if(line.startsWith("wm ")) {
					if(currentLine.length == 2) {
						wrapMode = currentLine[1];
					}
				}
			}
			
			if(!diffusePath.equals("")) {
				Texture2D diffuse = getMaterialTexture(diffusePath, Integer.valueOf(wrapMode));
				setNumOfRows(diffuse, numOfRows);
				material.setDiffuseMap(diffuse);
			}
			if(!normalPath.equals("")) {
				Texture2D normal = getMaterialTexture(normalPath, Integer.valueOf(wrapMode));
				setNumOfRows(normal, numOfRows);
				material.setNormalMap(normal);
			}
			if(!ambientPath.equals("")) {
				Texture2D ambient = getMaterialTexture(ambientPath, Integer.valueOf(wrapMode));
				setNumOfRows(ambient, numOfRows);
				material.setAmbientMap(ambient);
			}
			if(!specularPath.equals("")) {
				Texture2D specular = getMaterialTexture(specularPath, Integer.valueOf(wrapMode));
				setNumOfRows(specular, numOfRows);
				material.setSpecularMap(specular);
			}
			if(!alphamapPath.equals("")) {
				Texture2D alpha = getMaterialTexture(alphamapPath, Integer.valueOf(wrapMode));
				setNumOfRows(alpha, numOfRows);
				material.setAlphamap(alpha);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return GENERIC_MATERIAL;
		}
		
		ResourceCache.addMaterial(material);
		return material;
	}

	private static void setNumOfRows(Texture2D texture, int numOfRows) {
		if(numOfRows > 1) {
			texture.setNumberOfRows(numOfRows);
		}
	}
	
	private static Texture2D getMaterialTexture(String path, int wrapMode) {
		if(wrapMode == 1) {
			return new Texture2D(path, config.samplerFilter, TextureWrapMode.ClampToEdge);
		}else if(wrapMode == 2) {
			return new Texture2D(path, config.samplerFilter, TextureWrapMode.ClampToBorder);
		}else if(wrapMode == 3) {
			return new Texture2D(path, config.samplerFilter, TextureWrapMode.Repeat);
		}else if(wrapMode == 4) {
			return new Texture2D(path, config.samplerFilter, TextureWrapMode.MirrorRepeat);
		}else {
			return new Texture2D(path, config.samplerFilter);
		}	
	}
}
