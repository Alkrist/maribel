package com.alkrist.maribel.client.render.scenegraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joml.Vector3f;

import com.alkrist.maribel.client.core.Camera;

public class RenderList {
	private LinkedHashMap<String, Renderable> objectList;
	
	private List<Renderable> sortedCache;
    private boolean cacheValid;
    
	private boolean changed;
	
	public LinkedHashMap<String, Renderable> getObjectList(){
		return objectList;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	public RenderList() {
		
		changed = false;
		objectList = new LinkedHashMap<String, Renderable>();
	}
	
	public boolean contains(String id){
		
		return objectList.containsKey(id);
	}
	
	public Renderable get(String key){
		
		return objectList.get(key);
	}
	
	public void add(Renderable object){
		
		objectList.put(object.getId(), object);
		cacheValid = false;
	}
	
	public void remove(Renderable object){
		
		objectList.remove(object.getId());
		cacheValid = false;
	}
	
	public void remove(String key){
		
		objectList.remove(key);
		cacheValid = false;
	}
	
	public Set<String> getKeySet(){
		
		return objectList.keySet();
	}
	
	public Set<Map.Entry<String, Renderable>> getEntrySet(){
		
		return objectList.entrySet();
	}
	
	public Collection<Renderable> getValues(){
		
		return objectList.values();
	}
	
	public boolean hasChanged(){
		return changed;
	}
	
	public boolean isEmpty(){
		return objectList.isEmpty();
	}
	
	// painter's algorithm for transparent scene objects
	public List<Renderable> sortBackToFront(Camera camera) {
		Vector3f viewPosition = camera.getPosition();
		
        // Check if we can reuse the cached sort
        if (!cacheValid || camera.isMoved()) {
            // Update cache
            sortedCache = new ArrayList<>(objectList.values());
            sortedCache.sort(Comparator.comparingDouble(
                r -> -r.getWorldTransform().getTranslation().distanceSquared(viewPosition)
            ));
            cacheValid = true;
        }
        return sortedCache;
    }
}
