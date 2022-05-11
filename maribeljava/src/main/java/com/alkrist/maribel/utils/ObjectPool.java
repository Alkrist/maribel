package com.alkrist.maribel.utils;

import java.util.logging.Level;

/**
 * Object pool to reuse objects, can be effectively used for 2 cases:
 * 1) when you have to limit creation of new objects
 * 2) when you have heavy objects that take a lot of time to create
 * 
 * pros:
 * - fast
 * - can save time on high scale of objects
 * 
 * cons:
 * - uses a lot of RAM in comparison to just creating/deleting elements
 * 
 * How does it work:
 * A pool has a stack of elements, whenever free() method is called, pool gets a new object to stack,
 * which will be pushed on top.
 * 
 * Whenever consume() method is called, pool checks if there're any free elements available, 
 * if yes, it pops them, if not, it creates a new instance.
 * 
 * To make a long story short, it just checks for spare objects before creating new ones.
 * 
 * Default pool length is 8 elements, however, this number is extended automatically.
 * 
 * @author Mikhail
 *
 * @param <T> - Pooled object type
 */
public abstract class ObjectPool <T> {

	private final PoolStack<T> available = new PoolStack<T>();
	
	/**
	 * Creates a new instance, bust be implemented in child classes
	 * @return new object
	 */
	public abstract T create();
	
	/**
	 * Gets a free object from pool or creates a new instance.
	 * @return obtained object
	 */
	public synchronized T consume() {
		if(available.size() == 0)
			return create();
		
		T instance = available.pop();
		return instance;
	}
	
	/**
	 * Add an object to pool, so it's treated as reused.
	 * Do not use this object any further!
	 * @param instance - instance to pool
	 * @return that instance
	 */
	public synchronized T free(T instance) {
		if (instance == null) throw new IllegalArgumentException("instance cannot be null!");
		available.push(instance);
		return instance;
	}
	
	/**
	 * @return the amount of elements that can be reused.
	 */
	public int getAvailableSize() {
		return available.size();
	}
	
	/**
	 * Remove everything from pool and make it default length of 8.
	 */
	public void clear() {
		available.clear();
	}
	
	@Override
	public String toString() {
		return String.format("Pool available: %d", available.size());
	}
	
	//Pool stack
	private static class PoolStack<T>{
		
		private T[] stack;
		private int tail; //pointer to the NEXT element, NOT current
		
		@SuppressWarnings("unchecked")
		public PoolStack() {
			this.stack = (T[]) new Object[8];
			this.tail = 0;
		}
		
		public void push(T value) {
			if(tail >= stack.length)
				grow();
			stack[tail] = value;
			tail++;
		}
		
		public T pop() {
			T value = stack[tail-1];
			stack[tail-1] = null;
			tail = Math.max(0, tail - 1);
			if(tail*2 < stack.length)
				reduce();
			if(value == null) Logging.getLogger().log(Level.WARNING, "[Object Pool]: Queued value is null");
			return value;
		}
		
		public int size() {
			return tail;
		}
		
		@SuppressWarnings("unchecked")
		public void clear() {
			this.stack = (T[]) new Object[8];
			this.tail = 0;
		}
		
		@SuppressWarnings("unchecked")
		private void grow() {
			int newLength = stack.length + Math.max(8, (int)(stack.length * 1.8f));
			T[] newStack = (T[]) new Object[newLength];
			System.arraycopy(stack, 0, newStack, 0, Math.min(stack.length, newStack.length));
			this.stack = newStack;
		}
		
		@SuppressWarnings("unchecked")
		private void reduce() {
			int newLength = Math.max(8, (int)(stack.length * 0.75f));
			T[] newStack = (T[]) new Object[newLength];
			System.arraycopy(stack, 0, newStack, 0, Math.min(stack.length, newStack.length));
			this.stack = newStack;
		}
	}
}
