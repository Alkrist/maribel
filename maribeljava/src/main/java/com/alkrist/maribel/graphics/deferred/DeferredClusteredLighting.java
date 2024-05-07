package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R8;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL42.glMemoryBarrier;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.light.AmbientLight;
import com.alkrist.maribel.graphics.components.light.DirectionLight;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class DeferredClusteredLighting {

	protected static final int GRID_SIZE_X = 12;
	protected static final int GRID_SIZE_Y = 12;
	protected static final int GRID_SIZE_Z = 24;
	protected static final int NUM_CLUSTERS = GRID_SIZE_X * GRID_SIZE_Y * GRID_SIZE_Z;
	
	private static final int CLUSTER_ALIGNED_SIZE = 448;
	private static final int LIGHT_ALIGNED_SIZE = 48;
	
	private static final ComponentMapper<PointLight> pointLightMapper = ComponentMapper.getFor(PointLight.class);
	private static final ComponentMapper<DirectionLight> directionLightMapper = ComponentMapper.getFor(DirectionLight.class);
	
	private Texture deferredSceneTexture;
	
	private DeferredClusterShader clusterShader;
	private DeferredLightAABBCullingShader lightCullingShader;
	private DeferredClusteredLightingShader lightShader;
	
	private int clusterSSBO;
	private int pointLightSSBO;
	private int directionLightSSBO;
	
	private int width;
	private int height;
	
	
	public DeferredClusteredLighting(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.clusterShader = DeferredClusterShader.getInstance();
		this.lightCullingShader = DeferredLightAABBCullingShader.getInstance();
		this.lightShader = DeferredClusteredLightingShader.getInstance();
		
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
	
	public void initPointLightSSBO(ImmutableArrayList<Entity> lightEntities) {
		
		ByteBuffer lightData = BufferUtils.createByteBuffer(LIGHT_ALIGNED_SIZE * lightEntities.size());
		FloatBuffer fv = lightData.asFloatBuffer();
		for(Entity entity: lightEntities) {
			PointLight light = pointLightMapper.getComponent(entity);
			
			Vector3f p = light.getPosition();
			Vector3f c = light.getColor();
			float i = light.getIntensity();
			float r = light.getRadius();
			float e = light.getAttenuationFactor();
			
			fv.put(p.x).put(p.y).put(p.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(r)
			.put(e)
			.put(0.0f); // 4 bytes padding
		}
		fv.flip();
		
		this.pointLightSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, pointLightSSBO);
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointLightSSBO);
		
	}

	public void updatePointLightSSBO(ImmutableArrayList<Entity> lightEntities) {
		ByteBuffer lightData = BufferUtils.createByteBuffer(LIGHT_ALIGNED_SIZE * lightEntities.size());
		FloatBuffer fv = lightData.asFloatBuffer();
		
		for(Entity entity: lightEntities) {
			PointLight light = pointLightMapper.getComponent(entity);
			
			Vector3f p = light.getPosition();
			Vector3f c = light.getColor();
			float i = light.getIntensity();
			float r = light.getRadius();
			float e = light.getAttenuationFactor();
			
			fv.put(p.x).put(p.y).put(p.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(r)
			.put(e)
			.put(0.0f); // 4 bytes padding
		}
		
		fv.flip();
		
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, pointLightSSBO);
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, lightData);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointLightSSBO);
	}
	
	public void initDirectionLightSSBO(ImmutableArrayList<Entity> lightEntities) {
		ByteBuffer lightData = BufferUtils.createByteBuffer(48 * lightEntities.size());
		FloatBuffer fv = lightData.asFloatBuffer();
		
		for(Entity entity: lightEntities) {
			System.out.println("here");
			DirectionLight light = directionLightMapper.getComponent(entity);
			
			Vector3f d = light.getDirection();
			Vector3f c = light.getColor();
			float i = light.getIntensity();
			
			fv.put(d.x).put(d.y).put(d.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(0.0f)
			.put(0.0f)
			.put(0.0f);
		}
		fv.flip();
		
		this.directionLightSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, directionLightSSBO);
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, directionLightSSBO);
	}
	
	public void updateDirectionLightSSBO(ImmutableArrayList<Entity> lightEntities) {
		ByteBuffer lightData = BufferUtils.createByteBuffer(48 * lightEntities.size());
		FloatBuffer fv = lightData.asFloatBuffer();

		for(Entity entity: lightEntities) {

			DirectionLight light = directionLightMapper.getComponent(entity);
			
			Vector3f d = light.getDirection();
			Vector3f c = light.getColor();
			float i = light.getIntensity();

			fv.put(d.x).put(d.y).put(d.z).put(1.0f)
			.put(c.x).put(c.y).put(c.z).put(1.0f)
			.put(i)
			.put(0.0f)
			.put(0.0f)
			.put(0.0f);
		}
		fv.flip();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, directionLightSSBO);
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, directionLightSSBO);
	}
	
	public void lightAABBIntersection() {
		lightCullingShader.bind();
		lightCullingShader.updateUniforms();
		glDispatchCompute(27,1,1); // for 12x12x24 work groups of cluster shader we have 3456 threads, same as for 27 work groups
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}
	
	public void render(Texture albedo, Texture position, Texture normal, Texture specularEmissionDiffuseSSAOBloom,
			Texture pssm, Texture ssaoBlurScene, Texture sampleCoverageMask) {
		
		glFinish();
		
		lightShader.bind();
		glBindImageTexture(0, deferredSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		glBindImageTexture(2, albedo.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(3, position.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA32F);
		glBindImageTexture(4, normal.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(5, specularEmissionDiffuseSSAOBloom.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(7, sampleCoverageMask.getId(), 0, false, 0, GL_READ_ONLY, GL_R8);
		
		lightShader.updateUniforms(width, height, GRID_SIZE_X, GRID_SIZE_Y, GRID_SIZE_Z, pssm);
		
		if (GLContext.getConfig().isSSAOEnabled)
			glBindImageTexture(6, ssaoBlurScene.getId(), 0, false, 0, GL_READ_ONLY, GL_R16F);
		
		glDispatchCompute(width/2, height/2, 1);
	}
	
	public Texture getDeferredSceneTexture() {
		return deferredSceneTexture;
	}
}
