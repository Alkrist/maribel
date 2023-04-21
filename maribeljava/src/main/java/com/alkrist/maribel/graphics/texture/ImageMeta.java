package com.alkrist.maribel.graphics.texture;

public class ImageMeta {

	private int width;
	private int height;
	private int channels;
	
	public ImageMeta(int width, int height, int channels) {
		this.width = width;
		this.height = height;
		this.channels = channels;
	}
	
	public ImageMeta(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getChannels() {
		return channels;
	}
	
	public void setChannels(int channels) {
		this.channels = channels;
	}
	
}
