package com.alkrist.maribel.client;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.utils.Logging;

public class MaribelTest {

	public static void main(String[] args) {
		
		// pre-init
		preInit();
		
		TestSceneHandler testScene = new TestSceneHandler();
		testScene.register();
		
		
		Context.getMasterLoader().initialization();
		
		Context.getMasterLoader().postInitialization();
		
		Context.getCoreEngine().start();
		
		// terminate cleanup
		System.out.println("clean up phase");
	}
	
	private static void preInit() {
	    Logging.initLogger();
	    Context.create();
	}
}
