package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL42.glMemoryBarrier;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;
import static org.lwjgl.opengl.GL43.glClearBufferData;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

public class TestCluster {

	private DeferredClusterShader shader;
	private DeferredLightAABBCullingShader lightCullingShader;
	private DeferredTestShader lightingShader;
	
	private Texture deferredSceneTexture;
	
	private int ssbo;
	private int lightSSBO;
	
	private int gridSizeX = 12;
	private int gridSizeY = 12;
	private int gridSizeZ = 24;
	private int numClusters = gridSizeX * gridSizeY * gridSizeZ;
	
	private final int clusterAlignedSize = 448; // (436 + 16 - 1) & ~(16 - 1) = 448 bytes
	private final int lightAlignedSize = 48; // (12 + 12 + 4 + 4) = 32 bytes
	
	private int width;
	private int height;
	
	private Mesh mesh;
	private RenderParameter config;
	
	private boolean isFirstPass = true;
	
	public TestCluster(int width,int height) {
		this.width = width;
		this.height = height;
		this.shader = DeferredClusterShader.getInstance();
		this.lightCullingShader = DeferredLightAABBCullingShader.getInstance();
		this.lightingShader = DeferredTestShader.getInstance();
		
		deferredSceneTexture = new Texture2D(width, height, 
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		createSSBO();
		
	}
	
	private void createSSBO() {
		this.ssbo = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		

		glBufferData(GL_SHADER_STORAGE_BUFFER, clusterAlignedSize * numClusters, GL_STATIC_COPY);
		
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, ssbo);
	}
	
	public void cullLightsCompute() {
		shader.bind();
		shader.updateUniforms(width, height);
		glDispatchCompute(gridSizeX, gridSizeY, gridSizeZ);
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}
	
	public void initLightSSBO(List<PointLight> pointLights) {
		
		ByteBuffer lightData = BufferUtils.createByteBuffer(lightAlignedSize * pointLights.size());
		FloatBuffer fv = lightData.asFloatBuffer();
		//FloatBuffer fv = BufferUtils.createFloatBuffer(lightAlignedSize * pointLights.size());
		for(PointLight light: pointLights) {
			Vector3f p = light.getPosition();
			Vector3f c = light.getColor();
			float i = light.getIntensity();
			float r = light.getRadius();
			
			fv.put(p.x).put(p.y).put(p.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(r)
			.put(0.0f).put(0.0f); // 4 + 4 bytes padding
		}
		fv.flip();
		
		this.lightSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, lightSSBO);
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		//glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, lightData);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, lightSSBO);
		
	}
	
	public void updateLightSSBO(List<PointLight> pointLights) {
		ByteBuffer lightData = BufferUtils.createByteBuffer(lightAlignedSize * pointLights.size());
		FloatBuffer fv = lightData.asFloatBuffer();
		
		for(PointLight light: pointLights) {
			Vector3f p = light.getPosition();
			Vector3f c = light.getColor();
			float i = light.getIntensity();
			float r = light.getRadius();
			
			fv.put(p.x).put(p.y).put(p.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(r)
			.put(0.0f).put(0.0f); // 4 + 4 bytes padding
		}
		
		fv.flip();
		
		if(isFirstPass) {
			this.lightSSBO = glGenBuffers();
			isFirstPass = false;
		}
		
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, lightSSBO);
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, lightData);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, lightSSBO);
		
	}
	
	public void lightAABBIntersection() {
		lightCullingShader.bind();
		lightCullingShader.updateUniforms();
		glDispatchCompute(27,1,1); // for 12x12x24 work groups of cluster shader we have 3456 threads, same as for 27 work groups
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}
	
	public void render(Texture albedo, Texture position, Texture normal, Texture specularEmissionDiffuseSSAOBloom) {
		glFinish();
		
		lightingShader.bind();
		glBindImageTexture(0, deferredSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		glBindImageTexture(2, albedo.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(3, position.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA32F);
		glBindImageTexture(4, normal.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(5, specularEmissionDiffuseSSAOBloom.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		
		lightingShader.updateUniforms(width, height, gridSizeX, gridSizeY, gridSizeZ);
		
		glDispatchCompute(width/2, height/2,1);
	}
	
	public Texture getDeferredSceneTexture() {
		return deferredSceneTexture;
	}
}
