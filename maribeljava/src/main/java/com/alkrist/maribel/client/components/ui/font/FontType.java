package com.alkrist.maribel.client.components.ui.font;

import java.io.File;

import com.alkrist.maribel.client.components.ui.UITextPanel;
import com.alkrist.maribel.client.texture.Texture;

public class FontType {

	private Texture textureAtlas;
	private TextMeshBuilder loader;

	public FontType(Texture textureAtlas, File fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshBuilder(fontFile);
	}

	public Texture getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshData loadText(UITextPanel text) {
		return loader.createTextMesh(text);
	}
}
