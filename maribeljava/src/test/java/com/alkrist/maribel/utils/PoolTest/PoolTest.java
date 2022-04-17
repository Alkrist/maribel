package com.alkrist.maribel.utils.PoolTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.utils.ObjectPool;

public class PoolTest {

	static TestPool pool;
	PoolTestObject obj1, obj2, obj3, obj4;
	
	
	@BeforeAll
	public static void setUp() {
		pool = new TestPool();
	}
	
	@BeforeEach
	public void cleanUp() {
		pool.clear();
	}
	
	@Test
	public void stackTest() {
		/*
		 * Create 3 new objects that are not in pool,
		 * make sure pool is empty (objects are passed directly to their
		 * links
		 */
		obj1 = pool.consume();
		obj2 = pool.consume();
		obj3 = pool.consume();
		assertEquals(0, pool.getAvailableSize());
		
		/*
		 * "delete" 2 objects so they appear in pool - 
		 * pool size now must be exactly 2
		 */
		pool.free(obj1);
		pool.free(obj2);
		obj2 = null;
		obj1 = null;
		assertEquals(2, pool.getAvailableSize());
		
		/*
		 * Initialize one object from pool - 
		 * now pool size must be 1
		 */
		obj4 = pool.consume();
		assertNotNull(obj4);
		assertEquals(1, pool.getAvailableSize());
		
		/*
		 * Create 2 more objects:
		 * one of them is taken from a pool, now its empty, so
		 * second object is created with constructor
		 */
		obj1 = pool.consume();
		obj2 = pool.consume();
		assertNotNull(obj1);
		assertNotNull(obj2);
		assertEquals(0, pool.getAvailableSize());
	}
	
	@Test
	public void testGrow() {
		PoolTestObject[] objects = new PoolTestObject[20];
		
		//Creates new elements using constructor
		for(int i=0; i<20; i++) {
			objects[i] = pool.consume();
		}
		assertEquals(0, pool.getAvailableSize());
		
		//Frees elements to the pool, by the end pool size should be 20
		for(int i=0; i<20; i++) {
			pool.free(objects[i]);
			objects[i] = null;
		}
		assertEquals(20, pool.getAvailableSize());
		
		//Takes free elements from pool, by the end pool size should be 0
		for(int i=0; i<20; i++) {
			objects[i] = pool.consume();
		}
		assertEquals(0, pool.getAvailableSize());
	}
	
	@Test
	public void efficiencyTest() {
		final int LENGTH = 100000;
		PoolTestObject[] pooledObjects = new PoolTestObject[LENGTH];
		PoolTestObject[] listedObjects = new PoolTestObject[LENGTH];
		long startTime, creationTime, reuseTime;
		
		/*
		 * First of all, create new objects and store them in first array.
		 * Pool size must be 0.
		 */
		System.out.println("Testing for "+LENGTH+" objects...");
		startTime = System.currentTimeMillis();
		for(int i=0; i<LENGTH; i++) {
			pooledObjects[i] = pool.consume();
		}
		creationTime = System.currentTimeMillis() - startTime;
		System.out.println("Creating new objects time: "+ creationTime+"ms");
		assertEquals(0, pool.getAvailableSize());
		
		/*
		 * Next, remove these objects and store them in pool.
		 * Pool size must be equal to the amount of objects by now.
		 */
		startTime = System.currentTimeMillis();
		for(int i=0; i<LENGTH; i++) {
			pool.free(pooledObjects[i]);
			pooledObjects[i] = null;
		}
		System.out.println("Freeing objects to pool time: "+ (System.currentTimeMillis() - startTime)+"ms");
		assertEquals(LENGTH, pool.getAvailableSize());
	
		/*
		 * Third, initialize the second array with objects taken from pool.
		 * In the end, pool size must be 0 again.
		 */
		startTime = System.currentTimeMillis();
		for(int i=0; i<LENGTH; i++) {
			listedObjects[i] = pool.consume();
		}
		reuseTime = System.currentTimeMillis() - startTime;
		System.out.println("Taking objects from pool time: "+ reuseTime+"ms");
		assertEquals(0, pool.getAvailableSize());
		
		/*
		 * Finally, make sure reusing is more efficient than creating new objects.
		 */
		assertTrue(reuseTime <= creationTime);
	}
	
	public static class TestPool extends ObjectPool<PoolTestObject>{
		
		@Override
		public PoolTestObject create() {
			return new PoolTestObject();
		}
	}
}
