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

/**
 * {@link UIElement} that works as color panel without texture. Can have borders and curve edges.
 * <br/>
 * All sizes are specified in {@link UIConstraint} object in order to keep the shape on resize or on different
 * aspect ratio displays.
 * 
 * @author Alkrist
 */
public class UIColorPanel extends UIElement{

	private static final float BORDER_THICKNESS_PIXELS_MODIFIER = 42;
	private static final float BORDER_CURVATURE_PIXELS_MODIFIER = 16;
	
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
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, 
			float curvatureModifier) {
		this(constraints, color);
		setBorderCurvature(curvatureModifier);
	}
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, 
			float borderThicknessModifier, Vector3f borderColor) {
		this(constraints, color);;
		this.borderColor = borderColor;
		setBorderThickness(borderThicknessModifier);
	}
	
	public UIColorPanel(UIConstraints constraints, Vector4f color, 
			float curvatureModifier, float borderThicknessModifier, Vector3f borderColor) {
		this(constraints, color, borderThicknessModifier, borderColor);
		setBorderCurvature(curvatureModifier);
	}

	public Vector4f getColor() {
		return color;
	}
	
	public Vector3f getBorderColor() {
		return borderColor;
	}
	
	@Override
	protected void updateInternal(double deltaTime) {
		return;
	}

	@Override
	protected void renderInternal() {
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
		
		float width = GLContext.getWindow().getWidth();
		float height = GLContext.getWindow().getHeight();
		
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
		float w = (float)(GLContext.getWindow().getWidth());
		float h = (float)(GLContext.getWindow().getHeight());
		
		float length = w >= h ? w : h;
		
		return (length * borderRadius) / BORDER_CURVATURE_PIXELS_MODIFIER;
	}
	
	protected float getBorderThicknessPixels() {
		float w = (float)(GLContext.getWindow().getWidth());
		float h = (float)(GLContext.getWindow().getHeight());
		float length = w >= h ? w : h;
		
		return (length * borderThickness) / BORDER_THICKNESS_PIXELS_MODIFIER;
	}

	@Override
	public boolean clearBuffer() {
		
		return mesh.clearBuffer();
	}
	
	public void setBorderCurvature(float curvatureModifier) {
		curvatureModifier = Math.max(0, curvatureModifier);
		curvatureModifier = Math.min(1, curvatureModifier);
		this.borderRadius = curvatureModifier;
	}
	
	public void setBorderThickness(float thicknessModifier) {
		thicknessModifier = Math.max(0, thicknessModifier);
		thicknessModifier = Math.min(1, thicknessModifier);
		this.borderThickness = thicknessModifier;
	}
	
	public float getBorderThicknessModifier() {
		return borderThickness;
	}
	
	public float getBorderCurvatureModifier() {
		return borderRadius;
	}
}
