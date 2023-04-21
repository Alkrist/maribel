package com.alkrist.maribel.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

import java.util.logging.Level;

import com.alkrist.maribel.utils.Logging;

public class Texture2DStorage extends Texture{

	public Texture2DStorage(int width, int height, int levels, ImageFormat imageFormat) {
		super(GL_TEXTURE_2D, width, height);
		
		bind();
		
		switch(imageFormat)
		{
			case RGBA16FLOAT:
				allocateStorage2D(levels, GL_RGBA16F); break;
			case RGBA32FLOAT:
				allocateStorage2D(levels, GL_RGBA32F); break;
			case DEPTH32FLOAT:
				allocateStorage2D(levels, GL_DEPTH_COMPONENT32F); break;
			case R16FLOAT:
				allocateStorage2D(levels, GL_R16F); break;
			case R32FLOAT:
				allocateStorage2D(levels, GL_R32F); break;
			default:
				Logging.getLogger().log(Level.WARNING, "Image format is not supported! Allocating default format: GL_RGBA16F");
				allocateStorage2D(levels, GL_RGBA16F);
		}
		
		unbind();
	}

	public Texture2DStorage(int width, int height, int levels, ImageFormat imageFormat,
			TextureWrapMode textureWrapMode) {
		this(width, height, levels, imageFormat);
		
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
