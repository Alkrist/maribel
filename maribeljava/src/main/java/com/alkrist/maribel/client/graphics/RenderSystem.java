package com.alkrist.maribel.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.client.graphics.loaders.ResourceLoader;
import com.alkrist.maribel.client.graphics.model.MMCRenderer;
import com.alkrist.maribel.client.graphics.model.Model;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.particles.ParticleRenderer;
import com.alkrist.maribel.client.graphics.shader.shaders.MMCShader;
import com.alkrist.maribel.client.graphics.shadows.ShadowMapMaster;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.common.event.EventHandler;
import com.alkrist.maribel.common.event.Listener;
import com.alkrist.maribel.utils.ImmutableArrayList;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;
import com.alkrist.maribel.utils.math.Vector3f;

//TODO: add GUI support, improve GUI system itself.
//TODO: update projectionMatrix when resized
public class RenderSystem extends SystemBase implements Listener{

	private DisplayManager window;
	private Vector3f backgroundColor;
	
	private ShadowMapMaster shadowMapMaster;
	
	private MMCRenderer mmcRenderer;
	private MMCShader mmcShader;

	private ParticleRenderer particleRenderer;
	
	private Matrix4f projectionMatrix;

	private ImmutableArrayList<Entity> entities;
	private Map<ModelComposite, List<Transform>> preparedInstances;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Model> modelMapper;

	private Camera mainCamera;

	private List<Light> lights;
	private ComponentMapper<Light> lightMapper;
	

	public RenderSystem(DisplayManager manager, ResourceLoader loader) {
		super();
		window = manager;
		backgroundColor = new Vector3f(0.529f,0.843f,1f);
		
		mmcShader = new MMCShader();
		
		Matrix4f projectionMatrix = manager.getProjectionMatrix();

		mmcRenderer = new MMCRenderer(mmcShader, projectionMatrix);
		particleRenderer = new ParticleRenderer(loader, projectionMatrix);
		
		preparedInstances = new HashMap<ModelComposite, List<Transform>>();
		transformMapper = ComponentMapper.getFor(Transform.class);
		modelMapper = ComponentMapper.getFor(Model.class);

		mainCamera = Camera.MAIN_CAMERA;
		lightMapper = ComponentMapper.getFor(Light.class);
		lights = new ArrayList<Light>();
		
		shadowMapMaster = new ShadowMapMaster(mainCamera, window);
	}

	@Override
	public void addedToEngine() {
		entities = engine
				.getEntitiesOf(Family.one(Model.class, Transform.class, Light.class).get());
	}

	@Override
	public void update(double deltaTime) {
		/* TODO:
		 * 1) prepare
		 * 2) render all to sub cameras (FBOs)
		 * 3) render all to the main FBO, relative to MAIN_CAMERA
		 * 4) render main FBO to the screen
		 * 5) clean up
		 */
		prepare();
		prepareEntities();
		renderModels();
		particleRenderer.render(mainCamera);
		
		clearance();
	}
	
	@EventHandler
	public void onWindowResizeEvent(WindowResizeEvent event) {
		this.projectionMatrix = event.getProjectionMatrix();
		mmcRenderer.updateProjectionMatrix(projectionMatrix);
	}
	
	private void renderModels() {
		mmcShader.start();
		Matrix4f viewMatrix = MatrixMath.createViewMatrix(mainCamera);
		mmcShader.loadLights(lights, viewMatrix);
		mmcShader.loadViewMatrix(viewMatrix);
		//TODO: remove later
		mmcShader.loadBackgroundColor(backgroundColor.x, backgroundColor.y, backgroundColor.z);
		//TODO: remove later
		mmcShader.loadFogEffect(0.007f, 1.5f);
		mmcRenderer.render(preparedInstances, shadowMapMaster.getToShadowMapSpaceMatrix());
		mmcShader.stop();
	}

	private void prepareEntities() {
		for (Entity entity : entities)
			processEntity(entity);
	}
	
	private void clearance() {
		preparedInstances.clear();
		lights.clear();
	}
	
	private void processEntity(Entity entity) {
		if (modelMapper.hasComponent(entity) && transformMapper.hasComponent(entity)) {
			Model model = modelMapper.getComponent(entity);
			if (model.model != null) {
				List<Transform> batch = preparedInstances.get(model.model);
				if (batch != null)
					batch.add(transformMapper.getComponent(entity));
				else {
					List<Transform> newBatch = new ArrayList<Transform>();
					newBatch.add(transformMapper.getComponent(entity));
					preparedInstances.put(model.model, newBatch);
				}
			}
		}

		Light light = lightMapper.getComponent(entity);
		if (light != null)
			lights.add(light);

	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
