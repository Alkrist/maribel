package com.alkrist.maribel.client.components.ui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
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
		addUniform("primaryColor");
		
		//addUniform("width");
		//addUniform("edge");
		
		addUniform("borderWidth");
		addUniform("borderEdge");
		
		addUniform("outlineColor");
		addUniform("outlineOffset");
	}
	
	public void updateUniforms(Matrix4f orthographicMatrix){
		setUniform("orthographicMatrix", orthographicMatrix);
	}
	
	public void updateUniforms(int texture){
		setUniform("texture", texture);
	}
	
	public void updateUniforms(Vector3f primaryColor) {
		setUniform("primaryColor", primaryColor);
	}
	
	public void updateUniforms(Vector3f outlineColor, Vector2f outlineOffset) {
		setUniform("outlineColor", outlineColor);
		setUniform("outlineOffset", outlineOffset);
	}
	
	public void updateUniforms(float borderWidth, float borderEdge) {
		setUniform("borderWidth", borderWidth);
		setUniform("borderEdge", borderEdge);
	}
}
