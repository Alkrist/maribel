package com.alkrist.maribel.client.graphics;

import com.alkrist.maribel.common.event.Event;
import com.alkrist.maribel.common.event.HandlerList;
import com.alkrist.maribel.utils.math.Matrix4f;

public class WindowResizeEvent extends Event{

private static final HandlerList handlers = new HandlerList();
	
	private Matrix4f projectionMatrix;
	private int width;
	private int height;
	
	public WindowResizeEvent(Matrix4f projectionMatrix, int width,int height) {
		this.projectionMatrix = projectionMatrix;
		this.width = width;
		this.height = height;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
