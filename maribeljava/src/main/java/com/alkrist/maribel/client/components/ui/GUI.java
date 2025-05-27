package com.alkrist.maribel.client.components.ui;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.client.memory.UIVAO;
import com.alkrist.maribel.client.model.Mesh;
import com.alkrist.maribel.client.model.Vertex;
import com.alkrist.maribel.client.scenegraph.RenderList;
import com.alkrist.maribel.client.texture.Texture;
import com.alkrist.maribel.client.util.Util;

public class GUI {

	private ArrayList<UIScreen> screens = new ArrayList<UIScreen>();
	protected Texture fontsTexture;
	protected UIVAO panelMeshBuffer;
	
	public ArrayList<UIScreen> getScreens(){
		return screens;
	}
	
	public UIVAO getPanelMeshBuffer() {
		return panelMeshBuffer;
	}
	
	public void init() {
		//fontsTexture = new Texture2D("gui/tex/Fonts.png", SamplerFilter.Bilinear);
		panelMeshBuffer = new UIVAO();
		initPanelBuffer();
	}
	
	public void update(){
		
		screens.forEach(screen -> screen.update());
	}
	
	public void render(){
		glDisable(GL_DEPTH_TEST);
		screens.forEach(screen -> screen.render());
		glEnable(GL_DEPTH_TEST);
	}
	
	public void record(RenderList renderList){

		screens.forEach(screen -> screen.record(renderList));
	}
	
	public void cleanup(){

		screens.forEach(screen -> screen.cleanup());
	}
	
	private void initPanelBuffer() {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		vertices.add(new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)));
		vertices.add(new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)));
		vertices.add(new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0)));
		vertices.add(new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)));
		
		indices.add(1); indices.add(2); indices.add(0);
		indices.add(2); indices.add(1); indices.add(3);
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);
	
		Integer[] objectArray = new Integer[indices.size()];
		indices.toArray(objectArray);
		int[] indexData = Util.toIntArray(objectArray);
		
		Mesh mesh = new Mesh(vertexData, indexData);
		panelMeshBuffer.addData(mesh);
	}
}
