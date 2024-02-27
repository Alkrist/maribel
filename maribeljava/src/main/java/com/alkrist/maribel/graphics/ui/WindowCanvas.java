package com.alkrist.maribel.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.render.RenderParameter;
import com.alkrist.maribel.graphics.render.parameter.AlphaBlendingSrcAlpha;
import com.alkrist.maribel.graphics.ui.fonts.FontType;
import com.alkrist.maribel.graphics.ui.fonts.UIText;
import com.alkrist.maribel.graphics.ui.fonts.UITextShader;

public class WindowCanvas extends UICanvas implements Component{

	private static final RenderParameter config = new AlphaBlendingSrcAlpha();
	UITextShader shader;
	
	public WindowCanvas() {
		super();
		shader = UITextShader.getInstance();
	}
	
	@Override
	public void render() {
		glDisable(GL_DEPTH_TEST);    
		glDisable(GL_CULL_FACE);
		config.enable();
		
		elements.forEach(element -> element.render());
		
		if(texts != null) {
			renderTexts();
		}
		
		config.disable();
		glEnable(GL_DEPTH_TEST);     
		glEnable(GL_CULL_FACE);
	}

	private void renderTexts() {
		shader.bind();
		for(FontType font: texts.keySet()) {
			shader.updateFontTexture(font);
			
			for(UIText text: texts.get(font)) {
				text.render(shader);
			}
		}
	}

	@Override
	public void update(double deltaTime) {
		elements.forEach(element -> element.update(deltaTime));
	}

}
