package com.alkrist.maribel.client;

import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.alkrist.maribel.client.components.skybox.Skybox;
import com.alkrist.maribel.client.components.ui.GUI;
import com.alkrist.maribel.client.components.ui.UIColorPanel;
import com.alkrist.maribel.client.components.ui.UIScreen;
import com.alkrist.maribel.client.components.ui.UITextPanel;
import com.alkrist.maribel.client.components.ui.UITexturePanel;
import com.alkrist.maribel.client.components.ui.font.FontType;
import com.alkrist.maribel.client.core.Camera;
import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.core.Input;
import com.alkrist.maribel.client.memory.MeshVBO;
import com.alkrist.maribel.client.model.Model;
import com.alkrist.maribel.client.scenegraph.Renderable;
import com.alkrist.maribel.client.scenegraph.Renderer;
import com.alkrist.maribel.client.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.texture.Texture2D;
import com.alkrist.maribel.client.util.AssimpModelLoader;
import com.alkrist.maribel.graphics.components.TestModelShader;
import com.alkrist.maribel.graphics.render.parameter.DefaultRenderParameter;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.Logging;

public class MaribelTest {

	public static void main(String[] args) {
		
		// pre-init
		preInit();
		
		// init - load models and such
		Context.getRenderEngine().getScenegraph().addObject(new Skybox());
		List<Model> models = AssimpModelLoader.loadModel("assets/models","rock01.obj");	
		Model model = models.get(0);
		
		MeshVBO meshBuffer = new MeshVBO();
		meshBuffer.addData(model.getMesh());
		Renderer renderInfo = 
				new Renderer(TestModelShader.getInstance(), new DefaultRenderParameter(), meshBuffer);
		Renderable object = new Renderable();
		object.addComponent("main", renderInfo);
		object.addComponent("material", model.getMaterial());
		object.getWorldTransform().setTranslation(new Vector3f(0, -1, -5));
		object.getWorldTransform().setRotation(20, 50, 10);
		Context.getRenderEngine().getScenegraph().addObject(object);
		
		GUI gui = new GUI();
		gui.init();
		
		FontType harry = FontType.loadFont(FileUtils.getResourceLocation("fonts/test.fnt"), FileUtils.getResourceLocation("textures/fonts/test.png"), 24);
		UITextPanel textPanel = new UITextPanel("biba", harry, new Vector4f(1), 100, 100, 1);
		
		UIScreen testUIScreen = new UIScreen();
		testUIScreen.getElements().add(new UIColorPanel(new Vector4f(1, 1, 1, 0.5f), 200, 100, 200, 200, gui.getPanelMeshBuffer()));
		
		Texture2D sampleGUITexture = new Texture2D(FileUtils.getResourceLocation("textures/sample_gui.jpg"), SamplerFilter.Bilinear);
		testUIScreen.getElements().add(new UITexturePanel(sampleGUITexture, 200, 300, 375, 375, gui.getPanelMeshBuffer()));
		
		testUIScreen.getElements().add(textPanel);
		
		gui.getScreens().add(testUIScreen);
		//Context.getRenderEngine().setGUI(gui);
		
		Context.getCoreEngine().start();
		
		// terminate cleanup
		System.out.println("clean up phase");
	}
	
	private static void preInit() {
	    Logging.initLogger();
	    Context.create();
	}
	
	public static void cameraTestUpdater(double deltaTime) {
		Input input = Context.getInput();
		Camera camera = Context.getCamera();
		
		// Mouse rotation (only when mouse is locked)
	    if (input.isMouseLocked()) {
	        Vector2f mouseDelta = input.getMouseDelta();
	        
	        // Apply yaw (horizontal movement)
	        camera.rotateLocal(0, -mouseDelta.x, 0);
	        
	        // Apply pitch (vertical movement)
	        camera.rotateLocal(mouseDelta.y, 0, 0);
	    }

	    // Keyboard movement (existing code)
	    if (input.isKeyHolding(GLFW.GLFW_KEY_E)) {
	        camera.rotateLocal(0, 0, -0.001f);
	    }
	    
		if(input.isKeyHolding(GLFW.GLFW_KEY_E)) {
			camera.rotateLocal(0, 0, -0.001f);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_Q)) {
			camera.rotateLocal(0, 0, 0.001f);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_W)) {
			camera.moveLocal(0, 0, -0.001f);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_S)) {
			camera.moveLocal(0, 0, 0.001f);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_A)) {
			camera.moveLocal(-0.001f, 0, 0);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_D)) {
			camera.moveLocal(0.001f, 0, 0);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_SPACE)) {
			camera.moveLocal(0, 0.001f, 0);
		}
		
		if(input.isKeyHolding(GLFW.GLFW_KEY_C)) {
			camera.moveLocal(0, -0.001f, 0);
		}
	}
}
