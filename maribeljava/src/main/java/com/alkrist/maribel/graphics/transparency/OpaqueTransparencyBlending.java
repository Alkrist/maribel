package com.alkrist.maribel.graphics.transparency;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import com.alkrist.maribel.graphics.render.parameter.AlphaBlendingSrcAlpha;
import com.alkrist.maribel.graphics.surface.FullScreenQuad;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class OpaqueTransparencyBlending extends FullScreenQuad{

	private OpaqueTransparencyBlendShader shader;
	
	private Texture blendedSceneTexture;
	
	private int width;
	private int height;
	
	public OpaqueTransparencyBlending(int width, int height) {
		super(new AlphaBlendingSrcAlpha());
		
		this.width = width;
		this.height = height;
		
		shader = OpaqueTransparencyBlendShader.getInstance();
		
		blendedSceneTexture = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
	}
	
	public void resize(int w, int h) {
		width = w;
		height = h;
		
		blendedSceneTexture.delete();
		blendedSceneTexture = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
	}
	
	public void render(Texture opaqueScene, Texture opaqueSceneDepthMap, Texture transparencyLayer,
			Texture transparencyLayerDepthMap, Texture alphaMap) {
		glFinish();
		
		shader.bind();
		glBindImageTexture(0, blendedSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		glBindImageTexture(1, opaqueScene.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(2, transparencyLayer.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		
		shader.updateUniforms(alphaMap, opaqueSceneDepthMap, transparencyLayerDepthMap);
		
		glDispatchCompute(width/16, height/16, 1);
	}
	
	public Texture getBlendedSceneTexture() {
		return blendedSceneTexture;
	}
}
