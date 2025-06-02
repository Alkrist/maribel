package com.alkrist.maribel.client.render.target;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT3;
//import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT4;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

//import com.alkrist.maribel.client.render.target.FBO.Attachment;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.texture.Texture2D;
import com.alkrist.maribel.client.render.texture.Texture.ImageFormat;
import com.alkrist.maribel.client.render.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.render.texture.Texture.TextureWrapMode;

public class OpaqueForwardFbo extends FBO{

	//private int samples;
	// TODO: light scattering is cut for now
	
	public OpaqueForwardFbo(int width,int height, int samples) {
		Texture albedoAttachment = null;
		Texture worldPositionAttachment = null;
		Texture normalAttachment = null;
		Texture specularEmissionDiffuseSsaoBloomAttachment = null;
		//Texture lightScatteringAttachment = null;
		Texture depthAttachment = null;
		
		albedoAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		worldPositionAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		normalAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		specularEmissionDiffuseSsaoBloomAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		//lightScatteringAttachment = new Texture2D(width, height, samples, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
		depthAttachment = new Texture2D(width, height, samples, ImageFormat.DEPTH32FLOAT, SamplerFilter.Nearest, TextureWrapMode.ClampToEdge);
	
		attachments.put(Attachment.COLOR, albedoAttachment);
		attachments.put(Attachment.POSITION, worldPositionAttachment);
		attachments.put(Attachment.NORMAL, normalAttachment);
		attachments.put(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM, specularEmissionDiffuseSsaoBloomAttachment);
		//attachments.put(Attachment.LIGHT_SCATTERING, lightScatteringAttachment);
		attachments.put(Attachment.DEPTH, depthAttachment);
		
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(4); // 5 for light scattering
		drawBuffers.put(GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL_COLOR_ATTACHMENT1);
		drawBuffers.put(GL_COLOR_ATTACHMENT2);
		drawBuffers.put(GL_COLOR_ATTACHMENT3);
		//drawBuffers.put(GL_COLOR_ATTACHMENT4);
		drawBuffers.flip();
		
		frameBuffer = new FrameBuffer();
		frameBuffer.bind();
		
		frameBuffer.createColorTextureAttachment(albedoAttachment.getId(),0,(samples > 1));
		frameBuffer.createColorTextureAttachment(worldPositionAttachment.getId(),1,(samples > 1));
		frameBuffer.createColorTextureAttachment(normalAttachment.getId(),2,(samples > 1));
		frameBuffer.createColorTextureAttachment(specularEmissionDiffuseSsaoBloomAttachment.getId(),3,(samples > 1));
		//frameBuffer.createColorTextureAttachment(lightScatteringAttachment.getId(),4,(samples > 1));
		frameBuffer.createDepthTextureAttachment(depthAttachment.getId(),(samples > 1));
		
		frameBuffer.setDrawBuffers(drawBuffers);
		frameBuffer.checkStatus();
		frameBuffer.unbind();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
