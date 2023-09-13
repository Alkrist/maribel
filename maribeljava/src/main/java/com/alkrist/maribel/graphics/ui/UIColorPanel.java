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
	private Vector2f scale;
	
	private UIConstraint widthConstraint;
	private UIConstraint heightConstraint;
	
	public UIColorPanel(Vector4f color) {
		super();
		
		this.color = color;
		
		if(mesh == null) {
			mesh = ResourceLoader.loadToVAO(positions, 3);
		}
		
		if(shader == null) {
			shader = UIColorPanelShader.getInstance();
		}
	}
	
	public UIColorPanel(Vector2f position, Vector4f color, Vector2f scale) {
		
		this(color);
		
		this.position = position;
		this.scale = scale;
		this.color = color;
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
		matrix.identity().translateLocal(position.x, position.y, 0).scale(scale.x, scale.y, 1);
		return matrix;
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public void setWidthConstraint(UIConstraint constraint) {
		if(constraint.getType() == ConstraintType.CENTER) {
			throw new IllegalArgumentException("This constraint does not apply to size.");
		}
		
		this.widthConstraint = constraint;
	}
	
	public void setHeightConstraint(UIConstraint constraint) {
		if(constraint.getType() == ConstraintType.CENTER) {
			throw new IllegalArgumentException("This constraint does not apply to size.");
		}
		
		this.heightConstraint = constraint;
	}
	
	private float getRelativeHeight() {
		if(heightConstraint == null) {
			throw new IllegalArgumentException("no Y scale or height constraint specified.");
		}
		
		switch(heightConstraint.getType()) {
		
		case ASPECT:
			if(widthConstraint.getType() == ConstraintType.ASPECT) {
				throw new IllegalArgumentException("width and height both can't have aspect constraint at the same time.");
			}
			return getRelativeWidth() * heightConstraint.getValue();
			
		case RELATIVE:
			return heightConstraint.getValue();
			
		case PIXEL:
			return heightConstraint.getValue() / GLContext.getConfig().height;
			
		default:
			return 1;
		}
	}
	
	private float getRelativeWidth() {
		if(widthConstraint == null) {
			throw new IllegalArgumentException("no X scale or width constraint specified.");
		}
		
		switch(widthConstraint.getType()) {
		
		case ASPECT:
			if(heightConstraint.getType() == ConstraintType.ASPECT) {
				throw new IllegalArgumentException("width and height both can't have aspect constraint at the same time.");
			}
			return getRelativeHeight() * widthConstraint.getValue();
			
		case RELATIVE:
			return widthConstraint.getValue();
			
		case PIXEL:
			return widthConstraint.getValue() / GLContext.getConfig().width;
			
		default:
			return 1;
		}
	}
	
	private float getRelativeY() {
		if(posYconstraint == null) {
			throw new IllegalArgumentException("no Y position or Y position constraint specified.");
		}
		
		switch(posYconstraint.getType()) {
		
		case ASPECT:
			if(posXconstraint.getType() == ConstraintType.ASPECT) {
				throw new IllegalArgumentException("X and Y position both can't have aspect constraint at the same time.");
			}
			if(posYconstraint.hasOffsetSide() && posYconstraint.getOffsetSide() == OffsetFrom.BOTTOM) {
				return Math.abs(getRelativeX() * posYconstraint.getValue()) + (-1);
			}
			return 1 - (Math.abs(getRelativeX() * posYconstraint.getValue()));
			
		case RELATIVE:
			if(posYconstraint.hasOffsetSide() && posYconstraint.getOffsetSide() == OffsetFrom.BOTTOM) {
				return scale.y + posYconstraint.getValue() + (-1);
			}
			return 1 - (scale.y + posYconstraint.getValue());
			
		case PIXEL:
			float offset = posYconstraint.getValue() / (GLContext.getConfig().height / 2);
			if(posYconstraint.hasOffsetSide() && posYconstraint.getOffsetSide() == OffsetFrom.BOTTOM) {
				return scale.y + offset + (-1);
			}
			return 1 - (scale.y + offset);
			
		default:
			return 0;
		}
	}
	
	private float getRelativeX() {
		if(posXconstraint == null) {
			throw new IllegalArgumentException("no X position or X position constraint specified.");
		}
		
		switch(posXconstraint.getType()) {
		
		case ASPECT:
			if(posYconstraint.getType() == ConstraintType.ASPECT) {
				throw new IllegalArgumentException("X and Y position both can't have aspect constraint at the same time.");
			}
			if(posXconstraint.hasOffsetSide() && posXconstraint.getOffsetSide() == OffsetFrom.LEFT) {
				return Math.abs(getRelativeY() * posXconstraint.getValue()) + (-1);
			}
			return 1 - (Math.abs(getRelativeY() * posXconstraint.getValue()));
			
		case RELATIVE:
			if(posXconstraint.hasOffsetSide() && posXconstraint.getOffsetSide() == OffsetFrom.LEFT) {
				return scale.x + posXconstraint.getValue() + (-1);
			}
			return 1 - (scale.x + posXconstraint.getValue());
			
		case PIXEL:
			float offset = posXconstraint.getValue() / (GLContext.getConfig().width / 2);
			if(posXconstraint.hasOffsetSide() && posXconstraint.getOffsetSide() == OffsetFrom.LEFT) {
				return scale.x + offset + (-1);
			}
			return 1 - (scale.x + offset);
			
		default:
			return 0;
		}
	}
}
