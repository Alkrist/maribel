package com.alkrist.maribel.client;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import com.alkrist.maribel.graphics.components.light.DirectionLight;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.filter.contrast.ContrastProperty;
import com.alkrist.maribel.graphics.model.GenericModelShader;
import com.alkrist.maribel.graphics.model.GenericModelShadowShader;
import com.alkrist.maribel.graphics.model.Model;
import com.alkrist.maribel.graphics.model.ModelCompositeLoader;
import com.alkrist.maribel.graphics.model.ResourceLoader;
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
import com.alkrist.maribel.graphics.ui.constraints.CenterConstraint;
import com.alkrist.maribel.graphics.ui.constraints.RelativeConstraint;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;
import com.alkrist.maribel.graphics.ui.fonts.FontType;
import com.alkrist.maribel.graphics.ui.fonts.UIText;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.Logging;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing graphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {
	
	//after setup commit
	public static void main(String[] args) {
		Logging.initLogger();
		Settings.load();	
		
		GLContext.create("test", "system/icon32.png");
		GLWindow window = GLContext.getWindow();
		
		Model dog = ModelCompositeLoader.loadFromJson("models/dog.json");
		Model sampleScene = ModelCompositeLoader.loadFromJson("models/sample_plane.json");
		Model glass = ModelCompositeLoader.loadFromJson("models/transparent.json");
		Model dragon = ModelCompositeLoader.loadFromJson("models/dragon.json");
		
		//Model demo = ModelCompositeLoader.loadFromJson("demo\\demo");
		
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
		.setWidth(new RelativeConstraint(0.25f))
		.setHeight(new AspectConstraint(0.5f))
		.setX(new CenterConstraint())
		.setY(new RelativeConstraint(0.3f));
		
		float borderRadius = 0.5f;
		float borderThickness = 0.2f;
		Vector3f borderColor = new Vector3f(1,0,0);
		
		UIElement colorPanel = new UIColorPanel(colorPanelConstraints, 
				new Vector4f(0.4f,0.4f, 0.5f, 0.5f), 
				borderRadius,
				borderThickness, 
				borderColor);
		
		UIConstraints texturePanelConstraints = new UIConstraints()
				.setWidth(new RelativeConstraint(0.2f))
				.setHeight(new AspectConstraint(0.5f))
				.setX(new CenterConstraint())
				.setY(new RelativeConstraint(0.4f));
		
		UIElement texturePanel = new UITexturePanel(texturePanelConstraints, 
				new Texture2D("textures/overlay/overlay_gasmask_3.png", SamplerFilter.Nearest, TextureWrapMode.ClampToEdge));
		
		wCanvas.addUIElement(colorPanel);
		//wCanvas.addUIElement(colorPanel);
		//texturePanel.addChild(colorPanel);
		colorPanel.addChild(texturePanel);
		
		//times_new_roman_extended
		FontType font = new FontType(new Texture2D("textures/fonts/harry.png",SamplerFilter.Nearest, TextureWrapMode.ClampToEdge), new File(FileUtils.getResourceLocation("fonts/harry.fnt")));
		FontType candara = new FontType(new Texture2D("textures/fonts/candara.png",SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge), new File(FileUtils.getResourceLocation("fonts/candara.fnt")));
		
		UIConstraints textConstraints = new UIConstraints()
				.setX(new RelativeConstraint(0.5f))
				.setY(UIConstraints.MarginVertical.TOP, new RelativeConstraint(0.25f))
				.setWidth(new RelativeConstraint(0.5f))
				.setHeight(new RelativeConstraint(0.1f));	

		//UIText text = new UIText(new Vector2f(0.25f,0.25f), "Frames: ", 2, candara, 1f, false);
		UIText text = new UIText(textConstraints, "Biba", candara, false);
		text.setColor(0, 0, 0);
		wCanvas.addUIText(text);
		
		System.out.println("position: "+colorPanel.getConstraits().getPosition().x+" "+colorPanel.getConstraits().getPosition().y);
		System.out.println("scale: "+colorPanel.getConstraits().getScale().x+" "+colorPanel.getConstraits().getScale().y);
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
		
		/*Entity e3 = engine.createEntity();
		e3.addComponent(glassTransform);
		e3.addComponent(glassRenderable);
		e3.addComponent(transparentRenderer);
		e3.addComponent(shadowRenderer);
		e3.addComponent(light4);
		
		engine.addEntity(e3);*/
		
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

		
		// test lights
		List<PointLight> pointLights = new ArrayList<PointLight>();
		pointLights.add(new PointLight(new Vector3f(-10, 5, -60), new Vector3f(1, 0, 0), 15f, 1, 0.5f));
		pointLights.add(new PointLight(new Vector3f(20, 5, -60), new Vector3f(0, 1, 0), 15f, 1, 0.5f));
		pointLights.add(new PointLight(new Vector3f(-30, 5, -60), new Vector3f(0, 0, 1), 15f, 1, 0.5f));
		
		pointLights.add(new PointLight(new Vector3f(22, 5, -50), new Vector3f(0, 0.55f, 0.65f), 25f, 0.5f, 0.6f));
		pointLights.add(new PointLight(new Vector3f(34, 5, -80), new Vector3f(1, 1, 1), 15f, 1, 0.5f));
		pointLights.add(new PointLight(new Vector3f(-45, 5, -20), new Vector3f(0.4f, 0, 0.42f), 25f, 1, 0.2f));
		pointLights.add(new PointLight(new Vector3f(-50, 5, 0), new Vector3f(0.435f, 0, 1), 25f, 1, 0.7f));
		pointLights.add(new PointLight(new Vector3f(50, 5, 10), new Vector3f(0, 0.53f, 1), 35f, 0.7f, 0.3f));
		pointLights.add(new PointLight(new Vector3f(47, 5, -55), new Vector3f(0.71f, 0, 0.666f), 30f, 1, 0.9f));
		pointLights.add(new PointLight(new Vector3f(-5, 5, -35), new Vector3f(0.44f, 0, 1), 20f, 0.3f, 1));
		pointLights.add(new PointLight(new Vector3f(70, 7, -75), new Vector3f(0.76f, 0, 0.23f), 25f, 0.5f, 0.5f));
		
		for(PointLight light: pointLights) {
			Entity e = engine.createEntity();
			e.addComponent(light);
			engine.addEntity(e);
		}
		
		DirectionLight dirLight = new DirectionLight(new Vector3f(10000.0f,10000.0f, 1000.0f), new Vector3f(1, 0.8f, 0.4f), 0.5f);
		Entity sun = engine.createEntity();
		sun.addComponent(dirLight);
		engine.addEntity(sun);
		
		/*String basePath = "src/assets";
        String fullPath = System.getenv("APPDATA") + "\\.maribel\\";

        // Convert string paths to Path objects
        Path basePathObj = Paths.get(basePath);
        Path fullPathObj = Paths.get(fullPath);

        Path modelsPathObj = basePathObj.resolve("models/backrooms");
       
        System.out.println(FileUtils.getResourceLocation("models/backrooms/wall.obj"));*/
        
		while(!window.isCloseRequested()) {
			
			// Update loop now is here

			GLContext.getMainCamera().update();
			PSSMCamera.update(dirLight);
			dogTransform.rotate(0, 0.1f, 0);
			text.setTextString("VBOs: "+ResourceLoader.getVBOsCount());
			text.resize();
			//End of update loop
			
			engine.update(0);
			window.updateWindow();
			
			GLContext.getInput().update();
		}window.destroyWindow();
		
		GLContext.finish();
		Settings.save();
	}
}
