package com.alkrist.maribel.utils.math;

import org.joml.Vector2f;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.ui.constraints.UIConstraints;

public class RayCaster {

	public static boolean intersectsUI(UIConstraints constraints) {
		Vector2f position = constraints.getPosition();
		Vector2f scale = constraints.getScale();
		Vector2f viewportCursorPosition = GLContext.getInput().getCursorPosition();
		
		float screenSpaceCursorX = viewportCursorPosition.x / GLContext.getWindow().getWidth();
		float screenSpaceCursorY = viewportCursorPosition.y / GLContext.getWindow().getHeight();
		
		float screenSpacePositionX = (position.x + 1) * 0.5f;
		float screenSpacePositionY = 1 - ((position.y + 1) * 0.5f);
		
		boolean intersectX = screenSpaceCursorX > (screenSpacePositionX - (scale.x / 2))
				&& screenSpaceCursorX < (screenSpacePositionX + (scale.x / 2));
		
		boolean intersectY = screenSpaceCursorY > (screenSpacePositionY - (scale.y / 2))
				&& screenSpaceCursorY < (screenSpacePositionY + (scale.y / 2));
		
		return intersectX && intersectY;
	}
}
