package com.alkrist.maribel.common.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestEvents {

	private static class Owner{}
	private static TestListener listener = new TestListener();
	
	@BeforeAll
	public static void callEvents() {
		EventManager.registerEvents(listener, new Owner());
	}
	
	@Test
	public void testEventCalls() {
		EventManager.callEvent(new TestEventOne());
		EventManager.callEvent(new TestEventTwo());
		EventManager.callEvent(new TestEventThree());
		
		assertEquals(listener.getFirst(), TestEventOne.class.getCanonicalName());
		assertEquals(listener.getSecond(), TestEventTwo.class.getCanonicalName());
		assertEquals(listener.getThird(), TestEventThree.class.getCanonicalName());
	}
}
