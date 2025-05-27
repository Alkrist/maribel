package com.alkrist.maribel.client.components.ui.font;

import org.joml.Vector2f;

public class Glyph {

	private char character;
    private Vector2f position;
    private Vector2f size;
    private Vector2f bearing;
    private float advance;
    
    public Glyph(char character, Vector2f position, Vector2f size, Vector2f bearing, float advance) {
        this.character = character;
        this.position = position;
        this.size = size;
        this.bearing = bearing;
        this.advance = advance;
    }

	public char getCharacter() {
		return character;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getSize() {
		return size;
	}

	public Vector2f getBearing() {
		return bearing;
	}

	public float getAdvance() {
		return advance;
	}
    
    
}
