package com.alkrist.maribel.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

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
import com.alkrist.maribel.graphics.platform.InputHandler;
import com.alkrist.maribel.graphics.platform.RenderEngine;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.render.parameter.ShadowRenderParameter;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
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
	
	public static DirectionLight dirLight = new DirectionLight(new Vector3f(0.0f,10000.0f, 10000.0f), new Vector3f(1, 0.8f, 0.4f), 0.05f);
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
		
		Model demo = ModelCompositeLoader.loadFromJson("models/demo/demo.json");
		Transform demoTransform = new Transform(new Vector3f(0, -2, 0), new Vector3f(0), 1);
		
		Model bunny = ModelCompositeLoader.loadFromJson("models/bunny/bunny.json");
		
		
		Transform dogTransform = new Transform(new Vector3f(0, -4,-60), new Vector3f(0,0,0), 2);
		Transform sampleSceneTransform = new Transform(new Vector3f(0,-5,-60), new Vector3f(0,1,0), 2);
		Transform sampleSceneTransform2 = new Transform(new Vector3f(0,-30,-60), new Vector3f(0,1,0), 3);
		Transform glassTransform = new Transform(new Vector3f(0, 0,-40), new Vector3f(0,0,0), 2);
		Transform dragonTransform = new Transform(new Vector3f(0, -3,-10), new Vector3f(0,0,0), 1.5f);
		
		Transform bunnyTransform = new Transform(new Vector3f(0, -10,-10), new Vector3f(0,0,0), 0.25f);
		
		Renderable dogRenderable = new Renderable(dog.getChild("dog").getMesh(), dog.getChild("dog").getMaterial());
		Renderable sampleSceneRenderable = new Renderable(sampleScene.getChild("plane").getMesh(), sampleScene.getChild("plane").getMaterial());
		Renderable glassRenderable = new Renderable(glass.getChild("plane").getMesh(), glass.getChild("plane").getMaterial());
		Renderable dragonRenderable = new Renderable(dragon.getChild("body").getMesh(), dragon.getChild("body").getMaterial());
		
		Renderable demoWallRenderable = new Renderable(demo.getChild("wall").getMesh(), demo.getChild("wall").getMaterial());
		Renderable demoFloorRenderable = new Renderable(demo.getChild("floor").getMesh(), demo.getChild("floor").getMaterial());
		Renderable demoCeilingRenderable = new Renderable(demo.getChild("ceiling").getMesh(), demo.getChild("ceiling").getMaterial());
		Renderable demoBarrierRenderable = new Renderable(demo.getChild("barrier").getMesh(), demo.getChild("barrier").getMaterial());
		
		Renderable bunnyRenderable = new Renderable(bunny.getChild("bunny").getMesh(), bunny.getChild("bunny").getMaterial());
		
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
		//engine.addSystem(new RenderSystem());
		RenderEngine renderEngine = new RenderEngine(engine);
		
		
		/*Entity e1 = engine.createEntity();
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
		
		engine.addEntity(e2);*/
		
		/*Entity e3 = engine.createEntity();
		e3.addComponent(glassTransform);
		e3.addComponent(glassRenderable);
		e3.addComponent(transparentRenderer);
		e3.addComponent(shadowRenderer);
		e3.addComponent(light4);
		
		engine.addEntity(e3);*/
		
		/*Entity e4 = engine.createEntity();
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
		
		/*Entity canvasEntity = engine.createEntity();
		canvasEntity.addComponent(wCanvas);
		engine.addEntity(canvasEntity);*/

		/*Entity demoEntity1 = engine.createEntity();
		demoEntity1.addComponent(demoTransform);
		demoEntity1.addComponent(demoWallRenderable);
		demoEntity1.addComponent(omRenderer);
		//demoEntity1.addComponent(shadowRenderer);
		
		engine.addEntity(demoEntity1);
		
		Entity demoEntity2 = engine.createEntity();
		demoEntity2.addComponent(demoTransform);
		demoEntity2.addComponent(demoFloorRenderable);
		demoEntity2.addComponent(omRenderer);
		//demoEntity2.addComponent(shadowRenderer);
		
		engine.addEntity(demoEntity2);
		
		Entity demoEntity3 = engine.createEntity();
		demoEntity3.addComponent(demoTransform);
		demoEntity3.addComponent(demoCeilingRenderable);
		demoEntity3.addComponent(omRenderer);
		//demoEntity3.addComponent(shadowRenderer);
		
		engine.addEntity(demoEntity3);
		
		Entity demoEntity4 = engine.createEntity();
		demoEntity4.addComponent(demoTransform);
		demoEntity4.addComponent(demoBarrierRenderable);
		demoEntity4.addComponent(omRenderer);
		//demoEntity4.addComponent(shadowRenderer);
		
		engine.addEntity(demoEntity4);*/
		
		Entity e6 = engine.createEntity();
		e6.addComponent(bunnyRenderable);
		e6.addComponent(bunnyTransform);
		e6.addComponent(omRenderer);
		//e6.addComponent(shadowRenderer);
		engine.addEntity(e6);
		
		// test lights
		List<PointLight> pointLights = new ArrayList<PointLight>();
		Vector3f lampColor = new Vector3f(1);
		//pointLights.add(new PointLight(new Vector3f(1.13805f, 0f, 18.3666f), lampColor, 4f, 1f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(2.28304f, 1f, 11.2137f), lampColor, 3f, 1f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(2.86169f, 0f, 5.44496f), lampColor, 4f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(3.99158f, 1f, 0.859641f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(3.99158f, 1f, -4.87329f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(0.58399f, 1f, -2.56364f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(-4.00033f, 1f, -3.71998f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(-2.87043f, 1f, 3.15177f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(-9.79827f, 1f, 1.99022f), lampColor, 3f, 0.2f, 2.8f));
		//pointLights.add(new PointLight(new Vector3f(-15.615f, 1f, 3.17519f), lampColor, 3f, 0.2f, 2.8f));
		
		/*for(PointLight light: pointLights) {
			Entity e = engine.createEntity();
			e.addComponent(light);
			engine.addEntity(e);
		}*/
		
		//DirectionLight dirLight = new DirectionLight(new Vector3f(10000.0f,10000.0f, 1000.0f), new Vector3f(1, 0.8f, 0.4f), 0.5f);
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
        
		// TODO: test this game loop
		int targetTPS = 20;
		int targetFPS = 60;
		long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetTPS;
        float timeR = targetFPS > 0 ? 1000.0f / targetFPS : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        
        update(0);
        /*while(!window.isCloseRequested()) {
        	window.pollEvents();
        	
        	long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;
            
            if (targetFPS <= 0 || deltaFps >= 1) {
            	GLContext.getInput().update();
            }
            
            if (deltaUpdate >= 1) {
            	long diffTimeMillis = now - updateTime;
            	update(diffTimeMillis);
            	engine.update(0);
            	updateTime = now;
            	deltaUpdate--;
            }
            
            if (targetFPS <= 0 || deltaFps >= 1) {
                renderEngine.render();
                deltaFps--;
                window.updateWindow();
            }
            
            initialTime = now;
        }window.destroyWindow();*/
        
        
		while(!window.isCloseRequested()) {
			window.pollEvents();
			// Update loop now is here
			GLContext.getInput().update();
			bunnyTransform.rotate(0, 0.1f, 0);
			text.setTextString("VBOs: "+ResourceLoader.getVBOsCount());
			text.resize();
			//End of update loop
			
			engine.update(0);
			renderEngine.render();
			update(window.deltaTime());
			window.updateWindow();
			
		}window.destroyWindow();
		
		GLContext.finish();
		Settings.save();
	}
	
	public static void update(double dt) {
		InputHandler input = GLContext.getInput();
		float dx = 0; float dy = 0; float dz = 0;
		float pitch = 0; float yaw = 0; float roll = 0;
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_Q)) {
			roll = 10f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_E)) {
			roll = -10f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_Z)) {
			yaw = 10f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_X)) {
			yaw = -10f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_W)) {
			dz = 1f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_S)) {
			dz = -1f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_A)) {
			dx = -1f;
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_D)) {
			dx = 1f;
		}
		
		System.out.println(Math.floor(1/dt));
		GLContext.getMainCamera().update(dx, dy, dz, pitch, yaw, roll, dt);
		PSSMCamera.update(dirLight);
	}
}
