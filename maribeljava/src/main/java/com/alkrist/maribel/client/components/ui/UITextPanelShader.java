package com.alkrist.maribel.client.components.ui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class UITextPanelShader extends ShaderProgram{

	private static UITextPanelShader instance = null;
	
	public static UITextPanelShader getInstance() {
		if(instance == null) {
			instance = new UITextPanelShader();
		}
		
		return instance;
	}
	
	protected UITextPanelShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/text_panel_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/text_panel_fragment.glsl")));
		compileShader();
		
		addUniform("orthographicMatrix");
		addUniform("texture");
		addUniform("color");
	}
	
	public void updateUniforms(Matrix4f orthographicMatrix){
		setUniform("orthographicMatrix", orthographicMatrix);
	}
	
	public void updateUniforms(int texture){
		setUniform("texture", texture);
	}
	
	public void updateUniforms(Vector4f color) {
		setUniform("color", color);
	}
}
