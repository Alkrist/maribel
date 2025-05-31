package com.alkrist.maribel.client.components.ui;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.client.components.ui.fonts.FontMeshBuilder;
import com.alkrist.maribel.client.components.ui.fonts.FontMeshData;
import com.alkrist.maribel.client.components.ui.fonts.FontType;
import com.alkrist.maribel.client.core.Context;
import com.alkrist.maribel.client.render.memory.TextVAO;
import com.alkrist.maribel.client.render.pipeline.AlphaBlendingSrcAlpha;
import com.alkrist.maribel.client.render.pipeline.RenderParameter;

public class UITextPanel extends UIElement{

	private UITextPanelShader shader;
	
	protected FontMeshData panel;
	
	// text-related properties
	
	protected String text;
	
	private Vector3f primaryColor; // rgba
	
	private int xPos;
	private int yPos;
	
	private RenderParameter config;
	private TextVAO vao;
	
	private float fontSize;
	private float maxLineSize;
	private int numberOfLines = 0;
	private boolean isCentered = false;
	private FontType font;
	
	// shading attributes	
	private float borderWidth = 0f;
	private float borderEdge = 0f;
	private Vector3f outlineColor;
	private Vector2f outlineOffset;
	
	
	public UITextPanel(String text, FontType font, Vector3f color, int xPos, int yPos, float scaleX, float scaleY) {
        super(xPos, yPos, scaleX, scaleY);
        this.shader = UITextPanelShader.getInstance();

        this.text = text;
        this.primaryColor = color;
        this.xPos = xPos;
        this.yPos = yPos;
        
        
        this.font = font;
        this.maxLineSize = scaleX / Context.getWindow().getWidth();
        this.fontSize = (float) ((scaleY / Context.getWindow().getHeight()) / FontMeshBuilder.LINE_HEIGHT);
        
        // Initialize with proper blending
        this.config = new AlphaBlendingSrcAlpha();
        this.panel = generateTextMesh();
        this.vao = new TextVAO();
        vao.addData(panel);
        
        // shading attributes
        this.outlineColor = new Vector3f(0);
        this.outlineOffset = new Vector2f(0);
        
    }
	
	private FontMeshData generateTextMesh() {
		return font.loadText(this);
    }

	public void render() {
		config.enable();
		shader.bind();
		shader.updateUniforms(getOrthographicMatrix());
		shader.updateUniforms(primaryColor);
		shader.updateUniforms(outlineColor, outlineOffset);
		shader.updateUniforms(borderWidth, borderEdge);
		glActiveTexture(GL_TEXTURE0);
		font.getTextureAtlas().bind();
		shader.updateUniforms(0);
		vao.draw();
		config.disable();
	}
	
	public void setText(String newText) {
        this.text = newText;
        this.panel = generateTextMesh();
    }
	
	public String getTextString() {
		return text;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public float getMaxLineSize() {
		return maxLineSize;
	}
	
	public int getNumberofLines() {
		return numberOfLines;
	}
	
	public void setNumberOfLines(int numberofLines) {
		this.numberOfLines = numberofLines;
	}
	
	public boolean isCentered() {
		return isCentered;
	}
}
