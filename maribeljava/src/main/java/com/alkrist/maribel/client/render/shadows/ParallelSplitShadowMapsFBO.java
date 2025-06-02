package com.alkrist.maribel.client.render.shadows;

import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

import com.alkrist.maribel.client.render.pipeline.RenderParameter;
import com.alkrist.maribel.client.render.pipeline.ShadowRenderParameter;
import com.alkrist.maribel.client.render.target.FrameBuffer;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.texture.Texture2DArray;
import com.alkrist.maribel.client.render.texture.Texture.ImageFormat;
import com.alkrist.maribel.client.render.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.render.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.context.GLContext;

public class ParallelSplitShadowMapsFBO {
	
	FrameBuffer fbo;
	
	private Texture depthMap;
	
	private RenderParameter parameter;
	
	public ParallelSplitShadowMapsFBO() {
		parameter = new ShadowRenderParameter();
		
		depthMap = new Texture2DArray(GLContext.getConfig().shadowMapResolution, 
				GLContext.getConfig().shadowMapResolution,
				PSSMCamera.PSSM_SPLITS,
				ImageFormat.DEPTH32FLOAT,
				SamplerFilter.Bilinear,
				TextureWrapMode.ClampToEdge);
		
		fbo = new FrameBuffer();
		
		fbo.bind();
		glFramebufferTexture(GL_FRAMEBUFFER,
				GL_DEPTH_ATTACHMENT,
				depthMap.getId(),
				0);
		glDrawBuffers(GL_NONE);
		fbo.checkStatus();
		fbo.unbind();
	}

	public FrameBuffer getFbo() {
		return fbo;
	}

	public Texture getDepthMap() {
		return depthMap;
	}

	public RenderParameter getParameter() {
		return parameter;
	}
	
}
