package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.model.Mesh;
import com.alkrist.maribel.graphics.model.ResourceLoader;
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
	
	private Vector2f position;
	private Vector2f scale;
	
	private Vector4f color;
	
	private Vector3f borderColor;
	private float borderThickness = 0;
	
	private float borderRadius = 0;
	
	private Vector2f framePixelPosition;
	private Vector2f framePixelSize;
	
	
	public UIColorPanel(UIConstraints constraints, Vector4f color) {
		super(constraints);
		
		this.color = color;
		
		if(mesh == null) {
			mesh = ResourceLoader.loadToVAO(positions, 3);
		}
		
		if(shader == null) {
			shader = UIColorPanelShader.getInstance();
		}
		
		this.framePixelPosition = new Vector2f(0);
		this.framePixelSize = new Vector2f(0);
	}
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, float borderRadius) {
		this(constraints, color);
		this.borderRadius = borderRadius;
	}
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, float borderThickness, Vector3f borderColor) {
		this(constraints, color);
		this.borderThickness = borderThickness;
		this.borderColor = borderColor;
	}
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, float borderRadius, float borderThickness, Vector3f borderColor) {
		this(constraints, color, borderThickness, borderColor);
		this.borderRadius = borderRadius;
	}
	
	//TODO: add another constructor for more args UI panel

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
		position = constraints.getPosition();
		scale = constraints.getScale();
		
		float width = GLContext.getConfig().width;
		float height = GLContext.getConfig().height;
		
		framePixelPosition.x = width/2 + position.x * (width/2);
		framePixelPosition.y = height/2 +position.y * (height/2);
		
		framePixelSize.x = scale.x * width;
		framePixelSize.y = scale.y * height;
		
		
		matrix.identity().translateLocal(position.x, position.y, 0).scale(scale.x, scale.y, 1);
		return matrix;
	}
	
	protected Vector2f getFramePositionPixels() {
		return framePixelPosition;
	}
	
	protected Vector2f getFrameScalePixels() {
		return framePixelSize;
	}
	
	protected float getBorderRadiusPixels() {
		return (float)(GLContext.getConfig().width / GLContext.getConfig().height) * 40 * borderRadius;
	}
	
	protected float getBorderThicknessPixels() {
		return (float)(GLContext.getConfig().width / GLContext.getConfig().height) * 20 * borderThickness;
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public Vector3f getBorderColor() {
		return borderColor;
	}
}
