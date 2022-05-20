package com.alkrist.maribel.client.graphics.gui;

import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector4f;

public class GUIFrame implements Component{

	public Vector2f position;
	public Vector2f scale;
	public Vector4f color;
	private Texture texture;
	
	public GUIFrame(Vector2f position, Vector2f scale, String texName) {
		this.position = position;
		this.scale = scale;
		this.texture = Texture.loadTexture(texName);
		this.color = new Vector4f(0,0,0,0);
	}
	
	public GUIFrame(Vector2f position, Vector2f scale, Vector4f color) {
		this.position = position;
		this.scale = scale;
		this.color = color;
	}
	
	public GUIFrame(Vector2f position, Vector2f scale, String texName, Vector4f color) {
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.texture = Texture.loadTexture(texName);
	}
	
	public boolean isTextured() {
		return texture != null;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
