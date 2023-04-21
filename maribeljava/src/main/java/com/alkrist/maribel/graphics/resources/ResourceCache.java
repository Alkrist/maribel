package com.alkrist.maribel.graphics.resources;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.graphics.model.Material;
import com.alkrist.maribel.graphics.model.ModelComposite;

public class ResourceCache {

	private static Map<String, ModelComposite> mmcCache = new HashMap<String, ModelComposite>();
	
	private static Map<String, Material> materialCache = new HashMap<String, Material>();

	public static ModelComposite getModel(String name) {
		return mmcCache.get(name);
	}
	
	public static Material getMaterial(String name) {
		return materialCache.get(name);
	}
	
	public static void addModel(ModelComposite model) {
		mmcCache.put(model.getName(), model);
	}
	
	public static void addMaterial(Material material) {
		materialCache.put(material.getName(), material);
	}
	
	public static Map<String, Material> getAllMaterials(){
		return materialCache;
	}
	
	public static void cleanUp() {
		mmcCache.clear();
		materialCache.clear();
	}
}
