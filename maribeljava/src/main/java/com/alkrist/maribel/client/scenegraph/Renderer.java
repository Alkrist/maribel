package com.alkrist.maribel.client.scenegraph;

import com.alkrist.maribel.client.memory.VBO;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public class Renderer extends NodeComponent{

	private ShaderProgram shader;
	private RenderParameter config;
	private VBO vbo;
	
	public Renderer(ShaderProgram shader, RenderParameter config, VBO vbo){
		this.shader = shader;
		this.config = config;
		this.vbo = vbo;
	}
	
	public ShaderProgram getShader() {
		return shader;
	}
	
	public RenderParameter getConfig() {
		return config;
	}
	
	public VBO getVBO() {
		return vbo;
	}
	
	public void render(){
		
		config.enable();
		shader.bind();			
		shader.updateUniforms(getParent());
		vbo.draw();
		config.disable();
	}
}
