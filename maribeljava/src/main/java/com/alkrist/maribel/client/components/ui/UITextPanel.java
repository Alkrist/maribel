package com.alkrist.maribel.client.components.ui;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector4f;

import com.alkrist.maribel.client.components.ui.font.FontType;
import com.alkrist.maribel.client.components.ui.font.TextMeshBuilder;
import com.alkrist.maribel.client.memory.UIVAO;
import com.alkrist.maribel.client.model.Mesh;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlending;

public class UITextPanel extends UIElement{

	private UITextPanelShader shader;
	
	protected Mesh panel;
	
	// text-related properties
	private FontType font;
	protected String outputText;
	protected float fontSize;
	protected float maxLineSize;
	protected boolean isCentered = false;
	protected int numberOfLines;
	private Vector4f color; // rgba
	
	private int xPos;
	private int yPos;
	
	private RenderParameter config;
	private UIVAO vao;
	
	
	public UITextPanel(String text, FontType font, Vector4f color, int xPos, int yPos, float scaleX, float scaleY) {
        super(xPos, yPos, scaleX, scaleY);
        this.shader = UITextPanelShader.getInstance();
        this.font = font;
        this.outputText = text;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        
        this.maxLineSize = 0.5f;
        this.numberOfLines = 1;
        this.fontSize = (float) TextMeshBuilder.LINE_HEIGHT;
        // Initialize with proper blending
        this.config = new AlphaBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.panel = generateTextMesh();
        this.vao = new UIVAO();
        vao.addData(panel);
    }
	
	private Mesh generateTextMesh() {
		return font.loadText(this)
				.generateMesh();
    }

	public void render() {
		config.enable();
		shader.bind();
		shader.updateUniforms(getOrthographicMatrix());
		shader.updateUniforms(color);
		glActiveTexture(GL_TEXTURE0);
		//font.getTextureAtlas().bind();
		shader.updateUniforms(0);
		vao.draw();
		config.disable();
	}
	
	public void setText(String newText) {
        this.outputText = newText;
        this.panel = generateTextMesh();
    }
	
	public String getTextString() {
		return outputText;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public float getMaxLineSize() {
		return maxLineSize;
	}
	
	public boolean isCentered() {
		return isCentered;
	}
	
	public void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}
}
