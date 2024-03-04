package com.alkrist.maribel.graphics.ui.constraints;

import org.joml.Vector2f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.ui.fonts.TextMeshCreator;

/**
 * This class keeps all the constraints for positioning and scaling the UI element.
 * UI Element's values are not stored directly, instead, they're written as relative values and stored in 
 * constraints, which are used to calculate actual values later.
 * This is important for resizing purposes as well as different screen resolutions on different displays.
 * Using constraints instead of directly coding values will provide the necessary flexibility and abstraction for 
 * all the display types.
 * 
 * The following example shows how to create a simple textured UI frame that will be positioned on top-right
 * corner with a gap of 20 pixels from top and right border:
 * <pre>
 *  UIConstraints texturePanelConstraints = new UIConstraints()
			.setWidth(new RelativeConstraint(0.5f))
			.setHeight(new AspectConstraint(1))
			.setX(UIConstraints.MarginHorizontal.RIGHT,new PixelConstraint(20))
			.setY(UIConstraints.MarginVertical.TOP, new PixelConstraint(20));
		
	UIElement texturePanel = new UITexturePanel(texturePanelConstraints, texture);
 * </pre>
 * The types of constraints are:
 * {@link CenterConstraint} - position constraint to place an element in the center.<br/>
 * {@link AspectConstraint} - scale constraint to make the height relative to width of the UI element.<br/>
 * {@link RelativeConstraint} - position and scale constraint to set the relative value (from -1 to 1).<br/>
 * {@link PixelConstraint} - position and scale constraint to set the strict value in pixels (unlimited).<br/>
 * 
 * The typical flow of creating a constraint is as follows:<br/>
 * 1) create UIConstraints<br/>
 * 2) set the properties for all 4 attributes as shown in example<br/>
 * 3) add UIConstraints to the UIFrame.<br/>
 * <br/>
 * Default values for constraints are: (0,0) - for position, (1,1) - for scale (full-window quad).
 * <br/>
 * Coordinates on the display:
 * <br/>
 * <pre>
 *               TOP
 *     width     y=0
 *     |----------|----------|
 *     |          |          | RIGHT
 *x=0  ----------------------- x=1
 *LEFT |          |          |
 *     |----------|----------| height
 *               y=1
 *              BOTTOM
 * </pre>
 * Constraints' values can be changed in the runtime using setters.
 *
 * @author Alkrist
 */
public class UIConstraints {

	
	private PositionConstraint xConstraint;
	private PositionConstraint yConstraint;
	private ScaleConstraint widthConstraint;
	private ScaleConstraint heightConstraint;
	
	private Vector2f position;
	private Vector2f scale;
	
	/**
	 * Create UIConstraints element. It will set default values.
	 */
	public UIConstraints() {
		this.position = new Vector2f(0);
		this.scale = new Vector2f(1);
	}
	
	/**
	 * Set X constraint with margin.
	 * It will calculate position from the given border to the border of UIElement!
	 * 
	 * @param margin - {@link MarginHorizontal} margin. Allowed types are LEFT, RIGHT
	 * @param constraint - constraint. Allowed types are: {@link CenterConstraint}, 
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * 
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setX(MarginHorizontal margin, PositionConstraint constraint) {
		
		if(margin == MarginHorizontal.LEFT) {
			constraint.setToNegativeCoord();
		}
		
		constraint.setMargin();
		
		this.xConstraint = constraint;
		
		return this;
	}
	
	/**
	 * Set X constraint without margin.
	 * It will calculate position from the center of UI element relative to the center of window!
	 * 
	 * @param constraint - constraint. Allowed types are: {@link CenterConstraint}, 
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setX(PositionConstraint constraint) {
		this.xConstraint = constraint;
		return this;
	}
	
	/**
	 * Set Y constraint with margin.
	 * It will calculate position from the given border to the border of UIElement!
	 * 
	 * @param margin - {@link MarginVertical} margin. Allowed types are TOP, BOTTOM
	 * @param constraint - constraint. Allowed types are: {@link CenterConstraint}, 
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * 
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setY(MarginVertical margin, PositionConstraint constraint) {
		
		if(margin == MarginVertical.BOTTOM) {
			constraint.setToNegativeCoord();
		}
		
		constraint.setMargin();
		
		this.yConstraint = constraint;
		return this;
	}
	
	/**
	 * Set Y constraint without margin.
	 * It will calculate position from the center of UI element relative to the center of window!
	 * 
	 * @param constraint - constraint. Allowed types are: {@link CenterConstraint},
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setY(PositionConstraint constraint) {
		this.yConstraint = constraint;
		return this;
	}
	
	/**
	 * Set width constraint.
	 * 
	 * @param constraint - constraint. Allowed types are: {@link AspectConstraint},
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setWidth(ScaleConstraint constraint) {
		constraint.setValue(Math.abs(constraint.getValue()));
		this.widthConstraint = constraint;
		return this;
	}
	
	/**
	 * Set height constraint.
	 * 
	 * @param constraint - constraint. Allowed types are: {@link AspectConstraint},
	 * {@link RelativeConstraint}, {@link PixelConstraint}
	 * @return UIConstraint object (this), just to make cascaded method calls.
	 */
	public UIConstraints setHeight(ScaleConstraint constraint) {
		constraint.setValue(Math.abs(constraint.getValue()));
		this.heightConstraint = constraint;
		return this;
	}
	
	/**
	 * Method is used for UI elements.
	 * @return (x,y) actual screen position of owner UI element in normalized device coordinates.
	 */
	public Vector2f getPosition() {
		
		float offsetX = xConstraint.getRelativeValue(GLContext.getConfig().width, scale.x);
		float offsetY = yConstraint.getRelativeValue(GLContext.getConfig().height, scale.x);
		
		if(xConstraint.isMargin()) {
			position.x = xConstraint.isMarginNegative() ? (-1 + (offsetX * 2) + scale.x) : (1 - (offsetX * 2) - scale.x);
		}else {
			position.x = (-1 + (offsetX * 2));
		}
		
		if(yConstraint.isMargin()) {
			position.y = yConstraint.isMarginNegative() ? (-1 + (offsetY * 2) + scale.y) : (1 - (offsetY * 2) - scale.y);
		}else {
			position.y = (1 - (offsetY * 2));
		}
		
		return position;
	}

	/**
	 * Method is used for UI texts.
	 * @param width - text element width
	 * @param height - text element height
	 * @return (x,y) actual screen position of owner UI element in view space coordinates
	 */
	public Vector2f getPosition(float width, float height) {
		
		float offsetX = xConstraint.getRelativeValue(GLContext.getConfig().width, 0);
		float offsetY = yConstraint.getRelativeValue(GLContext.getConfig().height, 0);
		
		if(xConstraint.isMargin()) {
			position.x = xConstraint.isMarginNegative() ? offsetX : 1 - width - offsetX;
		}else {
			position.x = offsetX - (width / 2);
		}
		
		if(yConstraint.isMargin()) {
			position.y = yConstraint.isMarginNegative() ? 1 - height - offsetY : offsetY;
		}else {
			position.y = offsetY - (height / 2);
		}

		return position;
	}
	
	/**
	 * @return font size based on text scale (text height).
	 */
	public float getFontSize() {
		return (float) (heightConstraint.getRelativeValue(GLContext.getConfig().height, 0) / TextMeshCreator.LINE_HEIGHT);
	}
	
	/**
	 * @return (width, height) actual screen scale of owner UI element.
	 */
	public Vector2f getScale() {
		scale.x = widthConstraint.getRelativeValue(GLContext.getConfig().width, scale.x);
		scale.y = heightConstraint.getRelativeValue(GLContext.getConfig().height, scale.x);
		
		return scale;
	}
	
	public PositionConstraint getXConstraint() {
		return xConstraint;
	}
	
	public PositionConstraint getYConstraint() {
		return yConstraint;
	}
	
	public ScaleConstraint getWidthConstraint() {
		return widthConstraint;
	}
	
	public ScaleConstraint getHeightConstraint() {
		return heightConstraint;
	}
	
	
	/**
	 * Margin types for horizontal (x-axis) constraints.<br/>
	 * LEFT: element will be aligned by left border of the screen and left border of itself.<br/>
	 * RIGHT: element will be aligned by right border of the screen and right border of itself.<br/>
	 * (hence, you specify the gap by the constraint)<br/>
	 */
	public static enum MarginHorizontal{
		LEFT,
		RIGHT
	}
	
	/**
	 * Margin types for vertical (y-axis) constraints.<br/>
	 * TOP: element will be aligned by top border of the screen and top border of itself.<br/>
	 * BOTTOM: element will be aligned by bottom border of the screen and bottom border of itself.<br/>
	 * (hence, you specify the gap by the constraint)<br/>
	 */
	public static enum MarginVertical{
		TOP,
		BOTTOM
	}
	
	/**
	 * This is a helper method that gives you number of pixels for a certain percentage based on the
	 * current window width.
	 * 
	 * @param relativeValue - percentage value
	 * @return - pixel percentage width value
	 */
	public static int getWidthPixelsFor(float relativeValue) {
		return Math.round(relativeValue * GLContext.getConfig().width);
	}
	
	/**
	 * This is a helper method that gives you number of pixels for a certain percentage based on the
	 * current window height.
	 * 
	 * @param relativeValue - percentage value
	 * @return - pixels percentage height value
	 */
	public static int getHeightPixelsFor(float relativeValue) {
		return Math.round(relativeValue * GLContext.getConfig().height);
	}
}
