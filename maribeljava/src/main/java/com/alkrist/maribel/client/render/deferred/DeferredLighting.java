package com.alkrist.maribel.client.render.deferred;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_R8;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.texture.Texture2D;
import com.alkrist.maribel.client.render.texture.Texture.ImageFormat;
import com.alkrist.maribel.client.render.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.render.texture.Texture.TextureWrapMode;

public class DeferredLighting {

private Texture deferredSceneTexture;
	
	private int width;
	private int height;
	
	private DeferredLightingShader shader;
	
	public DeferredLighting(int width, int height) {
		this.width = width;
		this.height = height;
		
		shader = DeferredLightingShader.getInstance();
		deferredSceneTexture = new Texture2D(width, height, 
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
	}
	
	public void render(Texture albedo, Texture position, Texture normal, 
			Texture specularEmissionDiffuseSSAOBloom, Texture sampleCoverageMask) {
		glFinish();
		shader.bind();
		
		glBindImageTexture(0, deferredSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);	
		glBindImageTexture(2, albedo.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(3, position.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA32F);
		glBindImageTexture(4, normal.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(5, specularEmissionDiffuseSSAOBloom.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(7, sampleCoverageMask.getId(), 0, false, 0, GL_READ_ONLY, GL_R8);
		
		shader.updateUniforms(Context.getVideoConfig().multisampleSamplesCount);
		glDispatchCompute(width/2, height/2,1);
	}
	
	public Texture getDeferredSceneTexture() {
		return deferredSceneTexture;
	}
}
