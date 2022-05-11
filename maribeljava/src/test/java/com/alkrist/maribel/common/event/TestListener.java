package com.alkrist.maribel.common.event;

public class TestListener implements Listener{

	private String first, second, third;
	
	
	
	public String getFirst() {
		return first;
	}

	public String getSecond() {
		return second;
	}

	public String getThird() {
		return third;
	}

	@EventHandler
	public void onTestEventOne(TestEventOne event) {
		first = event.getMessage();
	}
	
	@EventHandler
	public void onTestEventTwo(TestEventTwo event) {
		second = event.getMessage();
	}
	
	@EventHandler
	public void onTestEventThree(TestEventThree event) {
		third = event.getMessage();
	}
}
