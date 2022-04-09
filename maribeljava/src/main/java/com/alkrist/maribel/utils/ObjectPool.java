package com.alkrist.maribel.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class ObjectPool <T> {

	private final Set<T> available = new HashSet<T>();
	private final Set<T> used = new HashSet<T>();
	
	public abstract T create();
	
	public synchronized T consume() {
		if(available.isEmpty()) {
			available.add(create());
		}
		
		T instance = available.iterator().next();
		available.remove(instance);
		used.add(instance);
		
		return instance;
	}
	
	public synchronized T free(T instance) {
		used.remove(instance);
		available.add(instance);
		
		return instance;
	}
	
	public int getAvailableSize() {
		return available.size();
	}
	
	public int getUsedSize() {
		return used.size();
	}
	
	public void clearAll() {
		used.clear();
		available.clear();
	}
	
	@Override
	public String toString() {
		return String.format("Pool available: %d, used: %d", available.size(), used.size());
	}
}
