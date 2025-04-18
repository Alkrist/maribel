package com.alkrist.maribel.client.core;

import static org.lwjgl.opengl.GL11.glFinish;

import com.alkrist.maribel.client.scenegraph.RenderList;
import com.alkrist.maribel.client.scenegraph.Scenegraph;

public class RenderEngine {

	private Scenegraph scenegraph;
	private RenderList opaqueSceneRenderList;
	
	private VideoConfig config;
	
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
		//TODO: clear screen
		
		scenegraph.record(opaqueSceneRenderList);
		
		opaqueSceneRenderList.getValues().forEach(object -> object.render());
		
		//TODO: viewport
	}
	
	public Scenegraph getScenegraph() {
		return scenegraph;
	}
}
