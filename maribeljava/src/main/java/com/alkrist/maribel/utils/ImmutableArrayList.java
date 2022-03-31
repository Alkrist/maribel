package com.alkrist.maribel.utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Read-only wrapper class for the ArrayList.
 */
public class ImmutableArrayList<T> implements Iterable<T>{
	
	private final ArrayList<T> list;
	
	public ImmutableArrayList(ArrayList<T> list){
		this.list = list;
	}
	
	public int size() {
		return list.size();
	}
	
	public T get(int index){
		return list.get(index);
	}
	
	public boolean contains(T value) {
		return list.contains(value);
	}
	
	public int indexOf(T value) {
		return list.indexOf(value);
	}
	
	public int lastIndexOf(T value) {
		return list.lastIndexOf(value);
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		return (T[]) list.toArray();
	}
	
	public int hashCode() {
		return list.hashCode();
	}
	
	public boolean equals(Object object) {
		return list.equals(object);
	}
	
	public String toString() {
		return list.toString();
	}

	public Iterator<T> iterator() {
		return list.iterator();
	}

}

