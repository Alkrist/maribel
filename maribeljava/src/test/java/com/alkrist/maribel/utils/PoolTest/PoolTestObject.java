package com.alkrist.maribel.utils.PoolTest;

public class PoolTestObject {

	private static int ID = 0;
	
	private int number;
	private String name;
	private int[] buffer;
	private int id;
	
	public PoolTestObject() {
		this.number = Integer.MAX_VALUE;
		this.name = "abcdefghijklmnopqrstuvwxyz1234567890[]}{';:אבגדהו¸זחטיךכלםמןנסעףפץצקרשתûü‎‏ÿ";
		this.buffer = new int[100];
		for(int i=0; i<100; i++)
			buffer[i] = i;
		this.id = ID++;
	}
	
	public String getStr() {
		return name;
	}
	
	public int getInt() {
		return number;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("id: %d", id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		
		if(obj.getClass() != this.getClass()) return false;
		
		return ((PoolTestObject)obj).getId() == this.id;
	}
	
	@Override
	public int hashCode() {
		int hash = 54;
		return hash + id;
	}
}
