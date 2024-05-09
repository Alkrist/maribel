package com.alkrist.maribel.graphics.target;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT3;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT4;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.alkrist.maribel.graphics.target.FBO.Attachment;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class OffScreenFBO extends FBO{

	private int samples;
	
	public OffScreenFBO(int width,int height, int samples) {
		this.samples = samples;
		
		Texture albedoAttachment = null;
		Texture worldPositionAttachment = null;
		Texture normalAttachment = null;
		Texture specularEmissionDiffuseSsaoBloomAttachment = null;
		Texture lightScatteringAttachment = null;
		Texture depthAttachment = null;
		
		albedoAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		worldPositionAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		normalAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		specularEmissionDiffuseSsaoBloomAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		lightScatteringAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		depthAttachment = new Texture2D(width, height, samples, ImageFormat.DEPTH32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		
		attachments.put(Attachment.COLOR, albedoAttachment);
		attachments.put(Attachment.POSITION, worldPositionAttachment);
		attachments.put(Attachment.NORMAL, normalAttachment);
		attachments.put(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM, specularEmissionDiffuseSsaoBloomAttachment);
		attachments.put(Attachment.LIGHT_SCATTERING, lightScatteringAttachment);
		attachments.put(Attachment.DEPTH, depthAttachment);
		
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(5);
		drawBuffers.put(GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL_COLOR_ATTACHMENT1);
		drawBuffers.put(GL_COLOR_ATTACHMENT2);
		drawBuffers.put(GL_COLOR_ATTACHMENT3);
		drawBuffers.put(GL_COLOR_ATTACHMENT4);
		drawBuffers.flip();
		
		frameBuffer = new FrameBuffer();
		frameBuffer.bind();
		
		frameBuffer.createColorTextureAttachment(albedoAttachment.getId(),0,(samples > 1));
		frameBuffer.createColorTextureAttachment(worldPositionAttachment.getId(),1,(samples > 1));
		frameBuffer.createColorTextureAttachment(normalAttachment.getId(),2,(samples > 1));
		frameBuffer.createColorTextureAttachment(specularEmissionDiffuseSsaoBloomAttachment.getId(),3,(samples > 1));
		frameBuffer.createColorTextureAttachment(lightScatteringAttachment.getId(),4,(samples > 1));
		frameBuffer.createDepthTextureAttachment(depthAttachment.getId(),(samples > 1));
		
		frameBuffer.setDrawBuffers(drawBuffers);
		frameBuffer.checkStatus();
		frameBuffer.unbind();
	}

	@Override
	public void resize(int width, int height) {
		this.getAttachmentTexture(Attachment.COLOR).delete();
		this.getAttachmentTexture(Attachment.POSITION).delete();
		this.getAttachmentTexture(Attachment.NORMAL).delete();
		this.getAttachmentTexture(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM).delete();
		this.getAttachmentTexture(Attachment.LIGHT_SCATTERING).delete();
		this.getAttachmentTexture(Attachment.DEPTH).delete();
		
		Texture albedoAttachment = null;
		Texture worldPositionAttachment = null;
		Texture normalAttachment = null;
		Texture specularEmissionDiffuseSsaoBloomAttachment = null;
		Texture lightScatteringAttachment = null;
		Texture depthAttachment = null;
		
		albedoAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		worldPositionAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		normalAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		specularEmissionDiffuseSsaoBloomAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		lightScatteringAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		depthAttachment = new Texture2D(width, height, samples, ImageFormat.DEPTH32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		
		frameBuffer.bind();
		
		frameBuffer.createColorTextureAttachment(albedoAttachment.getId(),0,(samples > 1));
		frameBuffer.createColorTextureAttachment(worldPositionAttachment.getId(),1,(samples > 1));
		frameBuffer.createColorTextureAttachment(normalAttachment.getId(),2,(samples > 1));
		frameBuffer.createColorTextureAttachment(specularEmissionDiffuseSsaoBloomAttachment.getId(),3,(samples > 1));
		frameBuffer.createColorTextureAttachment(lightScatteringAttachment.getId(),4,(samples > 1));
		frameBuffer.createDepthTextureAttachment(depthAttachment.getId(),(samples > 1));
		
		frameBuffer.checkStatus();
		frameBuffer.unbind();
	}
}
