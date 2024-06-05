package com.alkrist.maribel.graphics.model;

import org.joml.Matrix4f;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.components.Transform;
import com.alkrist.maribel.graphics.shader.ShaderProgram;
import com.alkrist.maribel.graphics.shadow.PSSMCamera;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.math.MatrixMath;

public class GenericModelShadowShader extends ShaderProgram{

	private static GenericModelShadowShader instance = null;
	
	public static GenericModelShadowShader getInstance() {
	    if(instance == null) {
	    	instance = new GenericModelShadowShader();
	    }
	      return instance;
	}
	
	private static final ComponentMapper<Transform> transformComponentMapper = ComponentMapper.getFor(Transform.class);
	
	protected GenericModelShadowShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_vertex.glsl")));
		addGeometryShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_geometry.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_fragment.glsl")));
		compileShader();
		
		for(int i = 0; i < PSSMCamera.PSSM_SPLITS; i++) {
			addUniform("projectionViewMatrices["+i+"]");
		}
		
		addUniform("modelMatrix");
	}
	
	public void updateUniforms(Entity e) {
		Transform transform = transformComponentMapper.getComponent(e);
		Matrix4f modelMatrix = MatrixMath.createTransformationMatrix(transform.position, transform.rotation, transform.scale);
	
		Matrix4f[] projectionViewMatrices = PSSMCamera.getProjectionViewMatrices();
		
		setUniform("modelMatrix", modelMatrix);
		for(int i=0; i < PSSMCamera.PSSM_SPLITS; i++) {
			setUniform("projectionViewMatrices["+i+"]", projectionViewMatrices[i]);
		}
	}
}
