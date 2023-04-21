package com.alkrist.maribel.graphics.texture;

import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL31.GL_RGBA8_SNORM;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

public class Texture2DMultisample extends Texture{

	public Texture2DMultisample(int width, int height, int samples, ImageFormat imageFormat) {
		super(GL_TEXTURE_2D_MULTISAMPLE, width, height);
		
		bind();
		
		switch(imageFormat)
		{
			case RGBA8_SNORM:
				allocateImage2DMultisample(samples, GL_RGBA8_SNORM); break;
			case RGBA16FLOAT:
				allocateImage2DMultisample(samples, GL_RGBA16F); break;
			case RGBA32FLOAT:
				allocateImage2DMultisample(samples, GL_RGBA32F); break;
			case RGB32FLOAT:
				allocateImage2DMultisample(samples, GL_RGB32F); break;
			case DEPTH32FLOAT:
				allocateImage2DMultisample(samples, GL_DEPTH_COMPONENT32F); break;
			case R16FLOAT:
				allocateImage2DMultisample(samples, GL_R16F); break;
			case R32FLOAT:
				allocateImage2DMultisample(samples, GL_R32F); break;
			default:
				Logging.getLogger().log(Level.WARNING, "Image format is not supported! Allocating default format: GL_RGBA16F");
				allocateImage2DMultisample(samples, GL_RGBA16F);
		}
		
		unbind();
	}

	public Texture2DMultisample(int width, int height, int samples,
			ImageFormat imageFormat, SamplerFilter samplerFilter){
		this(width, height, samples, imageFormat);
		
		bind();
		
		switch(samplerFilter)
		{
			case Nearest:
				nearestFilter(); break;
			case Bilinear:
				bilinearFilter(); break;
			case Trilinear:
				trilinearFilter(); break;
			case Anisotropic:
				anisotropicFilter(); break;
		}
		
		unbind();
	}
	
	public Texture2DMultisample(int width, int height, int samples,
			ImageFormat imageFormat, SamplerFilter samplerFilter, TextureWrapMode textureWrapMode) {
		this(width, height, samples, imageFormat, samplerFilter);
		
		bind();
		
		switch(textureWrapMode)
		{
			case ClampToBorder:
				clampToBorder(); break;
			case ClampToEdge:
				clampToEdge(); break;
			case MirrorRepeat:
				mirroredRepeat(); break;
			case Repeat:
				repeat(); break;
		}
		
		unbind();
	}
}
