package com.alkrist.maribel.graphics.platform;

import org.joml.Matrix4f;

import com.alkrist.maribel.common.event.Event;
import com.alkrist.maribel.common.event.HandlerList;

/**
 * Window resize event. Has data about new projection matrix, new width and
 * height of the window.
 * 
 * @author Alkrist
 *
 */
public class WindowResizeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Matrix4f projectionMatrix;
	private int width;
	private int height;

	public WindowResizeEvent(Matrix4f projectionMatrix, int width, int height) {
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
