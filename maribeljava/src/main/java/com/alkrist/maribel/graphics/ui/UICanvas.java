package com.alkrist.maribel.graphics.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.ui.fonts.FontType;
import com.alkrist.maribel.graphics.ui.fonts.TextMeshData;
import com.alkrist.maribel.graphics.ui.fonts.UIText;

/**
 * A parent class for UI container.
 * 
 * @author Alkrist
 */
public abstract class UICanvas {

	protected List<UIElement> elements;
	protected Map<FontType, List<UIText>> texts;
	
	public UICanvas() {
		elements = new ArrayList<UIElement>();
	}
	
	/**
	 * Add a {@link UIElement} to this canvas. UI elements can have child UI elements and will be rendered in
	 * hierarchy to maintain the order.
	 * @param element - UI element that will be added
	 */
	public void addUIElement(UIElement element) {
		elements.add(element);
	}
	
	/**
	 * Add a {@link UIText} to this canvas. UI texts are mapped by font type, the order doesn't matter in their
	 * case and they will always be rendered on top of the other UI.
	 * @param text - UI text element that will be added. It will also create the mesh for the text
	 */
	public void addUIText(UIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		UIText.TextVAO vao = ResourceLoader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		
		// Set text mesh
		text.setMeshData(vao, data.getVertexCount());
		
		// Set text width and height parameters
		text.setTextSize(data.getWidth(), data.getHeight());
		
		if(texts == null) {
			texts = new HashMap<FontType, List<UIText>>();
		}
		
		List<UIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<UIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	/**
	 * Removes a {@link UIText} object from texts of this canvas. Will also remove text's buffer.
	 * @param text - UI text that will be removed
	 * @return if text was removed or not
	 */
	public boolean removeUIText(UIText text) {
		List<UIText> textBatch = texts.get(text.getFont());
		if(textBatch != null) {
			text.deleteFromBuffer();
			textBatch.remove(text);
			if(textBatch.isEmpty()) {
				texts.remove(text.getFont());
			}
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Removes {@link UIElement} object from elements of this canvas.
	 * @param element
	 * @return
	 */
	public boolean removeUIElement(UIElement element) {
		return elements.remove(element);
	}
	
	/**
	 * Removes all UI elements and texts from this canvas.
	 */
	public void clearUIElements() {
		elements.clear();
		
		Iterator<List<UIText>> iterator = texts.values().iterator();
	    while (iterator.hasNext()) {
	        List<UIText> textBatch = iterator.next();
	        iterator.remove();

	        for (UIText text : textBatch) {
	            text.deleteFromBuffer();
	        }
	        textBatch.clear();
	    }
	}
	
	/**
	 * @return a list of all UI elements of this canvas. Can be null!
	 */
	public List<UIElement> getUIElements(){
		return elements;
	}
	
	/**
	 * @return a map of all texts and their fonts of this canvas. Can be null!
	 */
	public Map<FontType, List<UIText>> getAllTexts(){
		return texts;
	}
	
	/**
	 * Will give the list of texts for specified font.
	 * @param font - font for which the texts are returned
	 * @return list of {@link UIText} for this font. Can be null!
	 */
	public List<UIText> getTextsFor(FontType font){
		return texts.get(font);
	}
	
	public void resize(int width, int height) {
		for(FontType key: texts.keySet()) {
			for(UIText text: texts.get(key)) {
				text.resize();
			}
		}
	}
	
	public abstract void render();
	
	public abstract void update(double deltaTime);
	
}
