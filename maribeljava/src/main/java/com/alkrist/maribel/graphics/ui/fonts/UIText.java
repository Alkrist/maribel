package com.alkrist.maribel.graphics.ui.fonts;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.graphics.model.ResourceLoader;

public class UIText{

	private String textString;
	private float fontSize;

	private TextVAO textMeshVao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);
	
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	
	private Vector2f position;
	
	private float edge = 0.02f;
	private float width = 0.5f;
	
	private float borderWidth = 0.7f;
	private float borderEdge = 0.1f;
	private Vector3f outlineColor = new Vector3f(1, 0, 0);
	private Vector2f outlineOffset = new Vector2f(0.006f, 0.006f);
	
	public UIText(Vector2f position, String text, float fontSize, FontType font, float maxLineLength,
			boolean centered) {
		
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		
		this.position = position;
	}
	
	public void render(UITextShader shader) {
		glBindVertexArray(textMeshVao.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.updateUniforms(this);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	public int getMesh() {
		return textMeshVao.getVAO();
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void setMeshData(TextVAO vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public void setOutlineParameters(Vector3f outlineColor, Vector2f outlineOffset) {
		this.outlineColor = outlineColor;
		this.outlineOffset = outlineOffset;
	}
	
	public void setBorderParameters(float borderWidth, float borderEdge) {
		this.borderEdge = borderEdge;
		this.borderWidth = borderWidth;
	}

	public FontType getFont() {
		return font;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	public String getTextString() {
		return textString;
	}
	
	public Vector2f getOutlineOffset() {
		return outlineOffset;
	}
	
	public Vector3f getOutlineColor() {
		return outlineColor;
	}
	
	public void setTextString(String text) {
		this.textString = text;
		
		if(textMeshVao != null) {
			TextMeshData data = font.loadText(this);
			ResourceLoader.updateTextVAO(textMeshVao, data.getVertexPositions(), data.getTextureCoords());
			setMeshData(textMeshVao, data.getVertexCount());
		}
	}

	public void deleteFromBuffer() {
		ResourceLoader.deleteVBO(textMeshVao.getPositionsVBO());
		ResourceLoader.deleteVBO(textMeshVao.getTextureCoordsVBO());
		ResourceLoader.deleteVAO(textMeshVao.getVAO());
		
		textMeshVao = null;
	}

	protected float getFontSize() {
		return fontSize;
	}
	
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
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
	
	public static class TextVAO{
		private int vao;
		private int positionsVBO;
		private int textureCoordsVBO;
		
		public TextVAO(int vao, int positionsVBO, int textureCoordsVBO) {
			this.vao = vao;
			this.positionsVBO = positionsVBO;
			this.textureCoordsVBO = textureCoordsVBO;
		}
		
		public int getVAO() {
			return vao;
		}
		
		public int getPositionsVBO() {
			return positionsVBO;
		}
		
		public int getTextureCoordsVBO() {
			return textureCoordsVBO;
		}
		
		public void setPositionsVBO(int vbo) {
			this.positionsVBO = vbo;
		}
		
		public void setTextureCoordsVBO(int vbo) {
			this.textureCoordsVBO = vbo;
		}
	}
}
