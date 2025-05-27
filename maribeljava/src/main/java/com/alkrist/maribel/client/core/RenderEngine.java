package com.alkrist.maribel.client.core;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glViewport;

import com.alkrist.maribel.client.components.ui.GUI;
import com.alkrist.maribel.client.scenegraph.RenderList;
import com.alkrist.maribel.client.scenegraph.Scenegraph;
import com.alkrist.maribel.client.util.GLUtil;

public class RenderEngine {

	private Scenegraph scenegraph;
	private RenderList opaqueSceneRenderList;
	
	private VideoConfig config;
	
	GUI gui;
	
	public RenderEngine() {
		// something that is engine-specific and does not change per scene
	}
	
	public void init() {
		config = Context.getVideoConfig();
		
		// something that changes per scene (i.e. menu and game)
		scenegraph = new Scenegraph();
		opaqueSceneRenderList = new RenderList();
		
		
		glFinish();
	}
	
	public void render() {
		GLUtil.clearScreen();
		
		scenegraph.record(opaqueSceneRenderList);
		
		opaqueSceneRenderList.getValues().forEach(object -> object.render());
		
		if(gui != null) {
			gui.render();
		}
		
		//TODO: viewport
		glViewport(0, 0, Context.getWindow().getWidth(), Context.getWindow().getHeight());
	}
	
	public Scenegraph getScenegraph() {
		return scenegraph;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
}
