package com.alkrist.maribel.client;

import java.util.List;

import com.alkrist.maribel.client.model.Model;
import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.client.util.AssimpModelLoader;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.utils.Logging;

public class NewTest {

	public static void main(String[] args) {
		Logging.initLogger();
		Settings.load();	
		
		GLContext.create("test", "system/icon32.png");
		GLWindow window = GLContext.getWindow();
		
		System.out.println(AssimpModelLoader.class.getResource("/assets/models"));
		try {
			List<Model> models = AssimpModelLoader.loadModel("assets/models","rock01.obj");
			System.out.println(models.size());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
