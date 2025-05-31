package com.alkrist.maribel.client.components.ui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.utils.FileUtils;

public class UIColorPanelShader extends ShaderProgram {
	
	private static UIColorPanelShader instance = null;
	
	public static UIColorPanelShader getInstance() {
		if(instance == null) {
			instance = new UIColorPanelShader();
		}
		
		return instance;
	}

	protected UIColorPanelShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/color_panel_vertex.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/components/ui/color_panel_fragment.glsl")));
		compileShader();
		
		addUniform("orthographicMatrix");
		addUniform("color");
	}
	
	public void updateUniforms(Matrix4f orthographicMatrix){
		setUniform("orthographicMatrix", orthographicMatrix);
	}
	
	public void updateUniforms(Vector4f color){
		setUniform("color", color);
	}
}
