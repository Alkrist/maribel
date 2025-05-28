package com.alkrist.maribel.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.client.components.skybox.Skybox;
import com.alkrist.maribel.client.components.ui.GUI;
import com.alkrist.maribel.client.components.ui.UIColorPanel;
import com.alkrist.maribel.client.components.ui.UIScreen;
import com.alkrist.maribel.client.components.ui.UITextPanel;
import com.alkrist.maribel.client.components.ui.font.FontType;
import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.memory.MeshVBO;
import com.alkrist.maribel.client.model.Model;
import com.alkrist.maribel.client.scenegraph.Renderable;
import com.alkrist.maribel.client.scenegraph.Renderer;
import com.alkrist.maribel.client.sound.SoundBuffer;
import com.alkrist.maribel.client.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.client.texture.Texture2D;
import com.alkrist.maribel.client.util.AssimpModelLoader;
import com.alkrist.maribel.graphics.components.TestModelShader;
import com.alkrist.maribel.graphics.render.parameter.DefaultRenderParameter;
import com.alkrist.maribel.utils.FileUtils;

public class TestSceneHandler {

	private List<Model> rockModels = new ArrayList<>();
	private List<Renderable> rockRenderables = new ArrayList<>();
	private GUI gui;
	
	private SoundBuffer bibaSound;
	
	private FontType harryFnt;
	
	private TestCameraSystem cameraSys;
	
	public void register() {
		registerInitializationTasks();
		registerPostInitializationTasks();
	}
	
	private void registerInitializationTasks() {
		Context.getMasterLoader().addInitializationTask("Loading fonts", this::loadFonts);
        Context.getMasterLoader().addInitializationTask("Loading rock models", this::loadModels);
        Context.getMasterLoader().addInitializationTask("Loading sounds", this::loadSoundBuffers);
    }
	
	private void registerPostInitializationTasks() {
		Context.getMasterLoader().addPostInitializationTask("Creating scene", this::createRenderables);
		Context.getMasterLoader().addPostInitializationTask("Creating guis", this::createGuis);
		Context.getMasterLoader().addPostInitializationTask("Creating systems", this::registerSystems);
	}
	
	private void loadFonts() {
		harryFnt = new FontType(new Texture2D(FileUtils.getResourceLocation("textures/fonts/harry.png"),SamplerFilter.Nearest, TextureWrapMode.ClampToEdge), new File(FileUtils.getResourceLocation("fonts/harry.fnt")));
	}
	
	private void loadModels() {
        // Load your rock models
        List<Model> models = AssimpModelLoader.loadModel("assets/models", "rock01.obj");
        rockModels.addAll(models);
    }
	
	private void loadSoundBuffers() {
		bibaSound = new SoundBuffer(FileUtils.getResourceLocation("sounds/vomit.ogg"));
	}
	
	private void createGuis() {
		gui = new GUI();
		gui.init();
		
		UITextPanel textPanel = new UITextPanel("biba", harryFnt, new Vector4f(1), 100, 100, 100, 50);
		UIScreen testUIScreen = new UIScreen();
		testUIScreen.getElements().add(new UIColorPanel(new Vector4f(1, 1, 1, 0.5f), 200, 100, 200, 200, gui.getPanelMeshBuffer()));
		testUIScreen.getElements().add(textPanel);
		
		//Texture2D sampleGUITexture = new Texture2D(FileUtils.getResourceLocation("textures/sample_gui.jpg"), SamplerFilter.Bilinear);
		//testUIScreen.getElements().add(new UITexturePanel(sampleGUITexture, 200, 300, 375, 375, gui.getPanelMeshBuffer()));
		
		//testUIScreen.getElements().add(textPanel);
		
		gui.getScreens().add(testUIScreen);
		Context.getRenderEngine().setGUI(gui);
	}
	
	private void createRenderables() {
		Context.getRenderEngine().getScenegraph().addObject(new Skybox());
		
		// Process models and create renderables
        for(Model model : rockModels) {
            MeshVBO meshBuffer = new MeshVBO();
            meshBuffer.addData(model.getMesh());
            Renderer renderInfo = new Renderer(
                TestModelShader.getInstance(), 
                new DefaultRenderParameter(), 
                meshBuffer
            );
            
            Renderable object = new Renderable();
            object.addComponent("main", renderInfo);
            object.addComponent("material", model.getMaterial());
            object.getWorldTransform().setTranslation(new Vector3f(0, -1, -5));
    		object.getWorldTransform().setRotation(20, 50, 10);
    		Context.getRenderEngine().getScenegraph().addObject(object);
            rockRenderables.add(object);
        }
	}
	
	private void registerSystems() {
		cameraSys = new TestCameraSystem(bibaSound);
		Context.getCoreEngine().addSystem(cameraSys);
	}
}
