package com.alkrist.maribel.client.components.skybox;

import com.alkrist.maribel.client.memory.MeshVBO;
import com.alkrist.maribel.client.model.Material;
import com.alkrist.maribel.client.model.Mesh;
import com.alkrist.maribel.client.scenegraph.Renderable;
import com.alkrist.maribel.client.scenegraph.Renderer;
import com.alkrist.maribel.client.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.texture.Texture2D;
import com.alkrist.maribel.client.util.AssimpModelLoader;
import com.alkrist.maribel.client.util.Constants;
import com.alkrist.maribel.graphics.render.parameter.CullFaceDisable;
import com.alkrist.maribel.utils.FileUtils;

public class Skybox extends Renderable{

	public Skybox() {
		getWorldTransform().setLocalScaling(Constants.ZFAR, Constants.ZFAR, Constants.ZFAR);
		getWorldTransform().setLocalTranslation(0,0,0);
		
		Mesh mesh = AssimpModelLoader.loadModel("assets/skybox", "skybox.obj").get(0).getMesh();
		
		MeshVBO meshBuffer = new MeshVBO();
		meshBuffer.addData(mesh);
		
		Texture2D gridSkybox = new Texture2D(FileUtils.getResourceLocation("skybox/grid_skybox.png"), SamplerFilter.Bilinear);
		Material mtl = new Material();
		mtl.setDiffusemap(gridSkybox);
		
		Renderer renderInfo = 
				new Renderer(SkyboxShader.getInstance(), new SkyboxRenderParameter(), meshBuffer);
		
		addComponent("main", renderInfo);
		addComponent("material", mtl);
	}
	
	public void render() {
		super.render();
	}
}
