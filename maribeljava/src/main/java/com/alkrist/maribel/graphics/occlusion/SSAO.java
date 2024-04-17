package com.alkrist.maribel.graphics.occlusion;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import org.joml.Vector3f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.graphics.texture.Texture2DStorage;

public class SSAO {

	private Texture sceneTexture;
	private Texture blurSceneTexture;
	
	private Texture debugTexture;
	
	private int kernelSize;
	private Vector3f[] kernel;
	private float[] randomX;
	private float[] randomY;
	private Texture noiseTexture;
	
	private SSAOShader ssaoShader;
	private SSAOBlurShader blurShader;
	private SSAONoiseShader noiseShader;
	
	private int width;
	private int height;
	
	
	public SSAO(int width, int height) {
		this.width = width;
		this.height = height;
		
		kernelSize = 64;
		randomX = new float[16];
		randomY = new float[16];	
		for (int i=0; i<16; i++){
			randomX[i] = (float) Math.random() * 2 - 1;
			randomY[i] = (float) Math.random() * 2 - 1;
		}
		kernel = generateRandomKernel3D(kernelSize);
		
		ssaoShader = SSAOShader.getInstance();
		blurShader = SSAOBlurShader.getInstance();
		noiseShader = SSAONoiseShader.getInstance();
		
		noiseTexture = new Texture2D(4,4,ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.Repeat);
		sceneTexture = new Texture2D(width, height, ImageFormat.R16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		blurSceneTexture = new Texture2D(width, height, ImageFormat.R16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
	
		debugTexture = new Texture2D(width, height, ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		noiseShader.bind();
		glBindImageTexture(0, noiseTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		noiseShader.updateUniforms(randomX, randomY);
		glDispatchCompute(1,1,1);
	}
	
	public void render(Texture worldPos, Texture normal) {
		ssaoShader.bind();
		glBindImageTexture(0, sceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_R16F);
		glBindImageTexture(1, worldPos.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA32F);
		glBindImageTexture(2, normal.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(3, noiseTexture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		
		ssaoShader.updateUniforms(GLContext.getMainCamera().getViewMatrix(),
				GLContext.getMainCamera().getProjectionMatrix(), width, height, kernel);
		
		glDispatchCompute(width/16,height/16,1);
		
		blurShader.bind();
		glBindImageTexture(0, blurSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_R16F);
		glBindImageTexture(1, sceneTexture.getId(), 0, false, 0, GL_READ_ONLY, GL_R16F);
		glBindImageTexture(2, debugTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		
		glDispatchCompute(width/16,height/16,1);
	}
	
	public Texture getBlurSceneTexture() {
		return blurSceneTexture;
	}
	
	public Texture getDebugTexture() {
		return debugTexture;
	}
	
	private static Vector3f[] generateRandomKernel3D(int kernelSize){
		
		Vector3f[] kernel = new Vector3f[kernelSize];
		
		for (int i=0; i<kernelSize; i++){
			kernel[i] = new Vector3f((float) Math.random()*2-1,
								  (float) Math.random()*2-1,
								  (float) Math.random());
			kernel[i].normalize();
			
			float scale = (float) i / (float) kernelSize;
			
			scale = (float) Math.min(Math.max(0.01, scale*scale), 1.0);
			
			kernel[i] = kernel[i].mul(scale).mul(-1);
		}
		return kernel;
	}
}
