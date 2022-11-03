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
import com.alkrist.maribel.client.graphics.shader.shaders.MMCShader;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;

public class MMCRenderer {

	private MMCShader shader;
	
	public MMCRenderer(MMCShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(Map<ModelComposite, List<Transform>> instances) {
		for (ModelComposite model : instances.keySet()) {
			for (String nodeName : model.getNodeNames()) {
				MCPart node = model.getNode(nodeName);
				if (node != null) {

					bindModel(node);
					List<Transform> batch = instances.get(model);
					for (Transform transform : batch) {
						prepareInstance(transform);
						GL11.glDrawElements(GL11.GL_TRIANGLES, node.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT,
								0);
					}
					unbindModel();

				}
			}
		}
	}
	
	private void bindModel(MCPart node) {
		Mesh mesh = node.getMesh();
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0); // vertices
		GL20.glEnableVertexAttribArray(1); // texture coords
		GL20.glEnableVertexAttribArray(2); // normals
		GL20.glEnableVertexAttribArray(3); // tangents

		Texture texture = node.getTexture();
		Texture normalMap = node.getNormalMap();
		
		shader.loadSpecularProperties(node.getShineDamper(), node.getReflecivity());
		shader.loadNumberOfRows(texture.getNumberOfRows());
		shader.loadTextureOffset(node.getTextureXOffset(), node.getTextureYOffset());
		if (node.isTransparent()) {
			RenderSystem.disableCulling();
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap.getTextureId());
	}
	
	private void unbindModel() {
		RenderSystem.enableCulling();
		GL20.glDisableVertexAttribArray(0); // vertices
		GL20.glDisableVertexAttribArray(1); // texture coords
		GL20.glDisableVertexAttribArray(2); // normals
		GL20.glDisableVertexAttribArray(3); // tangents
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Transform instance) {
		Matrix4f transformationMatrix = MatrixMath.createTransformationMatrix(instance.position, instance.rotation,
				instance.scale);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
