package com.alkrist.maribel.graphics.systems;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glViewport;

import com.alkrist.maribel.client.graphics.shader.shaders.TestRenderer;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.graphics.antialiasing.SampleCoverage;
import com.alkrist.maribel.graphics.components.ModelShadowRenderer;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.components.TransparentModelRenderer;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.context.GraphicsConfig;
import com.alkrist.maribel.graphics.deferred.DeferredLighting;
import com.alkrist.maribel.graphics.occlusion.SSAO;
import com.alkrist.maribel.graphics.platform.GLUtil;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.shadow.ParallelSplitShadowMapsFBO;
import com.alkrist.maribel.graphics.surface.FullScreenQuad;
import com.alkrist.maribel.graphics.target.FBO;
import com.alkrist.maribel.graphics.target.FBO.Attachment;
import com.alkrist.maribel.graphics.target.OffScreenFBO;
import com.alkrist.maribel.graphics.target.TransparencyFBO;
import com.alkrist.maribel.graphics.transparency.OpaqueTransparencyBlending;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class RenderSystem extends SystemBase{

	/*
	 * REMINDER
	 * Julia, whom I let down. Forgive me Julia, 
	 * I'm grateful for your honesty and trust so much.
	 * I swear,
	 * I'll change myself to be a better person.
	 */
	
	private GLWindow window;
	private GraphicsConfig config;
	
	private FullScreenQuad fullScreenQuad;
	private FBO primarySceneFBO;
	private FBO secondarySceneFBO;
	private ParallelSplitShadowMapsFBO pssmFBO;
	private SSAO ssao;
	private SampleCoverage sampleCoverage;
	private OpaqueTransparencyBlending opaqueTransparencyBlending;
	
	private DeferredLighting deferredLighting;
	
	private static final ComponentMapper<TestRenderer> testRendererMapper = ComponentMapper.getFor(TestRenderer.class);
	private static final ComponentMapper<OpaqueModelRenderer> opaqueModelRendererMapper = ComponentMapper.getFor(OpaqueModelRenderer.class);
	private static final ComponentMapper<ModelShadowRenderer> opaqueModelShadowMapper = ComponentMapper.getFor(ModelShadowRenderer.class);
	private static final ComponentMapper<TransparentModelRenderer> transparentRendererMapper = ComponentMapper.getFor(TransparentModelRenderer.class);
	
	private ImmutableArrayList<Entity> opaqueSceneRenderList;
	private ImmutableArrayList<Entity> transparentSceneRenderList;
	private ImmutableArrayList<Entity> shadowSceneRenderList;
	
	public RenderSystem() {
		super();
		this.window = GLContext.getWindow();
		this.config = GLContext.getConfig();
		this.fullScreenQuad = new FullScreenQuad(new CCW());
		createSceneFBOs();
		pssmFBO = new ParallelSplitShadowMapsFBO();
		PSSMCamera.init();
		deferredLighting = new DeferredLighting(GLContext.getWindow().getWidth(), GLContext.getWindow().getHeight());
		ssao = new SSAO(GLContext.getWindow().getWidth(), GLContext.getWindow().getHeight());
		sampleCoverage = new SampleCoverage(GLContext.getWindow().getWidth(), GLContext.getWindow().getHeight());
		opaqueTransparencyBlending = new OpaqueTransparencyBlending(GLContext.getWindow().getWidth(), GLContext.getWindow().getHeight());
	}
	
	@Override
	public void addedToEngine() {
		
		opaqueSceneRenderList = engine.getEntitiesOf(Family.one(OpaqueModelRenderer.class, TestRenderer.class).all(Transform.class, Renderable.class).get());
		transparentSceneRenderList = engine.getEntitiesOf(Family.all(Transform.class, Renderable.class, TransparentModelRenderer.class).get());
		shadowSceneRenderList = engine.getEntitiesOf(Family.all(ModelShadowRenderer.class, Transform.class, Renderable.class).get());
		glFinish();
	}
	
	@Override
	public void removedFromEngine() {
		opaqueSceneRenderList = null;
		transparentSceneRenderList = null;
		shadowSceneRenderList = null;
		glFinish();
		glViewport(0,0,config.width,config.height);
	}
	
	@Override
	public void update(double deltaTime) {
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
			
			glViewport(0,0,config.width,config.height);
			pssmFBO.getParameter().disable();
			pssmFBO.getFbo().unbind();
		}
		
		//===================================//
		//        RENDER OPAQUE OBJECTS      //
		//===================================//
		primarySceneFBO.bind();
		for(Entity e: opaqueSceneRenderList) {
			if(testRendererMapper.hasComponent(e))
				testRendererMapper.getComponent(e).render(e);
			if(opaqueModelRendererMapper.hasComponent(e))
				opaqueModelRendererMapper.getComponent(e).render(e);
		}
		
		primarySceneFBO.unbind();
		
		//TODO: render also specular emissive diffusion attributes for lighting and normal maps
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
		
		//TODO: enhance lighting system to render it properly:
		//Deal with multiple light sources as well as dynamic lights
		//===================================//
		//      RENDER DEFERRED LIGHTING     //
		//===================================//
		deferredLighting.render(primarySceneFBO.getAttachmentTexture(Attachment.COLOR),
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
			opaqueTransparencyBlending.render(deferredLighting.getDeferredSceneTexture(),
					primarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.COLOR),
					secondarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.ALPHA));
		}
		
		//TODO: PPE pass
		/*
		 * PPE pipeline:
		 * 1) pre-postprocessing - FXAA
		 * 2) postprocessing carried out via something, so it can be extended later and not controlled from here
		 * 3) retrieve final ppe result
		 */
		
		fullScreenQuad.setTexture(opaqueTransparencyBlending.getBlendedSceneTexture());
		fullScreenQuad.render();
		
		//TODO: render GUI
		glViewport(0,0,config.width,config.height);
	}
	
	private void createSceneFBOs() {
		primarySceneFBO = new OffScreenFBO(window.getWidth(), window.getHeight(), GLContext.getConfig().multisampleSamplesCount);
		secondarySceneFBO = new TransparencyFBO(window.getWidth(), window.getHeight());
	}

}
