package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.ecs.Component;

public class TestComponentI implements Component{

	public String data;
	public int x, y, z;
	
	public TestComponentI(int x, int y, int z, String data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == this.getClass() && obj != null) {
			if (((TestComponentI)obj).data == data 
					&& ((TestComponentI)obj).x == x 
					&& ((TestComponentI)obj).y == y 
					&& ((TestComponentI)obj).z == z) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		return (hash + x + y + z) * 31;
	}
}
