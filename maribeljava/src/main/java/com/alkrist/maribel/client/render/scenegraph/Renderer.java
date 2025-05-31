package com.alkrist.maribel.client.render.scenegraph;

import com.alkrist.maribel.client.render.memory.VBO;
import com.alkrist.maribel.client.render.pipeline.RenderParameter;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;

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
