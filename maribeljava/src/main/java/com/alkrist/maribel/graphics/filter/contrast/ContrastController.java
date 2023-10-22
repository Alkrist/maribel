package com.alkrist.maribel.graphics.filter.contrast;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.filter.PPEProperty;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class ContrastController {

	public static final int ID = 0;
	
	private Texture contrastTexture;
	
	private ContrastShader shader;
	
	public ContrastController() {
		
		this.shader = ContrastShader.getInstance();
		
		this.contrastTexture = new Texture2D(GLContext.getConfig().width, GLContext.getConfig().height,
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
	}
	
	public void render(PPEProperty prop, Texture sceneSampler) {
		shader.bind();
		
		glBindImageTexture(0, sceneSampler.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(1, contrastTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		
		shader.updateUniforms(prop);
		
		glDispatchCompute(GLContext.getConfig().width/16, GLContext.getConfig().height/16, 1);	
		
		glFinish();
	}
	
	public Texture getContrastTexture() {
		return contrastTexture;
	}
}
