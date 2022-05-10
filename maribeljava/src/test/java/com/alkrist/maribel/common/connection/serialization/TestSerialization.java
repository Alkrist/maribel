package com.alkrist.maribel.common.connection.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestSerialization implements SerialBuilder{

	private static Serializer serializer;
	
	@BeforeAll
	public static void init() {
		serializer = new Serializer();
	}
	
	@Test
	public void testEncoding() {
		TestSerializable obj = new TestSerializable();
		obj.fNumber = 10.5f;
		obj.number = 45;
		obj.str = "I love you Maribel, please love me too";
		obj.expr = false;		
		byte[] data = serializer.encode(obj);
		
		TestSerializable newObj = (TestSerializable) serializer.decode(data, this);
		
		assertEquals(10.5f, newObj.fNumber);
		assertEquals(45, newObj.number);
		assertEquals("I love you Maribel, please love me too", newObj.str);
		assertEquals(false, newObj.expr);
	}

	@Override
	public Serializable build(SerialBuffer buffer) {
		return new TestSerializable();
	}
}
