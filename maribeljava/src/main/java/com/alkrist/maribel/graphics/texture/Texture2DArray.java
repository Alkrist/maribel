package com.alkrist.maribel.graphics.texture;

import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

public class Texture2DArray extends Texture{

	public Texture2DArray(int width, int height, int layers, ImageFormat imageFormat){
		super(GL_TEXTURE_2D_ARRAY, width, height);
		
		bind();
		
		switch(imageFormat)
		{
			case RGBA16FLOAT:
				allocateStorage3D(1, layers, GL_RGBA16F); break;
			case RGBA32FLOAT:
				allocateStorage3D(1, layers, GL_RGBA32F); break;
			case DEPTH32FLOAT:
				allocateStorage3D(1, layers, GL_DEPTH_COMPONENT32F); break;
			case R16FLOAT:
				allocateStorage3D(1, layers, GL_R16F); break;
			case R32FLOAT:
				allocateStorage3D(1, layers, GL_R32F); break;
			default:
				Logging.getLogger().log(Level.WARNING, "Image format is not supported! Allocating default format: GL_RGBA16F");
				allocateStorage3D(1, layers, GL_RGBA16F);
		}
		
		unbind();
	}

	public Texture2DArray(int width, int height, int layers,
			ImageFormat imageFormat, SamplerFilter samplerFilter){
		this(width, height, layers, imageFormat);
		
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
	
	public Texture2DArray(int width, int height, int layers,
			ImageFormat imageFormat, SamplerFilter samplerFilter, TextureWrapMode textureWrapMode) {
		this(width, height, layers, imageFormat, samplerFilter);
		
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
