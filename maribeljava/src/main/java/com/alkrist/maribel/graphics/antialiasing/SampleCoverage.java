package com.alkrist.maribel.graphics.antialiasing;


import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_R8;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class SampleCoverage {

	private Texture sampleCoverageMask;
	private Texture specularEmissionBloomSingleSample;
	
	private SampleCoverageShader shader;
	
	private int width;
	private int height;
	
	public SampleCoverage(int width,int height) {
		this.width = width;
		this.height = height;
		
		shader = SampleCoverageShader.getInstance();
		
		sampleCoverageMask = new Texture2D(width, height, ImageFormat.R8, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		specularEmissionBloomSingleSample = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
	}
	
	public void resize(int w, int h) {
		width = w;
		height = h;
		
		sampleCoverageMask.delete();
		specularEmissionBloomSingleSample.delete();
		
		sampleCoverageMask = new Texture2D(width, height, ImageFormat.R8, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		specularEmissionBloomSingleSample = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
	}
	
	public void render(Texture worldPosition, Texture specularEmissionBloom) {
		
		shader.bind();
		
		glBindImageTexture(0, sampleCoverageMask.getId(), 0, false, 0, GL_WRITE_ONLY, GL_R8);
		glBindImageTexture(1, worldPosition.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA32F);
		glBindImageTexture(2, specularEmissionBloomSingleSample.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		glBindImageTexture(3, specularEmissionBloom.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		
		shader.updateUniforms();
		
		glDispatchCompute(width/16, height/16, 1);	
	}
	
	public Texture getSampleCoverageMask() {
		return sampleCoverageMask;
	}
	
	public Texture getSpecularEmissionBloomSingeSample() {
		return specularEmissionBloomSingleSample;
	}
}
