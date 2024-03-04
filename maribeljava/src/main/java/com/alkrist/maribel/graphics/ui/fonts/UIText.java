package com.alkrist.maribel.graphics.ui.fonts;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;

/**
 * This class represents a UI Text element, which works a bit differently from {@link UIElement}.
 * It has a string of text and font size, as well as other parameters and renders a text as a mesh.
 * In order to change or resize the string, the mesh must be recreated, which this class is responsible for.
 * <br/>
 * All sizes are set using {@link UIConstraints} (same as {@link UIElement}), which allows it to keep shape and position
 * when the window is resized or on the screens with different aspect ratios.
 * 
 * @author Alkrist
 */
public class UIText{

	private String textString;
	private float fontSize;

	private TextVAO textMeshVao;
	private int vertexCount;
	private Vector3f color;
	
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	
	private Vector2f position;
	
	private float edge = 0.05f;
	private float width = 0.5f;
	
	private float borderWidth = 0f;
	private float borderEdge = 0f;
	private Vector3f outlineColor;
	private Vector2f outlineOffset;
	
	// Text size parameters
	private float textWidth = 0;
	private float textHeight = 0;
	
	private UIConstraints constraints;
	
	/**
	 * Default constructor for UI Text element.
	 * All the positions and size must be set using constraints in order to maintain the structure of UI on resize.
	 * 
	 * @param constraints - {@link UIConstraints} which has data about position and size of the text
	 * @param text - actual text string
	 * @param font - {@link FontType} of this text object
	 * @param centered - if the text is centered or not (applies for texts having multiple lines)
	 */
	public UIText(UIConstraints constraints, String text, FontType font, boolean centered) {
		
		this.textString = text;	
		this.font = font;
		this.centerText = centered;
		
		this.constraints = constraints;
		
		this.lineMaxSize = constraints.getLineLength();
		this.fontSize = constraints.getFontSize();
		
		this.color = new Vector3f(0);
		this.outlineColor = new Vector3f(0);
		this.outlineOffset = new Vector2f(0);
	}
	
	/**
	 * Constructor for UI Text Element which includes color of the text.
	 * All the positions and size must be set using constraints in order to maintain the structure of UI on resize.
	 * 
	 * @param constraints - {@link UIConstraints} which has data about position and size of the text.
	 * @param text - actual text string
	 * @param font - {@link FontType} of this text object.
	 * @param centered - if the text is centered or not (applies for texts having multiple lines).
	 * @param color - color of the text
	 */
	public UIText(UIConstraints constraints, String text, FontType font, 
			boolean centered, Vector3f color) {
		this(constraints, text, font, centered);
		
		this.color = color;
	}

	/**
	 * Constructor for UI Text Element which includes color of the text and outline (border) color.
	 * All the positions and size must be set using constraints in order to maintain the structure of UI on resize.
	 * 
	 * @param constraints - {@link UIConstraints} which has data about position and size of the text
	 * @param text - actual text string
	 * @param font - {@link FontType} of this text object
	 * @param centered - if the text is centered or not (applies for texts having multiple lines)
	 * @param color - color of the text
	 * @param outlineColor - color of the outline (border)
	 */
	public UIText(UIConstraints constraints, String text, FontType font, 
			boolean centered, Vector3f color, Vector3f outlineColor) {
		this(constraints, text, font, centered, color);
		
		this.outlineColor = outlineColor;
	}
	
	/**
	 * Constructor for UI Text Element which includes color of the text, outline (border) color and outline (border)
	 * offset, which is basically shadow of the text.
	 * All the positions and size must be set using constraints in order to maintain the structure of UI on resize.
	 * 
	 * @param constraints - {@link UIConstraints} which has data about position and size of the text
	 * @param text - actual text string
	 * @param font - {@link FontType} of this text object
	 * @param centered - if the text is centered or not (applies for texts having multiple lines)
	 * @param color - color of the text
	 * @param outlineColor - color of the outline (border)
	 * @param outlineOffset - offset of the outline (border)
	 */
	public UIText(UIConstraints constraints, String text, FontType font, 
			boolean centered, Vector3f color, Vector3f outlineColor, Vector2f outlineOffset) {
		this(constraints, text, font, centered, color, outlineColor);
		
		this.outlineOffset = outlineOffset;
	}
	
	//Recalculate positions and texture coordinates in VAO
	private void updatePositions() {
		if(textMeshVao != null) {
			TextMeshData data = font.loadText(this);
			ResourceLoader.updateTextVAO(textMeshVao, data.getVertexPositions(), data.getTextureCoords());
			setMeshData(textMeshVao, data.getVertexCount());
			
			// Set text width and height parameters
			setTextSize(data.getWidth(), data.getHeight());
		}
	}

	/**
	 * Resize the text, will cause mesh recalculation.
	 * Called on window resize.
	 */
	public void resize() {
		
		this.lineMaxSize = constraints.getLineLength();
		this.fontSize = constraints.getFontSize();
		
		updatePositions();
	}
	
	/**
	 * Renders the text.
	 * @param shader - {@link UITextShader}
	 */
	public void render(UITextShader shader) {
		glBindVertexArray(textMeshVao.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		position = constraints.getPosition(textWidth, textHeight);
		shader.updateUniforms(this);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	/**
	 * Set the VAO and vertex count. Called on creation and update of the mesh buffer.
	 * @param vao - {@link TextVAO}
	 * @param verticesCount
	 */
	public void setMeshData(TextVAO vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	/**
	 * Will erase the text buffer data.
	 * Should be called only internally, from {@link UICanvas}.
	 */
	public void deleteFromBuffer() {
		ResourceLoader.deleteVBO(textMeshVao.getPositionsVBO());
		ResourceLoader.deleteVBO(textMeshVao.getTextureCoordsVBO());
		ResourceLoader.deleteVAO(textMeshVao.getVAO());
		
		textMeshVao = null;
	}
	
	/**
	 * Set new string text for this UI text object. Will rewrite the buffer.
	 * @param text - text string
	 */
	public void setTextString(String text) {
		this.textString = text;
		updatePositions();
	}
	
	/**
	 * Set the color for text.
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	/**
	 * Set text size. Used internally in {@link UICanvas}!
	 * @param textWidth - width of the text element
	 * @param textHeight - height of the text element
	 */
	public void setTextSize(float textWidth, float textHeight) {
		this.textWidth = textWidth;
		this.textHeight = textHeight;
	}
	
	/**
	 * Set the color of outline (border).
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setOutlineColor(float r, float g, float b) {
		outlineColor.set(r, g, b);
	}
	
	/**
	 * Set offset to which the outline (border) will be moved. Basically, it is a shadow of the text.
	 * @param dx - x offset
	 * @param dy - y offset
	 */
	public void setOutlineOffset(float dx, float dy) {
		outlineOffset.set(dx, dy);
	}
	
	/**
	 * Set parameters of the outline (border).
	 * @param borderWidth - distance from center of the character to the hard edge of the border. Default is 0.
	 * @param borderEdge - distance of alpha transition on the edge of hard border. Better be small (i.e. 0.1). Default is 0.
	 */
	public void setOutlineParameters(float borderWidth, float borderEdge) {
		this.borderEdge = borderEdge;
		this.borderWidth = borderWidth;
	}

	/**
	 * Set parameters for the smooth edge transition of the character and how blury and "fat" the character will be.
	 * @param width - distance from the center to the hard edge of the character. Default is 0.5.
	 * @param edge - distance of alpha transition on the hard edge of the character. Makes it smooth. Default value is 0.05.
	 */
	public void setSmoothstepParameters(float width, float edge) {
		this.width = width;
		this.edge = edge;
	}
	
	/**
	 * @return color of the text
	 */
	public Vector3f getColor() {
		return color;
	}
	
	/**
	 * @return {@link UIConstraints} object attached to the text element.
	 */
	public UIConstraints getConstraits() {
		return constraints;
	}
	
	/**
	 * @return {@link FontType} of this text.
	 */
	public FontType getFont() {
		return font;
	}
	
	/**
	 * @return string with text.
	 */
	public String getTextString() {
		return textString;
	}
	
	/**
	 * @return outline (border) offset, which is basically shadow of the text.
	 */
	public Vector2f getOutlineOffset() {
		return outlineOffset;
	}
	
	/**
	 * @return outline (border) color.
	 */
	public Vector3f getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Gives the width of the text.
	 * 0 - left border of the screen
	 * 1 - right border of the screen
	 * @return text width in interval <0, 1>
	 */
	public float getTextWidth() {
		return textWidth;
	}

	/**
	 * Gives the height of the text.
	 * 0 - top border of the screen
	 * 1 - bottom border of the screen
	 * @return text height in interval <0, 1>
	 */
	public float getTextHeight() {
		return textHeight;
	}
	
	protected int getMesh() {
		return textMeshVao.getVAO();
	}
	
	protected int getVertexCount() {
		return this.vertexCount;
	}
	
	protected float getFontSize() {
		return fontSize;
	}
	
	protected Vector2f getPosition() {
		return position;
	}
	
	protected int getNumberOfLines() {
		return numberOfLines;
	}
	
	protected boolean isCentered() {
		return centerText;
	}
	
	protected float getMaxLineSize() {
		return lineMaxSize;
	}
	
	protected float getWidth() {
		return width;
	}
	
	protected float getEdge() {
		return edge;
	}
	
	protected float getBorderWidth() {
		return borderWidth;
	}
	
	protected float getBorderEdge() {
		return borderEdge;
	}
	
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}
		
	/**
	 * This class is internal class for {@link UIText}. It stores ID of the vertex array object of this text,
	 * ID of vertex buffer object with positions and ID with vertex buffer object with texture coordinates.
	 */
	public static class TextVAO{
		private int vao;
		private int positionsVBO;
		private int textureCoordsVBO;
		
		/**
		 * Creates new text VAO.
		 * @param vao - VAO ID
		 * @param positionsVBO - ID of positions VBO
		 * @param textureCoordsVBO - ID of texture coordinates VBO
		 */
		public TextVAO(int vao, int positionsVBO, int textureCoordsVBO) {
			this.vao = vao;
			this.positionsVBO = positionsVBO;
			this.textureCoordsVBO = textureCoordsVBO;
		}
		
		/**
		 * @return VAO ID
		 */
		public int getVAO() {
			return vao;
		}
		
		/**
		 * @return positions VBO ID
		 */
		public int getPositionsVBO() {
			return positionsVBO;
		}
		
		/**
		 * @return texture coordinates VBO ID
		 */
		public int getTextureCoordsVBO() {
			return textureCoordsVBO;
		}
		
		/**
		 * Is used to rewrite positions buffer.
		 * @param vbo - new VBO ID
		 */
		public void setPositionsVBO(int vbo) {
			this.positionsVBO = vbo;
		}
		
		/**
		 * Is used to rewrite texture coordinates buffer.
		 * @param vbo - new texture coordinates VBO ID.
		 */
		public void setTextureCoordsVBO(int vbo) {
			this.textureCoordsVBO = vbo;
		}
	}
}
