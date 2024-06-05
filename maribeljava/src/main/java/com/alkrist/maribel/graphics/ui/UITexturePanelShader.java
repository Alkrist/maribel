package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtils;

public class UITexturePanelShader extends ShaderProgram{

	private static UITexturePanelShader instance = null;
	
	public static UITexturePanelShader getInstance() {
	    if(instance == null) {
	    	instance = new UITexturePanelShader();
	    }
	    return instance;
	}
	
	protected UITexturePanelShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/ui/texture_panel_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/ui/texture_panel_fragment.glsl")));
		compileShader();
		
		addUniform("transformationMatrix");
		addUniform("uiTexture");
	}
	
	public void updateUniforms(UITexturePanel panel) {
		
		setUniform("transformationMatrix", panel.getTransformationMatrix());
		
		Texture texture = panel.getTexture();
		glActiveTexture(GL_TEXTURE0);
		texture.bind();
		setUniform("uiTexture", 0);
	}
}
