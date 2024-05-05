package com.alkrist.maribel.graphics.deferred;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.PostProcessingVolume;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class DeferredClusteredLighting {

	private static final float LIGHT_MAX_RENDER_DISTANCE = 500.0f;
	private static final ComponentMapper<PointLight> pointLightMapper = ComponentMapper.getFor(PointLight.class);
	
	/*private final int gridSizeX = 12;
	private final int gridSizeY = 12;
	private final int gridSizeZ = 24;
	private final int numClusters = gridSizeX * gridSizeY * gridSizeZ;*/
	
	//private final int clusterAlignedSize = 448; // (436 + 16 - 1) & ~(16 - 1) = 448 bytes, padding 12 bytes
	private final int lightAlignedSize = 48; // (40 + 16 - 1) & ~(16 - 1) = 48 bytes, padding 8 bytes
	
	//private DeferredClusterShader clusterShader;
	//private DeferredLightAABBCullingShader lightCullingShader;
	//TODO: add render shader
	
	private Texture deferredSceneTexture;
	
	//private int clusterSSBO;
	private int lightSSBO;
	
	private int width;
	private int height;
	
	private boolean isFirstPass; //Defines if light buffer should be generated
	
	public DeferredClusteredLighting(int width, int height) {
		this.width = width;
		this.height = height;
		//this.clusterShader = DeferredClusterShader.getInstance();
		//this.lightCullingShader = DeferredLightAABBCullingShader.getInstance();
		//TODO: init render shader
		
		deferredSceneTexture = new Texture2D(width, height, 
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		isFirstPass = true;
	}
	
	/*private void createSSBO() {
		this.clusterSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, clusterSSBO);
		
		glBufferData(GL_SHADER_STORAGE_BUFFER, clusterAlignedSize * numClusters, GL_STATIC_COPY);
		
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, clusterSSBO);
	}*/
	
	/*public void computeClusters() {
		clusterShader.bind();
		clusterShader.updateUniforms(width, height, gridSizeX, gridSizeY, gridSizeZ);
		glDispatchCompute(gridSizeX, gridSizeY, gridSizeZ);
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}*/
	
	/*public void initLightSSBO(List<PointLight> pointLights) {
		
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
		
		this.lightSSBO = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, lightSSBO);
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, lightSSBO);
		
	}*/
	
	public void updateLightSSBO(ImmutableArrayList<Entity> lightEntities) {
		List<PointLight> pointLights = filterLights(lightEntities);
		
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
		glBufferData(GL_SHADER_STORAGE_BUFFER, lightData, GL_DYNAMIC_DRAW);
		//glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, lightData);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, lightSSBO);
		
	}
	
	private List<PointLight> filterLights(ImmutableArrayList<Entity> lightEntities){
		List<PointLight> lights = new ArrayList<PointLight>();
		
		for(Entity e: lightEntities) {
				PointLight light = pointLightMapper.getComponent(e);
				if(light.getDistanceToLight(GLContext.getMainCamera().getPosition()) <= LIGHT_MAX_RENDER_DISTANCE) {
					lights.add(light);
				}
		}
		
		return lights;
	}
	/*public void lightAABBIntersection() {
		lightCullingShader.bind();
		lightCullingShader.updateUniforms();
		glDispatchCompute(27,1,1); // for 12x12x24 work groups of cluster shader we have 3456 threads, same as for 27 work groups
		glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
	}*/
	
	public Texture getDeferredSceneTexture() {
		return deferredSceneTexture;
	}
}
