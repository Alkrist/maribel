package com.alkrist.maribel.graphics.surface;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.render.parameter.DefaultRenderParameter;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;

public class FullScreenQuad {

	private Texture texture;
	private ShaderProgram shader;
	private Mesh mesh;
	private RenderParameter config;
	
	
	public FullScreenQuad() {
		shader = FullScreenQuadShader.getInstance();
		config = new CCW();
		
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};

		this.mesh = ResourceLoader.loadToVAO(positions, 2);
	}
	
	public void render() {
		config.enable();
		
		shader.bind();
		shader.updateUniforms(texture);
		
		glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0); // vertices
		//glEnableVertexAttribArray(1); // texture coords
		
		glDrawArrays(GL_TRIANGLE_STRIP, 0, mesh.getVertexCount());
		
		glDisableVertexAttribArray(0); // vertices
		//glDisableVertexAttribArray(1); // texture coords
		glBindVertexArray(0);
		
		config.disable();
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public ShaderProgram getShader() {
		return shader;
	}

	public void setShader(ShaderProgram shader) {
		this.shader = shader;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public RenderParameter getConfig() {
		return config;
	}

	public void setConfig(RenderParameter config) {
		this.config = config;
	}
	
}
