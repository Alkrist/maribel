package com.alkrist.maribel.client.graphics.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alkrist.maribel.client.graphics.BufferObjectLoader;
import com.alkrist.maribel.client.graphics.model.Mesh;
import com.alkrist.maribel.client.graphics.shader.shaders.GUIShader;
import com.alkrist.maribel.utils.math.MatrixMath;

public class GUIRenderer {

	private final Mesh quad;
	private GUIShader shader;
	
	public GUIRenderer(BufferObjectLoader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		this.quad = loader.loadToVAO(positions, 2);
		this.shader = new GUIShader();
	}
	
	public void render(List<GUIFrame> frames) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GUIFrame gui: frames) {
			shader.loadColor(gui.color);
			shader.loadHasTexture(gui.isTextured());
			if(gui.isTextured()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture().getTextureId());
			}
			shader.loadTransformation(MatrixMath.createTransformationMatrix(gui.position, gui.scale));
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}