package com.alkrist.maribel.utils.PoolTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.utils.ObjectPool;

public class PoolTest {

	static TestPool pool;
	static int a,b,c,d,e,f;
	
	
	@BeforeAll
	public static void setUp() {
		pool = new TestPool();
	}
	
	@Test
	public void sizeTest(){
		a = pool.consume();
		b = pool.consume();
		c = pool.consume();
		
		assertEquals(3, pool.getUsedSize());
		
		pool.free(a);
		pool.free(b);
		
		assertEquals(2, pool.getAvailableSize());
		assertEquals(1, pool.getUsedSize());
		
		d = pool.consume();
		e = pool.consume();
		
		assertEquals(0, pool.getAvailableSize());
		assertEquals(3, pool.getUsedSize());
		
		pool.clearAll();
	}
	
	@Test
	public void poolEfficiencyTest() {
		List<Integer> values = new ArrayList<Integer>();
		long startTime, creationTime, reconsumptionTime;
		
		startTime = System.currentTimeMillis();
		for(int i=0; i<10000; ++i) {
			values.add(pool.consume());
		}
		creationTime = System.currentTimeMillis() - startTime;
		for(int i=0; i<10000; ++i) {
			pool.free(values.get(i));
		}
		values.clear();
		startTime = System.currentTimeMillis();
		for(int i=0; i<10000; ++i) {
			values.add(pool.consume());
		}
		reconsumptionTime =  System.currentTimeMillis() - startTime;
		System.out.println(reconsumptionTime);
		System.out.println(creationTime);
		
		values.clear();
		pool.clearAll();
	}
	
	public static class TestPool extends ObjectPool<Integer>{

		private static int id = 0;
		
		@Override
		public Integer create() {
			return id++;
		}
	}
}
