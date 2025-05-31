package com.alkrist.maribel.client.components.ui;

import org.joml.Matrix4f;

import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
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
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/texture_panel_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/texture_panel_fragment.glsl")));
		compileShader();
		
		addUniform("orthographicMatrix");
		addUniform("texture");
	}
	
	public void updateUniforms(Matrix4f orthographicMatrix){
		setUniform("orthographicMatrix", orthographicMatrix);
	}
	
	public void updateUniforms(int texture){
		setUniform("texture", texture);
	}
}
