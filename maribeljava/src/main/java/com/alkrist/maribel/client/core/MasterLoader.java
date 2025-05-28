package com.alkrist.maribel.client.core;

import java.util.ArrayList;
import java.util.List;

public class MasterLoader {

	/*private GUI loadingGUI;
    private UITextPanel progressText;
    private float progress = 0;*/
	
	private List<Runnable> initTasks;
	private List<Runnable> postInitTasks;
	
	protected MasterLoader() {
		initTasks = new ArrayList<>();
		postInitTasks = new ArrayList<>();
	}
	
	/*public void initLoadingScreen() {
        loadingGUI = new GUI();
        FontType loadingFont = FontType.loadFont(
            FileUtils.getResourceLocation("fonts/test.fnt"),
            FileUtils.getResourceLocation("textures/fonts/test.png"),
            24
        );
        
        UIScreen loadingScreen = new UIScreen();
        progressText = new UITextPanel("Loading: 0%", loadingFont, 
                                      new Vector4f(1), 100, 100, 1);
        
        loadingScreen.getElements().add(new UIColorPanel(
            new Vector4f(0, 0, 0, 0.7f), 
            0, 0, 
            Context.getWindow().getWidth(), 
            Context.getWindow().getHeight(),
            loadingGUI.getPanelMeshBuffer()
        ));
        loadingScreen.getElements().add(progressText);
        
        loadingGUI.getScreens().add(loadingScreen);
        Context.getRenderEngine().setGUI(loadingGUI);
    }*/
	
	public void addInitializationTask(String description, Runnable task) {
        initTasks.add(() -> {
        	System.out.println(description);
            //updateProgress(progress + (100f/initTasks.size()), description);
            task.run();
        });
    }
	
	public void addPostInitializationTask(String description, Runnable task) {
        postInitTasks.add(() -> {
            //updateProgress(progress + (100f/initTasks.size()), description);
            task.run();
        });
    }
	
	public void initialization() {
		for(int i = 0; i < initTasks.size(); i++) {
            Runnable task = initTasks.get(i);
            //updateProgress(i * progressPerTask, "Loading...");
            task.run();
        }
	}
	
	public void postInitialization() {
		for(int i = 0; i < postInitTasks.size(); i++) {
            Runnable task = postInitTasks.get(i);
            //updateProgress(i * progressPerTask, "Loading...");
            task.run();
        }
	}
	
	public void dispose() {
		
	}
	
	/*private void updateProgress(float newProgress, String message) {
        this.progress = Math.min(newProgress, 100);
        progressText.setText(String.format("%s: %.0f%%", message, progress));
        Context.getRenderEngine().renderLoadingScreen();
    }*/
}
