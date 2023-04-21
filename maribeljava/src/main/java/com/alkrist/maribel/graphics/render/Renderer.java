package com.alkrist.maribel.graphics.render;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.InstancedRenderable;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;

public abstract class Renderer {

	protected RenderParameter config;
	protected ShaderProgram shader;
	
	protected static final ComponentMapper<Transform> transformMapper = ComponentMapper.getFor(Transform.class);
	protected static final ComponentMapper<Renderable> renderableComponentMapper = ComponentMapper.getFor(Renderable.class);
	protected static final ComponentMapper<InstancedRenderable> instancedRenderableComponentMapper = ComponentMapper.getFor(InstancedRenderable.class);
	
	public Renderer(RenderParameter config, ShaderProgram shader) {
		this.config = config;
		this.shader = shader;
	}
	
	protected abstract void bindAttributes(Entity entity);
	protected abstract void unbindAttributes();
	protected abstract void draw(Entity entity);
	
	public void render(Entity entity) {
		Transform t = transformMapper.getComponent(entity);
		Renderable r = renderableComponentMapper.getComponent(entity);
		if(t != null && r != null && r.mesh.hasBoundingSphere()) {
			if(GLContext.getMainCamera().insideFrustum(t.position, r.mesh.getBoundingRadius())) {
				doRendering(entity);
			}
		}else
			doRendering(entity);
	}
	
	private void doRendering(Entity entity) {
		config.enable();
		shader.bind();
		bindAttributes(entity);
		shader.updateUniforms(entity);
		draw(entity);
		unbindAttributes();
		config.disable();
	}
}
