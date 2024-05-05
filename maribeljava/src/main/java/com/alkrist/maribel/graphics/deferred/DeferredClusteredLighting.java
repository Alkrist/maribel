package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL42.glMemoryBarrier;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;

public class DeferredClusteredLighting {

	protected static final int GRID_SIZE_X = 12;
	protected static final int GRID_SIZE_Y = 12;
	protected static final int GRID_SIZE_Z = 24;
	protected static final int NUM_CLUSTERS = GRID_SIZE_X * GRID_SIZE_Y * GRID_SIZE_Z;
	
	private static final int CLUSTER_ALIGNED_SIZE = 448;
	private static final int LIGHT_ALIGNED_SIZE = 48;
	
	private Texture deferredSceneTexture;
	
	private DeferredClusterShader clusterShader;
	private DeferredLightAABBCullingShader lightCullingShader;
	//TODO: add lighting shader
	
	private int clusterSSBO;
	private int lightSSBO;
	
	private int width;
	private int height;
	
	private boolean isFirstPass = true;
	
	public DeferredClusteredLighting(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.clusterShader = DeferredClusterShader.getInstance();
		this.lightCullingShader = DeferredLightAABBCullingShader.getInstance();
		//TODO: init lighting shader
		
		deferredSceneTexture = new Texture2D(width, height, 
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		createSSBO();
	}
	
	private void createSSBO() {
		this.clusterSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, clusterSSBO);
		

		glBufferData(GL_SHADER_STORAGE_BUFFER, CLUSTER_ALIGNED_SIZE * NUM_CLUSTERS, GL_STATIC_COPY);
		
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, clusterSSBO);
	}
	
	public void computeClusters() {
		clusterShader.bind();
		clusterShader.updateUniforms(width, height);
		glDispatchCompute(GRID_SIZE_X, GRID_SIZE_Y, GRID_SIZE_Z);
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}
	
	//TODO: make it entities
	public void updateLightSSBO(List<PointLight> pointLights) {
		ByteBuffer lightData = BufferUtils.createByteBuffer(LIGHT_ALIGNED_SIZE * pointLights.size());
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
			.put(0.0f).put(0.0f); // 4 + 4 bytes padding TODO: make one of them attenuation exponent value
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
	
	//TODO: render
	
	public Texture getDeferredSceneTexture() {
		return deferredSceneTexture;
	}
}
