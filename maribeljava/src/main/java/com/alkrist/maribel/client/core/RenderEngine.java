package com.alkrist.maribel.client.core;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glViewport;

import com.alkrist.maribel.client.components.ui.GUI;
import com.alkrist.maribel.client.render.antialiasing.FXAA;
import com.alkrist.maribel.client.render.antialiasing.SampleCoverage;
import com.alkrist.maribel.client.render.deferred.DeferredLighting;
import com.alkrist.maribel.client.render.filter.ssao.SSAO;
import com.alkrist.maribel.client.render.pipeline.DefaultRenderParameter;
import com.alkrist.maribel.client.render.scenegraph.RenderList;
import com.alkrist.maribel.client.render.scenegraph.Scenegraph;
import com.alkrist.maribel.client.render.shadows.CascadeShadowMapsFbo;
import com.alkrist.maribel.client.render.surface.FullScreenQuad;
import com.alkrist.maribel.client.render.target.FBO;
import com.alkrist.maribel.client.render.target.OpaqueForwardFbo;
import com.alkrist.maribel.client.render.target.TransparentForwardFbo;
import com.alkrist.maribel.client.render.target.FBO.Attachment;
import com.alkrist.maribel.client.render.texture.Texture;
import com.alkrist.maribel.client.render.transparency.OpaqueTransparencyBlending;
import com.alkrist.maribel.client.util.GLUtil;

public class RenderEngine {

	private Scenegraph scenegraph;
	private RenderList opaqueSceneRenderList;
	private RenderList transparentSceneRenderList;
	
	private VideoConfig config;
	private Window window;
	
	private FBO primarySceneFBO;
	private FBO secondarySceneFBO;
	private CascadeShadowMapsFbo shadowsFBO;
	
	private DeferredLighting deferredLighting;
	private FullScreenQuad fullScreenQuad;
	private GUI gui;
	
	// multisampling
	private FXAA fxaa;
	private SampleCoverage sampleCoverage;
	
	// filter
	private SSAO ssao;
	
	// blending
	private OpaqueTransparencyBlending opaqueTransparencyBlending;
	
	public RenderEngine() {
		// something that is engine-specific and does not change per scene
	}
	
	public void init() {
		config = Context.getVideoConfig();
		window = Context.getWindow();
		
		int width = window.getWidth();
		int height = window.getHeight();
		
		primarySceneFBO = new OpaqueForwardFbo(width, height, 
				config.multisampleSamplesCount);
		secondarySceneFBO = new TransparentForwardFbo(width, height);
		shadowsFBO = new CascadeShadowMapsFbo();
		
		scenegraph = new Scenegraph();
		opaqueSceneRenderList = new RenderList();
		transparentSceneRenderList = new RenderList();
		
		deferredLighting = new DeferredLighting(width, height);
		fullScreenQuad = new FullScreenQuad(new DefaultRenderParameter());
		
		// multisampling
		fxaa = new FXAA(width, height);
		sampleCoverage = new SampleCoverage(width, height);
		
		// filter
		ssao = new SSAO(width, height);
		
		// blending
		opaqueTransparencyBlending = new OpaqueTransparencyBlending(width, height);
		
		glFinish();
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
		secondarySceneFBO.unbind();
		
		//===================================//
		//       RECORD RENDER OBJECTS       //
		//===================================//
		scenegraph.record(opaqueSceneRenderList);
		
		//===================================//
		//        RENDER SHADOW MAPS         //
		//===================================//
		if(config.isShadowMapsEnabled) {
			shadowsFBO.getFbo().bind();
			shadowsFBO.getParameter().enable();
			glViewport(0,0,config.shadowMapResolution, config.shadowMapResolution);
			opaqueSceneRenderList.getValues().forEach(object -> object.renderShadows());
			glViewport(0, 0, Context.getWindow().getWidth(), Context.getWindow().getHeight());
			shadowsFBO.getParameter().disable();
			shadowsFBO.getFbo().unbind();
		}
		
		//===================================//
		//        RENDER OPAQUE SCENE        //
		//===================================//
		primarySceneFBO.bind();
		opaqueSceneRenderList.getValues().forEach(object -> object.render());
		primarySceneFBO.unbind();
		
		//===================================//
		//      RENDER TRANSPARENT SCENE     //
		//===================================//	
		scenegraph.recordTransparentObjects(transparentSceneRenderList);
		if(transparentSceneRenderList.getObjectList().size() > 0) {
			secondarySceneFBO.bind();
			transparentSceneRenderList.sortBackToFront(Context.getCamera()).forEach(object -> object.render());
			secondarySceneFBO.unbind();
		}
		
		//===================================//
		//        RENDER SSAO BUFFER         //
		//===================================//
		if(config.isSSAOEnabled) {
			ssao.render(primarySceneFBO.getAttachmentTexture(Attachment.POSITION),
					primarySceneFBO.getAttachmentTexture(Attachment.NORMAL));
		}
				
		if(config.isMultisamplingEnabled) {
			sampleCoverage.render(primarySceneFBO.getAttachmentTexture(Attachment.POSITION),
					primarySceneFBO.getAttachmentTexture(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM));
		}
		
		//===================================//
		//      RENDER DEFERRED LIGHTING     //
		//===================================//
		deferredLighting.render(primarySceneFBO.getAttachmentTexture(Attachment.COLOR), 
				primarySceneFBO.getAttachmentTexture(Attachment.POSITION), 
				primarySceneFBO.getAttachmentTexture(Attachment.NORMAL), 
				primarySceneFBO.getAttachmentTexture(Attachment.SPECULAR_EMISSION_DIFFUSE_SSAO_BLOOM),
				sampleCoverage.getSampleCoverageMask());
		
		//===================================//
		//   BLEND OPAQUE/TRANSPARENT SCENE  //
		//===================================//
		if(transparentSceneRenderList.getObjectList().size() > 0) {
			
			opaqueTransparencyBlending.render(deferredLighting.getDeferredSceneTexture(),
					primarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.COLOR),
					secondarySceneFBO.getAttachmentTexture(Attachment.DEPTH),
					secondarySceneFBO.getAttachmentTexture(Attachment.ALPHA));
		}
		
		//===================================//
		//       POST-PROCESSING SCENE       //
		//===================================//
		Texture currentScene = (transparentSceneRenderList.getObjectList().size() > 0) 
				? opaqueTransparencyBlending.getBlendedSceneTexture() 
						: deferredLighting.getDeferredSceneTexture();
		
		// FXAA
		if(config.isFXAAEnabled && Context.getCamera().isMoved()) {
			fxaa.render(currentScene);
			currentScene = fxaa.getFXAASceneTexture();
		}
		
		fullScreenQuad.setTexture(currentScene);
		fullScreenQuad.render();
		
		
		//===================================//
		//            RENDER GUI             //
		//===================================//
		if(gui != null) {
			gui.render();
		}
		
		glViewport(0, 0, Context.getWindow().getWidth(), Context.getWindow().getHeight());
	}
	
	public Scenegraph getScenegraph() {
		return scenegraph;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
}
