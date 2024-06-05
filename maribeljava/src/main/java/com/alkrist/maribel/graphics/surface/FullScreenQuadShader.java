package com.alkrist.maribel.graphics.surface;


import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.utils.FileUtils;

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
		
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/quad/fullscreen_VS.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/quad/fullscreen_FS.glsl")));
		//addGeometryShader(readShaderFromFile(FileUtil.getShadersPath()+"quad\\biba.glsl"));
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
