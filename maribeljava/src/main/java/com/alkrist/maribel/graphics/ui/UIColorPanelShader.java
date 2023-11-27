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
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"ui\\color_panel_fragment.glsl", "gui_lib.glsl"));
		compileShader();
		
		addUniform("transformationMatrix");
		addUniform("color");
		
		addUniform("borderRadius");
		addUniform("borderThickness");
		addUniform("borderColor");
		
		addUniform("framePositionPx");
		addUniform("frameSizePx");
	}
	
	public void updateUniforms(UIColorPanel panel) {
		setUniform("transformationMatrix", panel.getTransformationMatrix());
		
		setUniform("borderRadius", panel.getBorderRadiusPixels());
		setUniform("borderThickness", panel.getBorderThicknessPixels());
		setUniform("borderColor", panel.getBorderColor());
		
		setUniform("framePositionPx", panel.getFramePositionPixels());
		setUniform("frameSizePx", panel.getFrameScalePixels());
		
		setUniform("color", panel.getColor());
	}
}
