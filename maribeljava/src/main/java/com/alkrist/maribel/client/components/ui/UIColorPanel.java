package com.alkrist.maribel.client.components.ui;

import org.joml.Vector4f;

import com.alkrist.maribel.client.render.memory.UIVAO;
import com.alkrist.maribel.client.render.pipeline.AlphaBlendingSrcAlpha;
import com.alkrist.maribel.client.render.pipeline.RenderParameter;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;

public class UIColorPanel extends UIElement{

	private ShaderProgram shader;
	private RenderParameter config;
	private UIVAO vao;
	private Vector4f color;
	
	public UIColorPanel(Vector4f colorRGBA, int xPos, int yPos, int scaleX, int scaleY, UIVAO panelMeshBuffer) {
		super(xPos, yPos, scaleX, scaleY);
		
		this.color = colorRGBA;
		shader = UIColorPanelShader.getInstance();
		vao = panelMeshBuffer;
		config = new AlphaBlendingSrcAlpha();
	}

	public void render(){
		config.enable();
		shader.bind();
		shader.updateUniforms(getOrthographicMatrix());
		shader.updateUniforms(color);
		vao.draw();
		config.disable();
	}
}
