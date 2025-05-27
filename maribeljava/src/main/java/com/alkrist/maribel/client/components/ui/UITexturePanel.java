package com.alkrist.maribel.client.components.ui;

import com.alkrist.maribel.client.memory.UIVAO;
import com.alkrist.maribel.client.texture.Texture;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlending;
import com.alkrist.maribel.graphics.shader.ShaderProgram;


import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class UITexturePanel extends UIElement{

	private ShaderProgram shader;
	private RenderParameter config;
	private UIVAO vao;
	private Texture texture;

	
	public UITexturePanel(Texture texture, int xPos, int yPos, int scaleX, int scaleY, UIVAO panelMeshBuffer) {
		super(xPos, yPos, scaleX, scaleY);
		shader = UITexturePanelShader.getInstance();
		vao = panelMeshBuffer;
		config = new AlphaBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		this.texture = texture; // reuse'n'recycle, bitch
	}

	public void render(){
		config.enable();
		shader.bind();
		shader.updateUniforms(getOrthographicMatrix());
		glActiveTexture(GL_TEXTURE0);
		texture.bind();
		shader.updateUniforms(0);
		vao.draw();
		config.disable();
	}
}
