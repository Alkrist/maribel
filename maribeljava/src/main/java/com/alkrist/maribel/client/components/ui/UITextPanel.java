package com.alkrist.maribel.client.components.ui;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.client.components.ui.font.FontType;
import com.alkrist.maribel.client.components.ui.font.Glyph;
import com.alkrist.maribel.client.memory.UIVAO;
import com.alkrist.maribel.client.model.Mesh;
import com.alkrist.maribel.client.model.Vertex;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlending;

public class UITextPanel extends UIElement{

	private UITextPanelShader shader;
	
	protected Mesh panel;
	private FontType font;
	protected String outputText;
	private float scale;
	private Vector4f color; // rgba
	
	private int xPos;
	private int yPos;
	
	private RenderParameter config;
	private UIVAO vao;
	
	
	public UITextPanel(String text, FontType font, Vector4f color, int xPos, int yPos, float scale) {
		super(xPos, yPos, scale, scale);
		
		this.shader = UITextPanelShader.getInstance();
		
		this.font = font;
        this.outputText = text;
        this.scale = scale;
        this.color = color;
        
        this.xPos = xPos;
        this.yPos = yPos;
        
        this.panel = generateTextMesh();
        
        this.config = new AlphaBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.vao = new UIVAO();
        vao.addData(panel);
	}
	
	private Mesh generateTextMesh() {
        List<Vertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float cursorX = xPos;
        float cursorY = yPos;
        int indexOffset = 0;

        for (int i = 0; i < outputText.length(); i++) {
            char c = outputText.charAt(i);
            Glyph glyph = font.getGlyph(c);

            if (c == '\n') {
                cursorX = xPos;
                cursorY -= font.getLineHeight() * scale;
                continue;
            }

            // Calculate vertex positions (quad for each character)
            float x0 = cursorX + glyph.getBearing().x * scale;
            float y0 = cursorY - (glyph.getSize().y - glyph.getBearing().y) * scale;
            float x1 = x0 + glyph.getSize().x * scale;
            float y1 = y0 + glyph.getSize().y * scale;

            // Texture coordinates (from atlas)
            Vector2f texPos = glyph.getPosition();
            Vector2f texSize = glyph.getSize();
            Vector2f texCoord0 = new Vector2f(texPos.x, texPos.y);
            Vector2f texCoord1 = new Vector2f(texPos.x + texSize.x, texPos.y + texSize.y);

            // Add vertices (two triangles per character)
            vertices.add(new Vertex(new Vector3f(x0, y0, 0), new Vector2f(texCoord0.x, texCoord0.y)));
            vertices.add(new Vertex(new Vector3f(x0, y1, 0), new Vector2f(texCoord0.x, texCoord1.y)));
            vertices.add(new Vertex(new Vector3f(x1, y1, 0), new Vector2f(texCoord1.x, texCoord1.y)));
            vertices.add(new Vertex(new Vector3f(x1, y0, 0), new Vector2f(texCoord1.x, texCoord0.y)));

            // Add indices (two triangles)
            indices.add(indexOffset + 0);
            indices.add(indexOffset + 1);
            indices.add(indexOffset + 2);
            indices.add(indexOffset + 0);
            indices.add(indexOffset + 2);
            indices.add(indexOffset + 3);

            indexOffset += 4;
            cursorX += glyph.getAdvance() * scale;
        }

        // Convert lists to arrays
        Vertex[] vertexArray = vertices.toArray(new Vertex[0]);
        int[] indexArray = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh(vertexArray, indexArray);
    }

	public void render() {
		config.enable();
		shader.bind();
		shader.updateUniforms(getOrthographicMatrix());
		shader.updateUniforms(color);
		glActiveTexture(GL_TEXTURE0);
		font.getTextureAtlas().bind();
		shader.updateUniforms(0);
		vao.draw();
		config.disable();
	}
	
	public void setText(String newText) {
        this.outputText = newText;
        this.panel = generateTextMesh();
    }
}
