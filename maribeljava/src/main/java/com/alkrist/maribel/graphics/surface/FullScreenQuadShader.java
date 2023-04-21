package com.alkrist.maribel.graphics.surface;


import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtil;

public class FullScreenQuadShader extends ShaderProgram{

	private static FullScreenQuadShader instance = null;
	
	public static FullScreenQuadShader getInstance() {
	    if(instance == null) {
	    	instance = new FullScreenQuadShader();
	    }
	    return instance;
	}
	
	protected FullScreenQuadShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"quad\\fullscreen_VS.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"quad\\fullscreen_FS.glsl"));
		compileShader();
		
		addUniform("texture");
	}
	
	@Override
	public void updateUniforms(Texture texture) {
		glActiveTexture(GL_TEXTURE0);
		texture.bind();
		setUniform("texture", 0);
	}
}
