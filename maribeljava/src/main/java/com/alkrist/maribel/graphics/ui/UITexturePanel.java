package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.texture.Texture;

public class UITexturePanel extends UIElement{

	float[] positions = {
			-1, 1, 0, 
			-1, -1, 0,
			1, -1, 0,
			1, -1, 0,
			1, 1, 0,
			-1, 1, 0};
	
	private static Mesh mesh;
	private static UITexturePanelShader shader;
	
	private Texture texture;
	private Vector2f scale;
	
	public UITexturePanel(Vector2f position, Texture texture, Vector2f scale) {
		super(position);
		this.scale = scale;
		this.texture = texture;
		
		if(mesh == null) {
			mesh = ResourceLoader.loadToVAO(positions, 3);
		}
		
		if(shader == null) {
			shader = UITexturePanelShader.getInstance();
		}
	}

	@Override
	public void updateInternal(double deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderInternal() {
		glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0);
		shader.bind();
		shader.updateUniforms(this);
		
		glDrawArrays(GL_TRIANGLE_STRIP, 0, mesh.getVertexCount());
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	protected Matrix4f getTransformationMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1);
		return matrix;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
