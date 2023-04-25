package com.alkrist.maribel.graphics.systems;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glViewport;

import com.alkrist.maribel.client.graphics.shader.shaders.TestRenderer;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.OpaqueModelShadowRenderer;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.context.GraphicsConfig;
import com.alkrist.maribel.graphics.platform.GLUtil;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.shadow.ParallelSplitShadowMapsFBO;
import com.alkrist.maribel.graphics.surface.FullScreenQuad;
import com.alkrist.maribel.graphics.target.FBO;
import com.alkrist.maribel.graphics.target.FBO.Attachment;
import com.alkrist.maribel.graphics.target.OffScreenFBO;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class RenderSystem extends SystemBase{

	private GLWindow window;
	private GraphicsConfig config;
	
	private FullScreenQuad fullScreenQuad;
	private FBO primarySceneFBO;
	private ParallelSplitShadowMapsFBO pssmFBO;
	
	private static final ComponentMapper<TestRenderer> testRendererMapper = ComponentMapper.getFor(TestRenderer.class);
	private static final ComponentMapper<OpaqueModelRenderer> opaqueModelRendererMapper = ComponentMapper.getFor(OpaqueModelRenderer.class);
	private static final ComponentMapper<OpaqueModelShadowRenderer> opaqueModelShadowMapper = ComponentMapper.getFor(OpaqueModelShadowRenderer.class);
	
	private ImmutableArrayList<Entity> opaqueSceneRenderList;
	private ImmutableArrayList<Entity> shadowSceneRenderList;
	
	public RenderSystem() {
		super();
		this.window = GLContext.getWindow();
		this.config = GLContext.getConfig();
		this.fullScreenQuad = new FullScreenQuad();
		createPrimarySceneFBO();
		pssmFBO = new ParallelSplitShadowMapsFBO();
		PSSMCamera.init();
	}
	
	@Override
	public void addedToEngine() {
		
		opaqueSceneRenderList = engine.getEntitiesOf(Family.one(OpaqueModelRenderer.class, TestRenderer.class).all(Transform.class, Renderable.class).get());
		shadowSceneRenderList = engine.getEntitiesOf(Family.all(OpaqueModelShadowRenderer.class, Transform.class, Renderable.class).get());
		glFinish();
	}
	
	@Override
	public void removedFromEngine() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(double deltaTime) {
		//===================================//
		//        CLEAR RENDER BUFFER        //
		//===================================//
		GLUtil.clearScreen();
		primarySceneFBO.bind();
		GLUtil.clearScreen();
		primarySceneFBO.unbind();
		pssmFBO.getFbo().bind();
		GLUtil.clearScreen();
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
		
		fullScreenQuad.setTexture(primarySceneFBO.getAttachmentTexture(Attachment.COLOR));
		fullScreenQuad.render();
		
	}
	
	private void createPrimarySceneFBO() {
		primarySceneFBO = new OffScreenFBO(window.getWidth(), window.getHeight(), 1);
	}

}
