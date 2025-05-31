package com.alkrist.maribel.client.components.ui.fonts;

import java.io.File;

import com.alkrist.maribel.client.components.ui.UITextPanel;
import com.alkrist.maribel.client.render.texture.Texture;


public class FontType {
	
	private Texture textureAtlas;
	private FontMeshBuilder builder;


	public FontType(Texture textureAtlas, File fontFile) {
		this.textureAtlas = textureAtlas;
		this.builder = new FontMeshBuilder(fontFile);
	}

	public Texture getTextureAtlas() {
		return textureAtlas;
	}

	public FontMeshData loadText(UITextPanel text) {
		return builder.createTextMesh(text);
	}
}
