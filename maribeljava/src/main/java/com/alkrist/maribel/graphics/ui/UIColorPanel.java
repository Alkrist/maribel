package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.ui.UIConstraint.ConstraintType;
import com.alkrist.maribel.graphics.ui.UIConstraint.OffsetFrom;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;

public class UIColorPanel extends UIElement{

	float[] positions = {
			-1, 1, 0, 
			-1, -1, 0,
			1, -1, 0,
			1, -1, 0,
			1, 1, 0,
			-1, 1, 0};
	private static Mesh mesh;
	private static UIColorPanelShader shader;
	
	private Vector4f color;
	
	public UIColorPanel(UIConstraints constraints, Vector4f color) {
		super(constraints);
		
		this.color = color;
		
		if(mesh == null) {
			mesh = ResourceLoader.loadToVAO(positions, 3);
		}
		
		if(shader == null) {
			shader = UIColorPanelShader.getInstance();
		}
	}
	
	//TODO: add another constructor for transparent UI panel

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
		Vector2f position = constraints.getPosition();
		Vector2f scale = constraints.getScale();
		
		matrix.identity().translateLocal(position.x, position.y, 0).scale(scale.x, scale.y, 1);
		return matrix;
	}
	
	public Vector4f getColor() {
		return color;
	}
}
