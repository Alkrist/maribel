package com.alkrist.maribel.client.graphics.shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alkrist.maribel.client.graphics.RenderSystem;
import com.alkrist.maribel.client.graphics.Transform;
import com.alkrist.maribel.client.graphics.model.MCPart;
import com.alkrist.maribel.client.graphics.model.Mesh;
import com.alkrist.maribel.client.graphics.model.ModelComposite;
import com.alkrist.maribel.client.graphics.shader.shaders.ShadowMapShader;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.MatrixMath;

public class ShadowMapMMCRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowMapShader shader;
	
	protected ShadowMapMMCRenderer(ShadowMapShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}
	
	public void render(Map<ModelComposite, List<Transform>> instances) {
		for (ModelComposite model : instances.keySet()) {
			for (String nodeName : model.getNodeNames()) {
				MCPart node = model.getNode(nodeName);
				if(node != null) {
					bindModel(node);
					
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, node.getTexture().getTextureId());
					
					if(node.isTransparent())
						RenderSystem.disableCulling();
					
					List<Transform> batch = instances.get(model);
					for (Transform transform : batch) {
						prepareInstance(transform);
						GL11.glDrawElements(GL11.GL_TRIANGLES, node.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
					}
					
					if(node.isTransparent())
						RenderSystem.enableCulling();
				}
			}
		}
	}
	
	private void bindModel(MCPart node) {
		Mesh mesh = node.getMesh();
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0); // vertices
		GL20.glEnableVertexAttribArray(1); // texture coords
	}
	
	private void prepareInstance(Transform instance) {
		Matrix4f modelMatrix = MatrixMath.createTransformationMatrix(instance.position, instance.rotation, instance.scale);
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.loadMVPMatrix(mvpMatrix);
	}
}
