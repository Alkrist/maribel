package com.alkrist.maribel.ecs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;

public class TestECS {

	static Engine testEngine;
	
	@BeforeAll
	public static void init() {
		testEngine = new Engine();
		testEngine.addSystem(new TestSystemI());
		testEngine.addSystem(new TestSystemII());
		
		for(int i=0; i<10000; i++) {
			Entity e = testEngine.createEntity();
			if(i<=5000) {
				e.addComponent(new TestComponentI(1,2,3,"test1"));
			}else {
				e.addComponent(new TestComponentII(1234, 5.5f));
				e.addComponent(new TestComponentIII("test3", 7.7f));
			}
			
		}
	}
	
	@Test
	public void engineRunTest() {
		for(int i=0;i<10;i++) {
			testEngine.update(0);
		}
	}
}
