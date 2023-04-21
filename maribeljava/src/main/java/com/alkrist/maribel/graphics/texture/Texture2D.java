package com.alkrist.maribel.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_R8;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL31.GL_RGBA8_SNORM;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

public class Texture2D extends Texture{

	public Texture2D(int width, int height, ImageFormat imageFormat) {
		super(GL_TEXTURE_2D, width, height);
		
		bind();
		
		switch(imageFormat)
		{
			case RGBA16FLOAT:
				allocateImage2D(GL_RGBA16F, GL_RGBA, GL_FLOAT); break;
			case RGBA32FLOAT:
				allocateImage2D(GL_RGBA32F, GL_RGBA, GL_FLOAT); break;
			case DEPTH32FLOAT:
				allocateImage2D(GL_DEPTH_COMPONENT32F, GL_DEPTH_COMPONENT, GL_FLOAT); break;
			case R16FLOAT:
				allocateImage2D(GL_R16F, GL_RED, GL_FLOAT); break;
			case R32FLOAT:
				allocateImage2D(GL_R32F, GL_RED, GL_FLOAT); break;
			case R8:
				allocateImage2D(GL_R8, GL_RED, GL_UNSIGNED_BYTE); break;
			default:
				Logging.getLogger().log(Level.WARNING, "Image format is not supported! Allocating default format: GL_RGBA16F");
				allocateImage2D(GL_RGBA16F, GL_RGBA, GL_FLOAT); break;
		}
		
		unbind();
	}

	public Texture2D(int width, int height, ImageFormat imageFormat, SamplerFilter samplerFilter){
		
		this(width, height, imageFormat);
		
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

	public Texture2D(int width, int height,
			ImageFormat imageFormat, SamplerFilter samplerFilter, TextureWrapMode textureWrapMode) {
		this(width, height, imageFormat, samplerFilter);
		
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
	
	public Texture2D(String file, SamplerFilter samplerFilter){
		super(file);
		
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
	
	public Texture2D(String file, SamplerFilter samplerFilter,
			TextureWrapMode textureWrapMode) {
		this(file, samplerFilter);
		
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
	
	public Texture2D(int width, int height, int samples, ImageFormat imageFormat){
		super(samples > 1 ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D, width, height);
		
		bind();
		
		switch(imageFormat)
		{
			case RGBA8_SNORM:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_RGBA8_SNORM);
				else
					allocateImage2D(GL_RGBA8_SNORM, GL_RGBA, GL_FLOAT);
				break;
			case RGBA16FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_RGBA16F);
				else
					allocateImage2D(GL_RGBA16F, GL_RGBA, GL_FLOAT);
				break;
			case RGBA32FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_RGBA32F);
				else
					allocateImage2D(GL_RGBA32F, GL_RGBA, GL_FLOAT);
				break;
			case RGB32FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_RGB32F);
				else
					allocateImage2D(GL_RGB32F, GL_RGBA, GL_FLOAT);
				break;
			case DEPTH32FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_DEPTH_COMPONENT32F);
				else
					allocateImage2D(GL_DEPTH_COMPONENT32F, GL_DEPTH_COMPONENT, GL_FLOAT);
				break;
			case R16FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_R16F);
				else
					allocateImage2D(GL_R16F, GL_RED, GL_FLOAT);
				break;
			case R32FLOAT:
				if (samples > 1)
					allocateImage2DMultisample(samples, GL_R32F);
				else
					allocateImage2D(GL_R32F, GL_RED, GL_FLOAT);
				break;
			default:
				Logging.getLogger().log(Level.WARNING, "Image format is not supported! Allocating default format: GL_RGBA16F");
				allocateImage2D(GL_RGBA16F, GL_RGBA, GL_FLOAT); break;
		}
		
		unbind();
	}

	public Texture2D(int width, int height, int samples,
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
	
	public Texture2D(int width, int height, int samples,
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
