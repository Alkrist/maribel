package com.alkrist.maribel.graphics.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alkrist.maribel.graphics.model.ResourceLoader;
import com.alkrist.maribel.graphics.ui.fonts.FontType;
import com.alkrist.maribel.graphics.ui.fonts.TextMeshData;
import com.alkrist.maribel.graphics.ui.fonts.UIText;

public abstract class UICanvas {

	protected List<UIElement> elements;
	protected Map<FontType, List<UIText>> texts;
	
	public UICanvas() {
		elements = new ArrayList<UIElement>();
	}
	
	public void addUIElement(UIElement element) {
		elements.add(element);
	}
	
	public void addUIText(UIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		UIText.TextVAO vao = ResourceLoader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshData(vao, data.getVertexCount());
		
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
	
	public void removeUIText(UIText text) {
		List<UIText> textBatch = texts.get(text.getFont());
		if(textBatch != null) {
			text.deleteFromBuffer();
			textBatch.remove(text);
			if(textBatch.isEmpty()) {
				texts.remove(text.getFont());
			}
		}
	}
	
	public void clearUIElements() {
		elements.clear();
	}
	
	public List<UIElement> getUIElements(){
		return elements;
	}
	
	public boolean removeUIElement(UIElement element) {
		return elements.remove(element);
	}
	
	public abstract void render();
	
	public abstract void update(double deltaTime);
	
}
