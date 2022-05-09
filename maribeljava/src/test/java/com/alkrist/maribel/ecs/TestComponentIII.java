package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.ecs.Component;

public class TestComponentIII implements Component{

	public String data;
	public float floatNumber;
	
	public TestComponentIII(String data, float floatNumber) {
		this.data = data;
		this.floatNumber = floatNumber;
	}
}
