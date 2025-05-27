package com.alkrist.maribel.client.components.ui;

import org.joml.Vector4f;

import com.alkrist.maribel.client.memory.UIVAO;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlending;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

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
		config = new AlphaBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
