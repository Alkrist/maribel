package com.alkrist.maribel.client.graphics.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.alkrist.maribel.graphics.texture.Texture2D;

/**
 * This class represents a basic GUI element for rendering. It has 2 states:
 * textured and colored. Textured frame can have color, but colored frame can
 * not have texture.
 * 
 * Also it can have rounded corners and border outline with a specified width
 * and color.
 * 
 * @author Alkrist
 *
 */
public class GUIFrame {

	public Vector2f position;
	public Vector2f scale;
	public Vector4f color;
	public Vector4f borderColor;
	public float borderScale;
	public float smoothCornerScale;
	private Texture2D texture;

	/**
	 * Textured GUI Frame.
	 * 
	 * @param texName  - texture path.
	 * @param position - 2D position. Dimensions are in range <-1, 1>, (0,0) is the
	 *                 center of the window.
	 * @param scale    - 2D scale. (1,1) is full window.
	 */
	public GUIFrame(String texName, Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
		//this.texture = Texture.loadTexture(texName);
		this.color = new Vector4f(0, 0, 0, 0);
		this.smoothCornerScale = 0;
		this.borderColor = new Vector4f(0, 0, 0, 0);
		this.borderScale = 0;
	}

	/**
	 * Textured GUI Frame with smooth edges.
	 * 
	 * @param texName           - texture path.
	 * @param position          - 2D position. Dimensions are in range <-1, 1>,
	 *                          (0,0) is the center of the window.
	 * @param scale             - 2D scale. (1,1) is full window.
	 * @param smoothCornerScale - the level of corners' roundness. Ranges in <0, 1>,
	 *                          where 0 - Right angle.
	 */
	public GUIFrame(String texName, Vector2f position, Vector2f scale, float smoothCornerScale) {
		this.position = position;
		this.scale = scale;
		//this.texture = Texture.loadTexture(texName);
		this.color = new Vector4f(0, 0, 0, 0);
		this.smoothCornerScale = smoothCornerScale;
		this.borderColor = new Vector4f(0, 0, 0, 0);
		this.borderScale = 0;
	}

	/**
	 * Textured GUI Frame with smooth edges and border outline.
	 * 
	 * @param texName           - texture path.
	 * @param position          - 2D position. Dimensions are in range <-1, 1>,
	 *                          (0,0) is the center of the window.
	 * @param scale             - 2D scale. (1,1) is full window.
	 * @param smoothCornerScale - the level of corners' roundness. Ranges in <0, 1>,
	 *                          where 0 - Right angle.
	 * @param borderColor       - color of the border outline.
	 * @param borderScale       - defines how wide the border will be. Ranges in <0,
	 *                          1>, where 0 - No border at all.
	 */
	public GUIFrame(String texName, Vector2f position, Vector2f scale, float smoothCornerScale, Vector4f borderColor,
			float borderScale) {
		this.position = position;
		this.scale = scale;
		//this.texture = Texture.loadTexture(texName);
		this.color = new Vector4f(0, 0, 0, 0);
		this.smoothCornerScale = smoothCornerScale;
		this.borderColor = new Vector4f(0, 0, 0, 0);
		this.borderScale = 0;
	}

	/**
	 * Colored GUI Frame with no texture.
	 * 
	 * @param color    - color of the frame.
	 * @param position - 2D position. Dimensions are in range <-1, 1>, (0,0) is the
	 *                 center of the window.
	 * @param scale    - 2D scale. (1,1) is full window.
	 */
	public GUIFrame(Vector4f color, Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.smoothCornerScale = 0;
		this.borderColor = new Vector4f(0, 0, 0, 0);
		this.borderScale = 0;
	}

	/**
	 * Colored GUI Frame with no texture and with smooth edges.
	 * 
	 * @param color             - color of the frame.
	 * @param position          - 2D position. Dimensions are in range <-1, 1>,
	 *                          (0,0) is the center of the window.
	 * @param scale             - 2D scale. (1,1) is full window.
	 * @param smoothCornerScale - the level of corners' roundness. Ranges in <0, 1>,
	 *                          where 0 - Right angle.
	 */
	public GUIFrame(Vector4f color, Vector2f position, Vector2f scale, float smoothCornerScale) {
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.smoothCornerScale = smoothCornerScale;
		this.borderColor = new Vector4f(0, 0, 0, 0);
		this.borderScale = 0;
	}

	/**
	 * Colored GUI Frame with no texture and with smooth edges and border outline.
	 * 
	 * @param color             - color of the frame.
	 * @param position          - 2D position. Dimensions are in range <-1, 1>,
	 *                          (0,0) is the center of the window.
	 * @param scale             - 2D scale. (1,1) is full window.
	 * @param smoothCornerScale - the level of corners' roundness. Ranges in <0, 1>,
	 *                          where 0 - Right angle.
	 * @param borderColor       - color of the border outline.
	 * @param borderScale       - defines how wide the border will be. Ranges in <0,
	 *                          1>, where 0 - No border at all.
	 */
	public GUIFrame(Vector4f color, Vector2f position, Vector2f scale, float smoothCornerScale, Vector4f borderColor,
			float borderScale) {
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.smoothCornerScale = smoothCornerScale;
		this.borderColor = borderColor;
		this.borderScale = borderScale;
	}

	public boolean isTextured() {
		return texture != null;
	}

	public Texture2D getTexture() {
		return texture;
	}
}
