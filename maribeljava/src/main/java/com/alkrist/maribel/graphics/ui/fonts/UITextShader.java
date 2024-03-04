package com.alkrist.maribel.graphics.ui.fonts;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;

public class UITextShader extends ShaderProgram{

	private static UITextShader instance = null;
	
	public static UITextShader getInstance() {
		if(instance == null) {
			instance = new UITextShader();
		}
		
		return instance;
	}
	
	protected UITextShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"ui\\text_vertex.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"ui\\text_fragment.glsl"));
		compileShader();
		
		addUniform("color");
		addUniform("fontAtlas");
		addUniform("translation");
		
		addUniform("width");
		addUniform("edge");
		
		addUniform("borderWidth");
		addUniform("borderEdge");
		
		addUniform("outlineColor");
		addUniform("offset");
		
	}
	
	public void updateFontTexture(FontType font) {
		glActiveTexture(GL_TEXTURE0);
		font.getTextureAtlas().bind();
		setUniform("fontAtlas", 0);
	}
	
	public void updateUniforms(UIText textElement) {
		setUniform("color", textElement.getColor());
		setUniform("translation", textElement.getPosition());
		
		setUniform("width", textElement.getWidth());
		setUniform("edge", textElement.getEdge());
		setUniform("borderWidth", textElement.getBorderWidth());
		setUniform("borderEdge", textElement.getBorderEdge());
		setUniform("outlineColor", textElement.getOutlineColor());
		setUniform("offset", textElement.getOutlineOffset());
	}
}
