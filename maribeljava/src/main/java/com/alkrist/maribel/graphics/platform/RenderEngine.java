package com.alkrist.maribel.graphics.platform;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alkrist.maribel.client.core.VideoConfig;
import com.alkrist.maribel.client.render.antialiasing.FXAA;
import com.alkrist.maribel.client.render.antialiasing.SampleCoverage;
import com.alkrist.maribel.client.render.filter.ssao.SSAO;
import com.alkrist.maribel.client.render.pipeline.CCW;
import com.alkrist.maribel.client.render.shadows.PSSMCamera;
import com.alkrist.maribel.client.render.shadows.ParallelSplitShadowMapsFBO;
import com.alkrist.maribel.client.render.surface.FullScreenQuad;
import com.alkrist.maribel.client.render.target.FBO;
import com.alkrist.maribel.client.render.target.FBO.Attachment;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.transparency.OpaqueTransparencyBlending;
import com.alkrist.maribel.client.util.GLUtil;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.graphics.components.ModelShadowRenderer;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.components.TransparentModelRenderer;
import com.alkrist.maribel.graphics.components.light.DirectionLight;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.deferred.DeferredClusteredLighting;
import com.alkrist.maribel.graphics.target.OffScreenFBO;
import com.alkrist.maribel.graphics.target.TransparencyFBO;
import com.alkrist.maribel.graphics.ui.WindowCanvas;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class RenderEngine {

	private static final ComponentMapper<OpaqueModelRenderer> opaqueModelRendererMapper = ComponentMapper.getFor(OpaqueModelRenderer.class);
	private static final ComponentMapper<ModelShadowRenderer> opaqueModelShadowMapper = ComponentMapper.getFor(ModelShadowRenderer.class);
	private static final ComponentMapper<TransparentModelRenderer> transparentRendererMapper = ComponentMapper.getFor(TransparentModelRenderer.class);
	private static final ComponentMapper<WindowCanvas> windowUIMapper = ComponentMapper.getFor(WindowCanvas.class);
	
	private Engine engine;
	
	private GLWindow window;
	private VideoConfig config;
	
	private int width;
	private int height;
	
	private FullScreenQuad fullScreenQuad;
	private FBO primarySceneFBO; //resized
	private FBO secondarySceneFBO; //resized
	private ParallelSplitShadowMapsFBO pssmFBO;
	private SSAO ssao; //resized
	private FXAA fxaa; //resized
	private SampleCoverage sampleCoverage; //resized
	private OpaqueTransparencyBlending opaqueTransparencyBlending; //resized
	
	private DeferredClusteredLighting deferredClusteredLighting; // resized
	
	private ImmutableArrayList<Entity> opaqueSceneRenderList;
	private ImmutableArrayList<Entity> transparentSceneRenderList;
	private ImmutableArrayList<Entity> shadowSceneRenderList;
	private ImmutableArrayList<Entity> windowCanvases;
	private ImmutableArrayList<Entity> pointLightEntities;
	private ImmutableArrayList<Entity> directionLightEntities;
	
	public RenderEngine(Engine engine) {
		this.window = GLContext.getWindow();
		this.config = GLContext.getConfig();
		
		this.width = window.getWidth();
		this.height = window.getHeight();
		
		this.fullScreenQuad = new FullScreenQuad(new CCW());
		primarySceneFBO = new OffScreenFBO(width, height, GLContext.getConfig().multisampleSamplesCount);
		secondarySceneFBO = new TransparencyFBO(width, height);
		pssmFBO = new ParallelSplitShadowMapsFBO();
		PSSMCamera.init();
		
		ssao = new SSAO(width, height);
		fxaa = new FXAA(width, height);
		sampleCoverage = new SampleCoverage(width, height);
		opaqueTransparencyBlending = new OpaqueTransparencyBlending(width, height);
		
		deferredClusteredLighting = new DeferredClusteredLighting(width, height);
		deferredClusteredLighting.computeClusters();
		
		this.engine = engine;
		//TODO: no test renderer later
		opaqueSceneRenderList = engine.getEntitiesOf(Family.all(OpaqueModelRenderer.class, Transform.class, Renderable.class).get());
		transparentSceneRenderList = engine.getEntitiesOf(Family.all(Transform.class, Renderable.class, TransparentModelRenderer.class).get());
		shadowSceneRenderList = engine.getEntitiesOf(Family.all(ModelShadowRenderer.class, Transform.class, Renderable.class).get());
		windowCanvases = engine.getEntitiesOf(Family.all(WindowCanvas.class).get());
		pointLightEntities = engine.getEntitiesOf(Family.all(PointLight.class).get());
		directionLightEntities = engine.getEntitiesOf(Family.all(DirectionLight.class).get());
		
		deferredClusteredLighting.initPointLightSSBO(pointLightEntities);
		deferredClusteredLighting.initDirectionLightSSBO(directionLightEntities);
		
		glFinish();
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
		//TODO: INIT render lists
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public void render() {
		
		//===================================//
		//        CLEAR RENDER BUFFER        //
		//===================================//
		GLUtil.clearScreen();
		primarySceneFBO.bind();
		GLUtil.clearScreen();
		secondarySceneFBO.bind();
		GLUtil.clearScreen();
		pssmFBO.getFbo().bind();
		glClear(GL_DEPTH_BUFFER_BIT);
		pssmFBO.getFbo().unbind();
		
		//===================================//
		//        RENDER SHADOW MAPS         //
		//===================================//
		if(GLContext.getConfig().isShadowMapsEnabled) {
			pssmFBO.getFbo().bind();
			pssmFBO.getParameter().enable();
			glViewport(0,0,config.shadowMapResolution, config.shadowMapResolution);
					
			for(Entity e: shadowSceneRenderList)
				opaqueModelShadowMapper.getComponent(e).render(e);
					
			glViewport(0, 0, width, height);
			pssmFBO.getParameter().disable();
			pssmFBO.getFbo().unbind();
		}
		
		//===================================//
		//        RENDER OPAQUE OBJECTS      //
		//===================================//
		primarySceneFBO.bind();
		for(Entity e: opaqueSceneRenderList) {
			if(opaqueModelRendererMapper.hasComponent(e))
				opaqueModelRendererMapper.getComponent(e).render(e);
		}
		primarySceneFBO.unbind();
		
		//===================================//
		//     RENDER TRANSPARENT OBJECTS    //
		//===================================//	
		secondarySceneFBO.bind();
		for(Entity e: transparentSceneRenderList) {
			if(transparentRendererMapper.hasComponent(e))
				transparentRendererMapper.getComponent(e).render(e);
		}
		secondarySceneFBO.unbind();
		
		//===================================//
		//        RENDER SSAO BUFFER         //
		//===================================//
		if(GLContext.getConfig().isSSAOEnabled) {
			ssao.render(primarySceneFBO.getAttachmentTexture(Attachment.POSITION),
					primarySceneFBO.getAttachmentTexture(Attachment.NORMAL));
		}
				
		//===================================//
		//RENDER SAMPLE COVERAGE MASK BUFFER //
		//===================================//
		if(GLContext.getConfig().isMultisamplingEnabled) {
			sampleCoverage.render(primarySceneFBO.getAttachmentTexture(Attachment.POSITION),
					primarySceneFBO.getAttachmentTexture(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM));
		}
		
		//===================================//
		//      RENDER DEFERRED LIGHTING     //
		//===================================//
				
		deferredClusteredLighting.updatePointLightSSBO(pointLightEntities);
		deferredClusteredLighting.updateDirectionLightSSBO(directionLightEntities);
		deferredClusteredLighting.lightAABBIntersection();

		deferredClusteredLighting.render(primarySceneFBO.getAttachmentTexture(Attachment.COLOR),
				primarySceneFBO.getAttachmentTexture(Attachment.POSITION),
				primarySceneFBO.getAttachmentTexture(Attachment.NORMAL),
				primarySceneFBO.getAttachmentTexture(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM),
				pssmFBO.getDepthMap(),
				ssao.getBlurSceneTexture(),
				sampleCoverage.getSampleCoverageMask());
		
		//===================================//
		//   BLEND OPAQUE/TRANSPARENT SCENE  //
		//===================================//
		if(transparentSceneRenderList.size() > 0) {
			opaqueTransparencyBlending.render(deferredClusteredLighting.getDeferredSceneTexture(),
					primarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.COLOR),
					secondarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.ALPHA));
		}
		
		//===================================//
		//       POST PROCESSED SCENE        //
		//===================================//
		/*
		 * PPE pipeline:
		 * 1) pre-postprocessing - FXAA +
		 * 2) postprocessing carried out via something, so it can be extended later and not controlled from here
		 * 3) retrieve final ppe result
		 */
		Texture prePostProcessingScene = transparentSceneRenderList.size() > 0 ? opaqueTransparencyBlending.getBlendedSceneTexture() :
			deferredClusteredLighting.getDeferredSceneTexture();
		Texture currentScene = prePostProcessingScene;
		
		/*if(GLContext.getConfig().isFXAAEnabled && GLContext.getMainCamera().isMoved()) {
			fxaa.render(currentScene);
			currentScene = fxaa.getFXAASceneTexture();
		}*/

		
		fullScreenQuad.setTexture(currentScene);
		fullScreenQuad.render();
		
		for(Entity e: windowCanvases) {
			windowUIMapper.getComponent(e).render();
		}
		
		resizeCheck();
		glViewport(0, 0,width, height);
	}
	
	public void cleanUp() {
		//TODO; clean up
	}
	
	private void resizeCheck() {
		int w = window.getWidth();
		int h = window.getHeight();
		
		if(width != w || height != h) {
			width = w;
			height = h;
			
			glFinish();
			
			// Resize FBOs
			primarySceneFBO.resize(w, h);
			secondarySceneFBO.resize(w, h);
			
			// Resize scene images
			deferredClusteredLighting.resize(w, h);
			ssao.resize(w, h);
			fxaa.resize(w, h);
			sampleCoverage.resize(w, h);
			opaqueTransparencyBlending.resize(w, h);
			
			// Resize UI
			for(Entity e: windowCanvases) {
				windowUIMapper.getComponent(e).resize(w, h);
			}
		}
	}
}
