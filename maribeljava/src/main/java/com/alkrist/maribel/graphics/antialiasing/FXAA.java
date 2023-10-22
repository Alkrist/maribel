package com.alkrist.maribel.graphics.antialiasing;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class FXAA {

	private FXAAShader shader;
	
	private Texture fxaaSceneTexture;
	
	public FXAA(int width, int height) {
		shader = FXAAShader.getInstance();
		
		fxaaSceneTexture = new Texture2D(width, height, 
				ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
	}
	
	public void render(Texture sceneTexture) {
		glFinish();
		shader.bind();
		glBindImageTexture(0, fxaaSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		shader.updateUniforms(sceneTexture);
		
		glDispatchCompute(GLContext.getConfig().width/16, GLContext.getConfig().height/16, 1);
	}
	
	public Texture getFXAASceneTexture() {
		return fxaaSceneTexture;
	}
}
