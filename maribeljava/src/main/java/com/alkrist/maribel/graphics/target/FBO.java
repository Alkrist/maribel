package com.alkrist.maribel.graphics.target;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.graphics.texture.Texture;

public abstract class FBO {

	protected int height; 
	protected int width;
	protected int colorAttachmentCount;
	protected int depthAttachmentCount;
	protected FrameBuffer frameBuffer;
	protected Map<Attachment, Texture> attachments = new HashMap<>();
	public Texture getAttachmentTexture(Attachment attachment){
		return attachments.get(attachment);
	}

	public void bind(){
		frameBuffer.bind();
	}
	
	public void unbind(){
		frameBuffer.unbind();
	}
	
	
	public enum Attachment {
		
		COLOR,
		ALPHA,
		NORMAL,
		POSITION,
		SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM,
		LIGHT_SCATTERING,
		DEPTH;
	}
}
