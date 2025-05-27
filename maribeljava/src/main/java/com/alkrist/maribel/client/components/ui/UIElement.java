package com.alkrist.maribel.client.components.ui;

import org.joml.Matrix4f;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.scenegraph.Renderable;

public abstract class UIElement extends Renderable{

	protected Matrix4f orthographicMatrix;
	
	protected UIElement(int xPos, int yPos, float scaleX, float scaleY) {
		super();
		this.getWorldTransform().setTranslation(xPos, yPos, 0);
		this.getWorldTransform().setScaling(scaleX, scaleY, 1);
		
		int width = Context.getWindow().getWidth();
		int height = Context.getWindow().getHeight();
		
		this.orthographicMatrix = new Matrix4f().ortho(0, width, height, 0, -1, 1);
		this.orthographicMatrix.mul(getWorldTransform().getWorldMatrix());
	}
	
	@Override
	public void update(){};
	
	public void update(String text){};
	
	public Matrix4f getOrthographicMatrix() {
		return orthographicMatrix;
	}
}
