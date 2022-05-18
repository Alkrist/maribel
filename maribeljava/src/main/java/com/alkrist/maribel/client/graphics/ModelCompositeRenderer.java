package com.alkrist.maribel.client.graphics;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alkrist.maribel.client.graphics.model.MCPart;
import com.alkrist.maribel.client.graphics.model.Mesh;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ModelShader;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;

public class ModelCompositeRenderer {

	private ModelShader shader;
	
	public ModelCompositeRenderer(ModelShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<ModelComposite, List<Transform>> instances) {
		for(ModelComposite model: instances.keySet()) {
			for(String nodeName: model.getNodeNames()) {
				MCPart node = model.getNode(nodeName);
				if(node!=null) {
					
					bindModel(node);
					List<Transform> batch = instances.get(model);
					for(Transform transform: batch) {
						prepareInstance(transform);
						GL11.glDrawElements(GL11.GL_TRIANGLES, node.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
					}
					unbindModel();
					
				}
			}
		}
	}
	
	private void bindModel(MCPart node) {
		Mesh mesh = node.getMesh();
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0); //vertices
		GL20.glEnableVertexAttribArray(1); //texture coords
		GL20.glEnableVertexAttribArray(2); //normals
				
		Texture texture = node.getTexture();
		shader.loadSpecularProperties(node.getShineDamper(), node.getReflecivity());
		if(node.isTransparent()) {
			RenderSystem.disableCulling();
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
	}
	
	private void unbindModel() {
		RenderSystem.enableCulling();
		GL20.glDisableVertexAttribArray(0); //vertices
		GL20.glDisableVertexAttribArray(1); //texture coords
		GL20.glDisableVertexAttribArray(2); //normals
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Transform instance) {
		Matrix4f transformationMatrix = MatrixMath.createTransformationMatrix(instance.position, instance.rotation, instance.scale);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//TODO: set bg color
	}
}
