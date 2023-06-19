package com.alkrist.maribel.graphics.ui;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;

public class UIColorPanelShader extends ShaderProgram{

	private static UIColorPanelShader instance = null;
	
	public static UIColorPanelShader getInstance() {
	    if(instance == null) {
	    	instance = new UIColorPanelShader();
	    }
	    return instance;
	}
	
	protected UIColorPanelShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"ui\\color_panel_vertex.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"ui\\color_panel_fragment.glsl"));
		compileShader();
		
		addUniform("transformationMatrix");
		addUniform("color");
	}
	
	public void updateUniforms(UIColorPanel panel) {
		setUniform("transformationMatrix", panel.getTransformationMatrix());
		
		setUniform("color", panel.getColor());
	}
}
