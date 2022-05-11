package com.alkrist.maribel.utils;

/**
 * An alternative container for data, considered faster than Arraylist, 
 * but worse in case of memory consumption.
 * 
 * The overall idea:
 * 
 * create an array of N-length.
 *          |
 *          V
 * ... add elements...
 *          |
 *          V
 * if index is out of bounds, extend capacity by creating a new array with a
 *  capacity = (previous * 3) / 2 + 1 or any other specified size.
 *  		| 
 *          V
 *       repeat
 * 
 * @author Mikhail
 *
 * @param <T>
 */
public class Bag <T>{

	private T[] data;
	
	private int size = 0;
	
	/**
	 * A constructor with a specified start capacity.
	 * 
	 * @param capacity - capacity at the beginning
	 */
	@SuppressWarnings("unchecked")
	public Bag(int capacity) {
		data = (T[]) new Object[capacity];
	}
	
	/**
	 * Default constructor with a capacity for 64 elements.
	 */
	public Bag() {
		this(64);
	}
	
	/**
	 * Remove and return an element from the position.
	 * 
	 * @param index - the element's position
	 * @return - removed element
	 */
	public T remove(int index) {
		T t = data[index];
		data[index] = data[--size];
		data[size] = null;
		return t;
	}
	
	/**
	 * Remove and return the last element.
	 * @return - removed element
	 */
	public T removeLast () {
		if (size > 0) {
			T t = data[--size];
			data[size] = null;
			return t;
		}
		return null;
	}
	
	/**
	 * Removes the specified element.
	 * 
	 * @param t - the element to remove
	 * @return - removed element
	 */
	public boolean remove(T t) {
		for (int i = 0; i < size; i++) {
			T t2 = data[i];

			if (t == t2) {
				data[i] = data[--size];
				data[size] = null;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if this bag has specified element
	 * @param t - specified element
	 * @return - the check result
	 */
	public boolean contains (T t) {
		for (int i = 0; size > i; i++) {
			if (t == data[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the element from the desired position
	 * @param index
	 * @return
	 */
	public T get(int index) {
		return data[index];
	}
	
	/**
	 * @return current size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @return the length of this bag
	 */
	public int getCapacity () {
		return data.length;
	}
	
	/**
	 * Checks if this index is in bounds or not
	 * 
	 * @param index - index to check
	 * @return the result
	 */
	public boolean isIndexWithinBounds (int index) {
		return index < getCapacity();
	}
	
	/**
	 * @return whether this bag is empty
	 */
	public boolean isEmpty () {
		return size == 0;
	}
	
	/**
	 * Add an element and grow if needed.
	 * 
	 * @param t - an element to add
	 */
	public void add (T t) {
		if (size == data.length) {
			grow();
		}
		data[size++] = t;
	}
	
	/**
	 * Set an element strictly to the position.
	 * 
	 * @param index - position where to add
	 * @param t - an element
	 */
	public void set (int index, T t) {
		if (index >= data.length) {
			grow(index * 2);
		}
		size = index + 1;
		data[index] = t;
	}
	
	/**
	 * Remove all of the elements from the bag.
	 */
	public void clear () {
		for (int i = 0; i < size; i++) {
			data[i] = null;
		}

		size = 0;
	}
	
	private void grow () {
		int newCapacity = (data.length * 3) / 2 + 1;
		grow(newCapacity);
	}
	
	@SuppressWarnings("unchecked")
	private void grow(int capacity) {
		T[] oldData = data;
		data = (T[]) new Object[capacity];
		System.arraycopy(oldData, 0, data, 0, oldData.length);
	}
}
