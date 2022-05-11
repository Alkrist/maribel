package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.ecs.Component;

public class TestComponentII implements Component{

	public int number;
	public float floatNumber;
	
	public TestComponentII(int number, float floatNumber) {
		this.number = number;
		this.floatNumber = floatNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == this.getClass() && obj != null) {
			if (((TestComponentII)obj).number == number
					&& ((TestComponentII)obj).floatNumber == floatNumber) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		return (hash + number + (int) floatNumber) * 31;
	}
}
