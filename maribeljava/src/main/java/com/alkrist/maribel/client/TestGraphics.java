package com.alkrist.maribel.client;

import org.joml.Vector3f;

import com.alkrist.maribel.client.graphics.shader.shaders.TestRenderer;
import com.alkrist.maribel.client.graphics.shader.shaders.TestShader;
import com.alkrist.maribel.client.graphics.shader.shaders.TestTransparencyShader;
import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.ModelShadowRenderer;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.components.TransparentModelRenderer;
import com.alkrist.maribel.graphics.components.light.DirectionalLight;
import com.alkrist.maribel.graphics.components.light.PointLight;
import com.alkrist.maribel.graphics.components.light.SpotLight;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.model.GenericModelShader;
import com.alkrist.maribel.graphics.model.GenericModelShadowShader;
import com.alkrist.maribel.graphics.model.Model;
import com.alkrist.maribel.graphics.model.ModelCompositeLoader;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlendingSrcAlpha;
import com.alkrist.maribel.graphics.render.parameter.CCW;
import com.alkrist.maribel.graphics.render.parameter.ShadowRenderParameter;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.graphics.systems.RenderSystem;
import com.alkrist.maribel.utils.Logging;

/**
 * REMOVE THIS FUCKING CLASS LATER!!!
 * 
 * It's for testing graphics without using the whole engine.
 * @author Mikhail
 *
 */
public class TestGraphics {

	public static DirectionalLight sun = new DirectionalLight(new Vector3f(-10000, 10000, 0), new Vector3f(1,1,1), 0.7f);
	public static PointLight light1 = new PointLight(new Vector3f(-2, 10, -40), new Vector3f(0,0,1), 0.5f, 1, 0.01f, 0.002f);
	public static SpotLight light2 = new SpotLight(new Vector3f(0, 15, 0), new Vector3f(1,0,1),
			2f, 1, 0.01f, 0.002f, new Vector3f(-2, 10, -40), 45);
	
	public static void main(String[] args) {
		Logging.initLogger();
		Settings.load();	
		
		GLContext.create("test", "system\\icon32");
		GLWindow window = GLContext.getWindow();

		Model dog = ModelCompositeLoader.loadFromJson("dog");
		Model sampleScene = ModelCompositeLoader.loadFromJson("sample_plane");
		Model glass = ModelCompositeLoader.loadFromJson("transparent");
		
		Transform dogTransform = new Transform(new Vector3f(0, -4,-60), new Vector3f(0,0,0), 2);
		Transform sampleSceneTransform = new Transform(new Vector3f(0,-5,-60), new Vector3f(0,0,0), 2);
		Transform glassTransform = new Transform(new Vector3f(0, 0,-40), new Vector3f(0,0,0), 2);
		
		Renderable dogRenderable = new Renderable(dog.getChild("dog").getMesh(), dog.getChild("dog").getMaterial());
		Renderable sampleSceneRenderable = new Renderable(sampleScene.getChild("plane").getMesh(), sampleScene.getChild("plane").getMaterial());
		Renderable glassRenderable = new Renderable(glass.getChild("plane").getMesh(), glass.getChild("plane").getMaterial());
		
		TestShader shader = TestShader.getInstance();
		GenericModelShader gms = GenericModelShader.getInstance();
		GenericModelShadowShader gmss = GenericModelShadowShader.getInstance();
		TestTransparencyShader ts = TestTransparencyShader.getInstance();
		
		TestRenderer renderer = new TestRenderer(new CCW(), shader);
		OpaqueModelRenderer omRenderer = new OpaqueModelRenderer(new CCW(), gms);
		ModelShadowRenderer shadowRenderer = new ModelShadowRenderer(new ShadowRenderParameter(), gmss);
		TransparentModelRenderer transparentRenderer = new TransparentModelRenderer(new CCW(), ts);
		
		Engine engine = new Engine();
		engine.addSystem(new RenderSystem());
		
		Entity e1 = engine.createEntity();
		e1.addComponent(dogRenderable);
		e1.addComponent(dogTransform);
		e1.addComponent(omRenderer);
		e1.addComponent(shadowRenderer);
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
		engine.addEntity(e3);
		
		
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
