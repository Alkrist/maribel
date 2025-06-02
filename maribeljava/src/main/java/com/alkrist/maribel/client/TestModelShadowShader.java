package com.alkrist.maribel.client;

import org.joml.Matrix4f;

import com.alkrist.maribel.client.math.Transform;
import com.alkrist.maribel.client.render.pipeline.ShaderProgram;
import com.alkrist.maribel.client.render.scenegraph.Renderable;
import com.alkrist.maribel.client.util.Constants;
import com.alkrist.maribel.utils.FileUtils;
import com.alkrist.maribel.utils.math.MatrixMath;

public class TestModelShadowShader extends ShaderProgram {

private static TestModelShadowShader instance = null;
	
	public static TestModelShadowShader getInstance() {
	    if(instance == null) {
	    	instance = new TestModelShadowShader();
	    }
	      return instance;
	}
	
	protected TestModelShadowShader() {
		super();
		addVertexShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_vertex.glsl")));
		addGeometryShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_geometry.glsl")));
		addFragmentShader(readShaderFromFile(FileUtils.getResourceLocation("shaders/model/gms_shadow_fragment.glsl")));
		compileShader();
		
		addUniform("modelMatrix");
		
		addUniformBlock("LightViewProjections");
	}
	
	public void updateUniforms(Renderable object) {
		Transform transform = object.getWorldTransform();
		Matrix4f modelMatrix = MatrixMath.createTransformationMatrix(transform.getTranslation(), transform.getRotation(), transform.getScaling().x);
		setUniform("modelMatrix", modelMatrix);
		
		bindUniformBlock("LightViewProjections", Constants.CSM_MATRICES_UBO_INDEX);
	}
}
