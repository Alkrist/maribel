package com.alkrist.maribel.graphics.ui.fonts;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class UIText{

	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);
	
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	
	private Vector2f position;
	
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
		glBindVertexArray(textMeshVao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.updateUniforms(color, position);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	public int getMesh() {
		return textMeshVao;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void setMeshData(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public FontType getFont() {
		return font;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	protected String getTextString() {
		return textString;
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
}
