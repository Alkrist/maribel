package com.alkrist.maribel.client.render.target;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.texture.Texture2D;
import com.alkrist.maribel.client.render.texture.Texture.ImageFormat;
import com.alkrist.maribel.client.render.texture.Texture.SamplerFilter;

public class TransparentForwardFbo extends FBO {

	public TransparentForwardFbo(int width, int height) {
		Texture albedoAttachment = null;
		Texture alphaAttachment = null;
		Texture lightScatteringAttachment = null;
		Texture depthAttachment = null;

		albedoAttachment = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		alphaAttachment = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		lightScatteringAttachment = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		depthAttachment = new Texture2D(width, height, ImageFormat.DEPTH32FLOAT, SamplerFilter.Nearest);

		attachments.put(Attachment.COLOR, albedoAttachment);
		attachments.put(Attachment.ALPHA, alphaAttachment);
		attachments.put(Attachment.LIGHT_SCATTERING, lightScatteringAttachment);
		attachments.put(Attachment.DEPTH, depthAttachment);

		IntBuffer drawBuffers = BufferUtils.createIntBuffer(3);
		drawBuffers.put(GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL_COLOR_ATTACHMENT1);
		drawBuffers.put(GL_COLOR_ATTACHMENT2);
		drawBuffers.flip();

		frameBuffer = new FrameBuffer();
		frameBuffer.bind();
		frameBuffer.createColorTextureAttachment(albedoAttachment.getId(), 0);
		frameBuffer.createColorTextureAttachment(alphaAttachment.getId(), 1);
		frameBuffer.createColorTextureAttachment(lightScatteringAttachment.getId(), 2);
		frameBuffer.createDepthTextureAttachment(depthAttachment.getId());
		frameBuffer.setDrawBuffers(drawBuffers);
		frameBuffer.checkStatus();
		frameBuffer.unbind();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

}
