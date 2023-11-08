package com.alkrist.maribel.client;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.client.graphics.shader.shaders.TestRenderer;
import com.alkrist.maribel.client.graphics.shader.shaders.TestShader;
import com.alkrist.maribel.client.graphics.shader.shaders.TestTransparencyShader;
import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.ModelShadowRenderer;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.PostProcessingVolume;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.components.TransparentModelRenderer;
import com.alkrist.maribel.graphics.components.light.DirectionalLight;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.components.light.SpotLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.filter.contrast.ContrastProperty;
import com.alkrist.maribel.graphics.model.GenericModelShader;
import com.alkrist.maribel.graphics.model.GenericModelShadowShader;
import com.alkrist.maribel.graphics.model.Model;
import com.alkrist.maribel.graphics.model.ModelCompositeLoader;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.render.parameter.ShadowRenderParameter;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.systems.RenderSystem;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;
import com.alkrist.maribel.graphics.ui.UIColorPanel;
import com.alkrist.maribel.graphics.ui.UIElement;
import com.alkrist.maribel.graphics.ui.UITexturePanel;
import com.alkrist.maribel.graphics.ui.WindowCanvas;
import com.alkrist.maribel.graphics.ui.constraints.AspectConstraint;
import com.alkrist.maribel.graphics.ui.constraints.PixelConstraint;
import com.alkrist.maribel.graphics.ui.constraints.RelativeConstraint;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;
import com.alkrist.maribel.utils.Logging;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing graphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {

	public static DirectionalLight sun = new DirectionalLight(new Vector3f(1, 10000, 1000), new Vector3f(1,1,1), 0.7f);
	public static PointLight light1 = new PointLight(new Vector3f(-2, 10, -40), new Vector3f(0,0,1), 0.5f, 1, 0.01f, 0.002f);
	public static SpotLight light2 = new SpotLight(new Vector3f(0, 15, 0), new Vector3f(1,0,1),
			2f, 1, 0.01f, 0.002f, new Vector3f(-2, 10, -40), 45);
	
	//after setup commit
	public static void main(String[] args) {
		Logging.initLogger();
		Settings.load();	
		
		GLContext.create("test", "system\\icon32");
		GLWindow window = GLContext.getWindow();
		
		Model dog = ModelCompositeLoader.loadFromJson("dog");
		Model sampleScene = ModelCompositeLoader.loadFromJson("sample_plane");
		Model glass = ModelCompositeLoader.loadFromJson("transparent");
		Model dragon = ModelCompositeLoader.loadFromJson("dragon");
		
		Transform dogTransform = new Transform(new Vector3f(0, -4,-60), new Vector3f(0,0,0), 2);
		Transform sampleSceneTransform = new Transform(new Vector3f(0,-5,-60), new Vector3f(0,1,0), 2);
		Transform sampleSceneTransform2 = new Transform(new Vector3f(0,-30,-60), new Vector3f(0,1,0), 3);
		Transform glassTransform = new Transform(new Vector3f(0, 0,-40), new Vector3f(0,0,0), 2);
		Transform dragonTransform = new Transform(new Vector3f(0, -3,-10), new Vector3f(0,0,0), 1.5f);
		
		Renderable dogRenderable = new Renderable(dog.getChild("dog").getMesh(), dog.getChild("dog").getMaterial());
		Renderable sampleSceneRenderable = new Renderable(sampleScene.getChild("plane").getMesh(), sampleScene.getChild("plane").getMaterial());
		Renderable glassRenderable = new Renderable(glass.getChild("plane").getMesh(), glass.getChild("plane").getMaterial());
		Renderable dragonRenderable = new Renderable(dragon.getChild("body").getMesh(), dragon.getChild("body").getMaterial());
		
		TestShader shader = TestShader.getInstance();
		GenericModelShader gms = GenericModelShader.getInstance();
		GenericModelShadowShader gmss = GenericModelShadowShader.getInstance();
		TestTransparencyShader ts = TestTransparencyShader.getInstance();
		
		TestRenderer renderer = new TestRenderer(new CCW(), shader);
		OpaqueModelRenderer omRenderer = new OpaqueModelRenderer(new CCW(), gms);
		ModelShadowRenderer shadowRenderer = new ModelShadowRenderer(new ShadowRenderParameter(), gmss);
		TransparentModelRenderer transparentRenderer = new TransparentModelRenderer(new CCW(), ts);
		
		
		WindowCanvas wCanvas = new WindowCanvas();
		//UIElement colorPanel = new UIColorPanel(new Vector2f(0f, 0f), new Vector4f(1, 1, 1, 1f), new Vector2f(0.5f, 0.5f));
		UIConstraints colorPanelConstraints = new UIConstraints()
		.setWidth(new RelativeConstraint(0.5f))
		.setHeight(new AspectConstraint(0.5f))
		.setX(new RelativeConstraint(0.25f))
		.setY(new RelativeConstraint(0.5f));
		
		UIElement colorPanel = new UIColorPanel(colorPanelConstraints, new Vector4f(0.4f,0.4f, 0.5f, 1.0f));
		
		UIConstraints texturePanelConstraints = new UIConstraints()
				.setWidth(new RelativeConstraint(0.5f))
				.setHeight(new AspectConstraint(1))
				.setX(UIConstraints.MarginHorizontal.RIGHT,new PixelConstraint(20))
				.setY(UIConstraints.MarginVertical.TOP, new PixelConstraint(20));
		
		UIElement texturePanel = new UITexturePanel(texturePanelConstraints, 
				new Texture2D("overlay\\overlay_gasmask_3.png", SamplerFilter.Nearest, TextureWrapMode.ClampToEdge));
		
		wCanvas.addUIElement(texturePanel);
		//wCanvas.addUIElement(colorPanel);
		//texturePanel.addChild(colorPanel);
		//colorPanel.addChild(texturePanel);
		
		//Test post processing pipeline
		ContrastProperty contrastProp = new ContrastProperty(new Vector3f(1f), new Vector3f(1f));
		
		PostProcessingVolume ppeVolume1 = new PostProcessingVolume.PPEComponentBuilder(0.1f).addEffectContrast(contrastProp).get();
		
		Engine engine = new Engine();
		engine.addSystem(new RenderSystem());
		
		Entity e1 = engine.createEntity();
		e1.addComponent(dogRenderable);
		e1.addComponent(dogTransform);
		e1.addComponent(omRenderer);
		e1.addComponent(shadowRenderer);
		e1.addComponent(ppeVolume1);
		engine.addEntity(e1);
		
		Entity e2 = engine.createEntity();
		e2.addComponent(sampleSceneRenderable);
		e2.addComponent(sampleSceneTransform);
		e2.addComponent(omRenderer);
		e2.addComponent(shadowRenderer);
		
		engine.addEntity(e2);
		
		Entity e3 = engine.createEntity();
		e3.addComponent(glassTransform);
		e3.addComponent(glassRenderable);
		e3.addComponent(transparentRenderer);
		e3.addComponent(shadowRenderer);
		
		engine.addEntity(e3);
		
		Entity e4 = engine.createEntity();
		e4.addComponent(dragonTransform);
		e4.addComponent(dragonRenderable);
		e4.addComponent(transparentRenderer);
		e4.addComponent(shadowRenderer);
		
		engine.addEntity(e4);
		
		Entity e5 = engine.createEntity();
		e5.addComponent(sampleSceneRenderable);
		e5.addComponent(sampleSceneTransform2);
		e5.addComponent(omRenderer);
		e5.addComponent(shadowRenderer);
		
		engine.addEntity(e5);
		
		Entity canvasEntity = engine.createEntity();
		canvasEntity.addComponent(wCanvas);
		engine.addEntity(canvasEntity);
		
		while(!window.isCloseRequested()) {
			
				
			GLContext.getInput().update();
			GLContext.getMainCamera().update();
			PSSMCamera.update(sun);
				
			dogTransform.rotate(0, 0.1f, 0);
			engine.update(0);
			window.updateWindow();
		}window.destroyWindow();
		
		GLContext.finish();
		Settings.save();
	}
}
