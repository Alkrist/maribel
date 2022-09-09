package com.alkrist.maribel.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.alkrist.maribel.client.graphics.model.Model;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.utils.ImmutableArrayList;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;
import com.alkrist.maribel.utils.math.Vector3f;

//TODO: add GUI support, improve GUI system itself.
//TODO: add tests for this whole thing.
public class RenderSystem extends SystemBase {

	private DisplayManager window;
	private Vector3f backgroundColor;
	
	private ModelCompositeRenderer modelRenderer;
	private ModelShader modelShader;

	private Matrix4f projectionMatrix;

	private ImmutableArrayList<Entity> entities;
	private Map<ModelComposite, List<Transform>> preparedInstances;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Model> modelMapper;

	private Camera camera;
	private ComponentMapper<Camera> cameraMapper;

	private List<Light> lights;
	private ComponentMapper<Light> lightMapper;

	public RenderSystem(DisplayManager manager) {
		super();
		window = manager;
		backgroundColor = new Vector3f(0,0,0);
		
		modelShader = new ModelShader();
		Matrix4f projectionMatrix = MatrixMath.createProjectionMatrix(window.getWidth(), window.getHeight());

		modelRenderer = new ModelCompositeRenderer(modelShader, projectionMatrix);
		preparedInstances = new HashMap<ModelComposite, List<Transform>>();
		transformMapper = ComponentMapper.getFor(Transform.class);
		modelMapper = ComponentMapper.getFor(Model.class);

		cameraMapper = ComponentMapper.getFor(Camera.class);
		lightMapper = ComponentMapper.getFor(Light.class);
		lights = new ArrayList<Light>();
	}

	@Override
	public void addedToEngine() {
		// (model ^ transform) | light | camera
		entities = engine
				.getEntitiesOf(Family.all(Model.class, Transform.class).one(Light.class).one(Camera.class).get());
	}

	@Override
	public void update(double deltaTime) {
		prepare();
		renderModels();
	}

	private void renderModels() {
		for (Entity entity : entities)
			processEntity(entity);

		modelShader.start();
		modelShader.loadViewMatrix(MatrixMath.createViewMatrix(camera));
		modelShader.loadLights(lights);
		modelRenderer.render(preparedInstances);
		modelShader.stop();

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

		if (cameraMapper.hasComponent(entity)) // quite depricated bullshit: can work with no cameras, but it's ecs dude
												// ;)
			camera = cameraMapper.getComponent(entity);
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
