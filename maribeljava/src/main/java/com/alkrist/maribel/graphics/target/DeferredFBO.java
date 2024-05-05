package com.alkrist.maribel.graphics.target;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;

public class DeferredFBO extends FBO{

	
	
	public DeferredFBO(int width,int height) {
		Texture albedoAttachment = null;
		albedoAttachment = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		
		attachments.put(Attachment.COLOR, albedoAttachment);
		
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(3);
		drawBuffers.put(GL_COLOR_ATTACHMENT0);
		drawBuffers.flip();
		
		frameBuffer = new FrameBuffer();
		frameBuffer.bind();
		frameBuffer.createColorTextureAttachment(albedoAttachment.getId(),0);
		frameBuffer.checkStatus();
		frameBuffer.unbind();
	}
}
