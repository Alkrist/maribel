package com.alkrist.maribel.graphics.model;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Renderable;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.math.MatrixMath;

public class GenericModelShader extends ShaderProgram{
	
	private static GenericModelShader instance = null;
	
	private static final ComponentMapper<Renderable> renderableComponentMapper = ComponentMapper.getFor(Renderable.class);
	private static final ComponentMapper<Transform> transformComponentMapper = ComponentMapper.getFor(Transform.class);

	
	public static GenericModelShader getInstance() {
	    if(instance == null) {
	    	instance = new GenericModelShader();
	    }
	      return instance;
	}
	
	protected GenericModelShader() {
		super();
		
		addVertexShader(readShaderFromFile(FileUtil.getShadersPath()+"model\\generic_model_vertex.glsl"));
		addFragmentShader(readShaderFromFile(FileUtil.getShadersPath()+"model\\generic_model_fragment.glsl"));
		compileShader();
		
		addUniform("material.diffusemap");
		addUniform("material.normalmap");
		addUniform("material.shininess");
		addUniform("material.emission");
		
		addUniform("projectionMatrix");
		addUniform("viewModelMatrix");
		
		/*for(int i=0; i<6; i++) {
			addUniform("frustumPlanes["+i+"]");
		}*/
	}
	
	public void updateUniforms(Entity e) {
		Renderable renderable = renderableComponentMapper.getComponent(e);
		Transform transform = transformComponentMapper.getComponent(e);
		
		Matrix4f viewMatrix = GLContext.getMainCamera().getViewMatrix();
		Matrix4f vmMatrix = MatrixMath.createTransformationMatrix(transform.position, transform.rotation, transform.scale);
		//Vector4f frustumPlanes[] = MatrixMath.createFrustumPlanes(GLContext.getMainCamera().getProjectionMatrix(), viewMatrix, vmMatrix);
		viewMatrix.mul(vmMatrix, viewMatrix);
		
		setUniform("viewModelMatrix", viewMatrix);
		setUniform("projectionMatrix", GLContext.getMainCamera().getProjectionMatrix());
		
		/*for(int i=0; i<6; i++) {
			setUniform("frustumPlanes["+i+"]", frustumPlanes[i]);
		}*/
		
		glActiveTexture(GL_TEXTURE0);
		renderable.material.getDiffuseMap().bind();
		setUniform("material.diffusemap", 0);
		
		glActiveTexture(GL_TEXTURE1);
		renderable.material.getDiffuseMap().bind();
		setUniform("material.normalmap", 1);
		
		setUniform("material.shininess", renderable.material.getShininess());
		setUniform("material.emission", renderable.material.getEmission());
	}
}
