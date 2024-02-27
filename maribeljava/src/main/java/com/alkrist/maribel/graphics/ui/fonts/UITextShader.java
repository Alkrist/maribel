package com.alkrist.maribel.graphics.ui.fonts;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
		
	}
	
	public void updateFontTexture(FontType font) {
		glActiveTexture(GL_TEXTURE0);
		font.getTextureAtlas().bind();
		setUniform("fontAtlas", 0);
	}
	
	public void updateUniforms(Vector3f color, Vector2f translation) {
		setUniform("color", color);
		setUniform("translation", translation);
	}
}
