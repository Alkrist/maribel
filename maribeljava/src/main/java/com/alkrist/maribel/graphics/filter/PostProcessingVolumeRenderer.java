package com.alkrist.maribel.graphics.filter;

import com.alkrist.maribel.graphics.components.PostProcessingVolume;
import com.alkrist.maribel.graphics.filter.contrast.ContrastController;
import com.alkrist.maribel.graphics.texture.Texture;

public class PostProcessingVolumeRenderer {

	
	ContrastController contrastController;
	
	/*
	 * Idea is: 
	 * 0) create and init this instance
	 * 
	 * 1) set aux textures
	 * 2) call effects in proper order, fetch scene texture and properties for shader
	 * 3) return final result texture
	 */
	private Texture depthMask;
	private Texture positionMask;
	private Texture ssaoBloomMask;
	private Texture lightScatteringmask;
	
	public PostProcessingVolumeRenderer() {
		this.contrastController = new ContrastController();
	}
	
	
	public void resize(int width, int height) {
		//TODO: resize controllers
	}
	
	public Texture render(PostProcessingVolume volume, Texture sceneTexture) {
		
		Texture volumedSceneTexture = sceneTexture;
		
		if(volume.hasEffect(ContrastController.ID)) {
			contrastController.render(volume.getUniformProperty(ContrastController.ID), volumedSceneTexture);
			volumedSceneTexture = contrastController.getContrastTexture();
		}
		
		return volumedSceneTexture;
	}
	
	public void setAuxiliaryTextures(Texture depthMask, Texture positionMask, Texture ssaoBloomMask, 
			Texture lightScatteringMask) {
		
		this.depthMask = depthMask;
		this.positionMask = positionMask;
		this.ssaoBloomMask = ssaoBloomMask;
		this.lightScatteringmask = lightScatteringMask;
	}
}
