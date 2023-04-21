package com.alkrist.maribel.client;

import org.joml.Vector3f;

import com.alkrist.maribel.client.graphics.shader.shaders.TestRenderer;
import com.alkrist.maribel.client.graphics.shader.shaders.TestShader;
import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.OpaqueModelRenderer;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.model.GenericModelShader;
import com.alkrist.maribel.graphics.model.Model;
import com.alkrist.maribel.graphics.model.ModelCompositeLoader;
import com.alkrist.maribel.graphics.platform.GLWindow;
import com.alkrist.maribel.graphics.render.parameter.CCW;
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

	public static void main(String[] args) {
		Logging.initLogger();
		Settings.load();	
		
		GLContext.create("test", "system\\icon32");
		GLWindow window = GLContext.getWindow();

		Model dog = ModelCompositeLoader.loadFromJson("dog");
		
		Transform dogTransform = new Transform(new Vector3f(0,-10,-60), new Vector3f(0,0,0), 1);
		//Renderable dogRenderable = new Renderable(dog.getChild("dog").getMesh(), dog.getChild("dog").getMaterial());
		Renderable dogRenderable = new Renderable(dog.getChild("dog").getMesh(), dog.getChild("dog").getMaterial());
		TestShader shader = TestShader.getInstance();
		GenericModelShader gms = GenericModelShader.getInstance();
		
		TestRenderer renderer = new TestRenderer(new CCW(), shader);
		OpaqueModelRenderer omRenderer = new OpaqueModelRenderer(new CCW(), gms);
		
		Engine engine = new Engine();
		engine.addSystem(new RenderSystem());
		
		Entity e1 = engine.createEntity();
		e1.addComponent(dogRenderable);
		e1.addComponent(dogTransform);
		e1.addComponent(omRenderer);
		engine.addEntity(e1);
		
		while(!window.isCloseRequested()) {
			
			GLContext.getInput().update();
			GLContext.getMainCamera().update();
			
			dogTransform.rotate(0, 0.1f, 0);
			engine.update(0);
			
			window.updateWindow();
		}window.destroyWindow();
		
		GLContext.finish();
		Settings.save();
	}
}
