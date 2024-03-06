/**
 * returns the border position
 *
 * parameters:
 *
 * framePixelPosition - UI frame center position in pixels
 *
 * framePixelSize - UI frame width and height in pixels
 *
 * returns:
 *
 * vec4(left, right, bottom, top)
 */
vec4 getBorderPosition(vec2 framePixelPosition, vec2 framePixelSize){
	float leftBorder = framePixelPosition.x - framePixelSize.x / 2;
	float rightBorder = framePixelPosition.x + framePixelSize.x / 2;
	float bottomBorder = framePixelPosition.y - framePixelSize.y / 2;
	float topBorder = framePixelPosition.y + framePixelSize.y / 2;

	return vec4(leftBorder, rightBorder, bottomBorder, topBorder);
}

/**
 * Checks if the current frag coordinate is a part of border zone.
 *
 * parameters:
 *
 * currentFragPosition - GL_FragCoord.xy
 *
 * borderPosition - pixels of the frame that are considered border. vec4(left, right, bottom, top)
 *
 * borderSize - thickness of the border
 */
int isOverlappingBorder(vec2 currentFragPosition, vec4 borderPosition, float borderSize){

	if (gl_FragCoord.x < borderPosition.x + borderSize
			|| currentFragPosition.x > borderPosition.y - borderSize
			|| currentFragPosition.y < borderPosition.z + borderSize
			|| currentFragPosition.y > borderPosition.w - borderSize) {
		return 1;
	}

	return 0;
}

/**
 * Checks if the current frag coordinate is a part of corner zone.
 *
 * parameters:
 *
 * currentFragPosition - GL_FragCoord.xy
 *
 * borderPosition - pixels of the frame that are considered border. vec4(left, right, bottom, top)
 *
 * cornerRadius - radius of corner <0;1>, 1 - 50% curvature, 0 - 0% curvature
 */
int isOverlappingCorner(vec2 currentFragPosition, vec4 borderPosition, float cornerRadius){

	float dist = 0;

	if (currentFragPosition.x < borderPosition.x + cornerRadius
			&& currentFragPosition.y < borderPosition.z + cornerRadius) {
		dist = distance(currentFragPosition.xy, vec2(borderPosition.x + cornerRadius, borderPosition.z + cornerRadius));

	} else if (currentFragPosition.x < borderPosition.x + cornerRadius
			&& currentFragPosition.y > borderPosition.w - cornerRadius) {
		dist = distance(currentFragPosition.xy, vec2(borderPosition.x + cornerRadius, borderPosition.w - cornerRadius));

	} else if (currentFragPosition.x > borderPosition.y - cornerRadius
			&& currentFragPosition.y < borderPosition.z + cornerRadius) {
		dist = distance(currentFragPosition.xy, vec2(borderPosition.y - cornerRadius, borderPosition.z + cornerRadius));

	} else if (currentFragPosition.x > borderPosition.y - cornerRadius
			&& currentFragPosition.y > borderPosition.w - cornerRadius) {
		dist = distance(currentFragPosition.xy, vec2(borderPosition.y - cornerRadius, borderPosition.w - cornerRadius));
	}

	if (dist > cornerRadius){
		return 1;
	}

	return 0;
}

/**
 * Check if the current frag coordinate is a part of corner border zone.
 *
 * parameters:
 *
 * currentFragPosition - GL_FragCoord.xy
 *
 * borderPosition - pixels of the frame that are considered border. vec4(left, right, bottom, top)
 *
 * cornerRadius - radius of corner <0;1>, 1 - 50% curvature, 0 - 0% curvature
 *
 * borderSize - thickness of the border
 */
int isOverlappingCornerBorder(vec2 currentFragPosition, vec4 borderPosition, float cornerRadius, float borderSize){
	float distToBorder = 0;

	if (currentFragPosition.x < borderPosition.x + cornerRadius + borderSize
			&& currentFragPosition.y < borderPosition.z + cornerRadius + borderSize){
		distToBorder = distance(currentFragPosition.xy, vec2(borderPosition.x + cornerRadius + borderSize,
				borderPosition.z + cornerRadius + borderSize));

	}else if (currentFragPosition.x < borderPosition.x + cornerRadius + borderSize
			&& currentFragPosition.y > borderPosition.w - cornerRadius - borderSize){
		distToBorder = distance(currentFragPosition.xy, vec2(borderPosition.x + cornerRadius + borderSize,
				borderPosition.w - cornerRadius - borderSize));

	}else if (currentFragPosition.x > borderPosition.y - cornerRadius - borderSize
			&& currentFragPosition.y < borderPosition.z + cornerRadius + borderSize){
		distToBorder = distance(currentFragPosition.xy, vec2(borderPosition.y - cornerRadius - borderSize,
				borderPosition.z + cornerRadius + borderSize));

	}else if (currentFragPosition.x > borderPosition.y - cornerRadius - borderSize
			&& currentFragPosition.y > borderPosition.w - cornerRadius - borderSize){
		distToBorder = distance(currentFragPosition.xy, vec2(borderPosition.y - cornerRadius - borderSize,
				borderPosition.w - cornerRadius - borderSize));
	}

	if(distToBorder > cornerRadius){
		return 1;
	}
	return 0;
}
