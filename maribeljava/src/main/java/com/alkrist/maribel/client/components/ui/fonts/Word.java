package com.alkrist.maribel.client.components.ui.fonts;

import java.util.ArrayList;
import java.util.List;


public class Word {
	
	private List<Glyph> glyphs = new ArrayList<Glyph>();
	private double width = 0;
	private double fontSize;
	
	protected Word(double fontSize){
		this.fontSize = fontSize;
	}
	
	protected void addGlyph(Glyph glyph){
		glyphs.add(glyph);
		width += glyph.getxAdvance() * fontSize;
	}
	
	protected List<Glyph> getGlyphs(){
		return glyphs;
	}
	
	protected double getWordWidth(){
		return width;
	}
}
