package com.alkrist.maribel.client.components.ui.font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import com.alkrist.maribel.client.texture.Texture;
import com.alkrist.maribel.client.texture.Texture.SamplerFilter;
import com.alkrist.maribel.client.texture.Texture2D;

public class FontType {

	private Texture textureAtlas;
	private int fontSize;
    private int lineHeight;
    
    private Map<Character, Glyph> glyphs;
    
    private FontType(Texture textureAtlas, Map<Character, Glyph> glyphs, int fontSize, int lineHeight) {
        this.textureAtlas = textureAtlas;
        this.glyphs = glyphs;
        this.fontSize = fontSize;
        this.lineHeight = lineHeight;
    }
    
    public Glyph getGlyph(char c) {
        return glyphs.getOrDefault(c, glyphs.get('?'));
    }
    
    public Texture getTextureAtlas() { 
    	return textureAtlas;
    }
    
    public int getFontSize() { 
    	return fontSize;
    }
    
    public int getLineHeight() { 
    	return lineHeight;
    }
    
    public static FontType loadFont(String fontPath, String texturePath, int fontSize) {
        // Load texture atlas
        Texture2D textureAtlas = new Texture2D(texturePath, SamplerFilter.Bilinear);

        // Parse font metadata (e.g., from .fnt file or FreeType)
        Map<Character, Glyph> glyphs = new HashMap<>();

        // Example: Load glyph data (adjust based on your font format)
        try (BufferedReader reader = new BufferedReader(new FileReader(fontPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("char ")) {
                    // Parse glyph data (example for BMFont format)
                    String[] parts = line.split("\\s+");
                    char id = (char) Integer.parseInt(parts[1].split("=")[1]);
                    float x = Integer.parseInt(parts[2].split("=")[1]);
                    float y = Integer.parseInt(parts[3].split("=")[1]);
                    float width = Integer.parseInt(parts[4].split("=")[1]);
                    float height = Integer.parseInt(parts[5].split("=")[1]);
                    float xoffset = Integer.parseInt(parts[6].split("=")[1]);
                    float yoffset = Integer.parseInt(parts[7].split("=")[1]);
                    float xadvance = Integer.parseInt(parts[8].split("=")[1]);

                    glyphs.put(id, new Glyph(
                        id,
                        new Vector2f(x / textureAtlas.getMetaData().getWidth(), y / textureAtlas.getMetaData().getHeight()),
                        new Vector2f(width, height),
                        new Vector2f(xoffset, yoffset),
                        xadvance
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FontType(textureAtlas, glyphs, fontSize, fontSize + 5); // Adjust line height
    }
}
